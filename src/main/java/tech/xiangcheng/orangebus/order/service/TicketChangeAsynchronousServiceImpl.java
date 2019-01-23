package tech.xiangcheng.orangebus.order.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.leaf.util.redis.RedisService;
import tech.xiangcheng.orangebus.leaf.util.string.StringUtil;
import tech.xiangcheng.orangebus.leaf.util.time.DateFormatUtil;
import tech.xiangcheng.orangebus.leaf.util.time.TimeUtil;
import tech.xiangcheng.orangebus.leaf.wechat.message.service.WXMessageService;
import tech.xiangcheng.orangebus.leaf.wechat.newpay.service.WXPay;
import tech.xiangcheng.orangebus.order.domain.enumType.OrderPayStatus;


/**
 *redis数据说明
 *维持3个map结构
 *1. 改签后code与改签前code映射关系，键值为： order.ticket.codeMap
 *2. 改签后order的code与其必要信息，用于log以及微信消息提醒，键值为: order.ticket.msg.{code}
 *3. 改签前order的code与其必要信息，用于log以及微信消息提醒，键值为： order.ticket.msg.{code}
 *2,3值为map结构，
 *2,3中键值包括toatl(总金额，单位元),startStationName(出发站名称）,endStationName(到达站名称）,
 *startTime(出发时间),startCityName(出发城市名称）,endCityName(到达城市名称)
 */
@Service
public class TicketChangeAsynchronousServiceImpl implements TicketChangeAsynchronousService {
	private final ExecutorService executorSercice;
	private final static Logger logger = LoggerFactory.getLogger(TicketChangeAsynchronousServiceImpl.class);
	@Autowired
	private PassengerBusOrderService orderService;
	@Autowired
	WXPay wxpay;
	@Autowired
	WXMessageService wxms;
	private final static String KEY_CODE = "code";
	private final static String KEY_TOTAL = "total";
	private final static String KEY_START_STATION_NAME = "startStationName";
	private final static String KEY_END_STATION_NAME = "endStationName";
	private final static String KEY_START_TIME = "startTime";
	private final static String KEY_OPNEID = "openid";
	private final static String KEY_START_CITY_NAME = "startCityName";
	private final static String KEY_END_CITY_NAME = "endCityName";
	private final static String REDIS_KEY_NEW_OLD_ORDER_MAP = "order.ticket.codeMap";
	private final static String REDIS_KEY_ORDER_MSG_PREFIX = "order.ticket.msg.";
	private final static String REFUND_DESC = "改签成功";
	public TicketChangeAsynchronousServiceImpl() {
		//不要轻易改动线程池类型，现在逻辑建立在单线程执行的顺序
		//若改成非单线程线程池需解决这样一个问题：
		// 在setOrderStatus方法执行前执行了submitTicketPaid()方法
		executorSercice = Executors.newSingleThreadExecutor();
	}
	@PreDestroy
	public void shutdown() {
		executorSercice.shutdown();
	}
	@Override
	@Deprecated
	public void submitTicketChange(JsonObj newOrderParams, JsonObj oldOrderParams) {
		executorSercice.submit(() -> {
			String oldOrderCode = oldOrderParams.getString(KEY_CODE);
			String newOrderCode = newOrderParams.getString(KEY_CODE);
			Map<String, String> oldOrderMsg = orderMsgConvert(oldOrderParams);
			Map<String, String> newOrderMsg = orderMsgConvert(newOrderParams);
			try (Jedis jedis = RedisService.getJedis()) {
				//清缓存
				clearChangeTicketRedis(jedis, oldOrderCode, newOrderCode);
				//在redis中缓存新订单与原订单的映射关系
				jedis.hset(REDIS_KEY_NEW_OLD_ORDER_MAP, newOrderCode, oldOrderCode);
				//在redis中缓存新订单相关信息
				jedis.hmset(REDIS_KEY_ORDER_MSG_PREFIX + newOrderCode, newOrderMsg);
				//在redis中缓存原订单相关信息
				jedis.hmset(REDIS_KEY_ORDER_MSG_PREFIX + oldOrderCode, oldOrderMsg);
			} catch (Exception e) {
				logger.error(e.toString());
				return;
			}
			//更改订单状态
			try {
				setOrderStatusAfterApplyChange(oldOrderCode, newOrderCode);
			} catch (Exception e) {
				logger.error(e.toString());
			}
		});
	}
	
	private Map<String, String> orderMsgConvert(JsonObj orderParams) {
		Map<String, String> map = new HashMap<>();
		orderParams.toMap().forEach((k, v) -> {
			if (v instanceof String) {
				map.put(k, v.toString());
			} else if (v instanceof Double) {
				map.put(k, String.valueOf((Double)v));
			} else if (v instanceof Date) {
				map.put(k, DateFormatUtil.getyyyy_MM_dd_hh_mmDateStr((Date) v));
			}  else {
				throw new IllegalArgumentException("无效的参数类型" + v.getClass());
			}
		});
		return map;
	}
	
	@Deprecated
	private void setOrderStatusAfterApplyChange(String oldOrderCode, String newOrderCode) {
		//原订单状态设置为改签中
		orderService.changeOrderPayStatusByCode(oldOrderCode, OrderPayStatus.SIGN_CHANGING);
		//新订单状态设置为改签待支付
		orderService.changeOrderPayStatusByCode(newOrderCode, OrderPayStatus.CHANGE_UNPAID);
		logger.info("改签处理，更改原订单支付状态为改签中，新订单状态为改签待支付，原订单code: " 
				+ oldOrderCode + "，新订单code为: " + newOrderCode);
	}

	private void setOrderStatusAfterNewOrderPaid(String oldOrderCode, String newOrderCode) {
		//原订单状态设置为已退款
		orderService.changeOrderPayStatusByCode(oldOrderCode, OrderPayStatus.REFUND_COMPLETE);
		//新订单状态设置为已支付
		orderService.changeOrderPayStatusByCode(newOrderCode, OrderPayStatus.PAID);
		logger.info("改签处理，更改原订单支付状态为已退款，新订单状态为已支付，原订单code: " 
				+ oldOrderCode + "，新订单code为: " + newOrderCode);
	}
	
	/**
	 * 判断是否为改签票
	 * @param ticketCode
	 * @return
	 */
	@Override
	public boolean isChangeOrder(String ticketCode) {
		try  (Jedis jedis = RedisService.getJedis()) {
			String oldOrderCode = jedis.hget(REDIS_KEY_NEW_OLD_ORDER_MAP, ticketCode);
			return !StringUtil.nullOrEmpty(oldOrderCode);
		}
	}

	@Override
	public void submitTicketPaid(String ticketCode, String openid) {
		executorSercice.submit(() -> {
			Map<String, String> oldOrderMap = null, newOrderMap = null;
			String oldOrderCode = null;
			try (Jedis jedis = RedisService.getJedis()) {
				//判断是否为改签订单
				oldOrderCode = jedis.hget(REDIS_KEY_NEW_OLD_ORDER_MAP, ticketCode);
				if (StringUtil.nullOrEmpty(oldOrderCode)) {
					//非改签订单
					return;
				}
				//改签订单
				//获取原订单信息
				oldOrderMap = jedis.hgetAll(REDIS_KEY_ORDER_MSG_PREFIX + oldOrderCode); 
				//获取新订单信息
				newOrderMap = jedis.hgetAll(REDIS_KEY_ORDER_MSG_PREFIX + ticketCode);
			}
			//原订单退款
			int total = (int) (Double.valueOf(oldOrderMap.get(KEY_TOTAL)) * 100);//微信以分为单位
			try {
				String res = wxpay.refund(oldOrderCode, TimeUtil.getTimeStamp(), REFUND_DESC, total, total);//全额退款
				if (res.equals("SUCCESS")) {
					//将原订单状态改为已退款，新订单状态改为已支付
					setOrderStatusAfterNewOrderPaid(oldOrderCode, ticketCode);
					//发送改签通知
					logger.debug("发送改签通知");
					try {
						sendChangeTicketMsg(oldOrderMap,newOrderMap, openid);
					} catch (Exception e) {
						logger.error(e.toString());
					}
					//清除redis中缓存
					logger.debug("清楚redis缓存");
					try (Jedis jedis = RedisService.getJedis()) {
						clearChangeTicketRedis(jedis, oldOrderCode, ticketCode);
					}
				} else {
					logger.error("改签处理过程中为原订单退款失败，原订单code : " + oldOrderCode);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("改签处理过程中为原订单退款失败，原订单code : " + oldOrderCode);
			}
		});
	}
	private void clearChangeTicketRedis(Jedis jedis, String oldOrderCode, String newOrderCode) {
		jedis.del(REDIS_KEY_ORDER_MSG_PREFIX + oldOrderCode,REDIS_KEY_ORDER_MSG_PREFIX + newOrderCode );
		jedis.hdel(REDIS_KEY_NEW_OLD_ORDER_MAP, newOrderCode);
	}

    private void sendChangeTicketMsg(Map<String, String> oldOrderMsg, Map<String, String> newOrderMsg, String openid) {
    	String code = newOrderMsg.get(KEY_CODE);
    	logger.info("发送改签通知，新订单code为: " + code);
    	String stationMsg = newOrderMsg.get(KEY_START_STATION_NAME) 
    			+ " - " + newOrderMsg.get(KEY_END_STATION_NAME);
    	String cityMsg = newOrderMsg.get(KEY_START_CITY_NAME) 
    			+ " - " + newOrderMsg.get(KEY_END_CITY_NAME);
    	String departureTime = newOrderMsg.get(KEY_START_TIME);
    	String remark = "如有问题，请联系客服";
    	wxms.sendChangeTicketSuccessMsg(openid, code, stationMsg, departureTime, cityMsg, remark);
    }

}
