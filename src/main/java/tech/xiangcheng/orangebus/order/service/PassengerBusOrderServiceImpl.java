package tech.xiangcheng.orangebus.order.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import tech.xiangcheng.orangebus.account.dao.CouponSnDao;
import tech.xiangcheng.orangebus.account.dao.MemberLevelDao;
import tech.xiangcheng.orangebus.account.dao.PersonalAccountDao;
import tech.xiangcheng.orangebus.account.domain.CheckAccount;
import tech.xiangcheng.orangebus.account.domain.MemberLevel;
import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.account.service.CouponSnService;
import tech.xiangcheng.orangebus.account.service.PersonalAccountService;
import tech.xiangcheng.orangebus.account.service.SvCheckService;
import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.service.BusScheduleService;
import tech.xiangcheng.orangebus.company.service.ScheduleVehicleService;
import tech.xiangcheng.orangebus.company.service.StationCoachNumberService;
import tech.xiangcheng.orangebus.config.dao.ConfigRefundDao;
import tech.xiangcheng.orangebus.config.domain.ConfigCheckStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigPayMethod;
import tech.xiangcheng.orangebus.config.domain.ConfigPayStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigRefundJson;
import tech.xiangcheng.orangebus.config.service.ConfigService;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;
import tech.xiangcheng.orangebus.generalcrud.GeneralService;
import tech.xiangcheng.orangebus.leaf.util.asynchronousService.AsynchronousService;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.leaf.util.sql.SQLGroupResult;
import tech.xiangcheng.orangebus.leaf.util.time.TimeUtil;
import tech.xiangcheng.orangebus.leaf.wechat.message.service.WXMessageService;
import tech.xiangcheng.orangebus.order.dao.PassengerBusOrderDao;
import tech.xiangcheng.orangebus.order.domain.OrderJson;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.order.domain.PreSale;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.order.domain.enumType.OrderCheckStatus;
import tech.xiangcheng.orangebus.order.domain.enumType.OrderPayStatus;
import tech.xiangcheng.orangebus.order.domain.enumType.PayMethod;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class PassengerBusOrderServiceImpl implements PassengerBusOrderService {

	@Autowired
	PassengerBusOrderDao passengerBusOrderDao;
	@Autowired
	BusScheduleService busScheduleService;
	@Autowired
	ScheduleVehicleService scheduleVehicleService;
	@Autowired
	PersonalAccountService personalAccountService;
	@Autowired
	PersonalAccountDao personalAccountDao;
	@Autowired
	ConfigService configService;
	@Autowired
	CouponSnService couponSnService;
	@Autowired
	CouponSnDao couponSnDao;
	@Autowired
	ConfigRefundDao configRefundDao;
	@Autowired
	MemberLevelDao memberLevelDao;
	@Autowired
	StationCoachNumberService stationCoachNumberService;
	@Autowired
	PreSaleService preSaleService;
	@Autowired
	GeneralService generalService;
	@Autowired
	WXMessageService wxms;
	private EntityManagerFactory emf;
	@Autowired
	AsynchronousService asynchronousService;
	@Autowired
	SvCheckService svCheckService;
	@PersistenceUnit
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.emf = emf;
	}

	private final Logger logger = LoggerFactory.getLogger(PassengerBusOrderServiceImpl.class);

	@Override
	public JsonObj orderServiceType(String vehicleScheduleId, String payIndex) {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					"select service, sum(amount) from passenger_bus_order where schedule_vehicle_id=? and pay_method =? and pay_status= ? and check_status=? group by service order by service");
			q.setParameter(1, vehicleScheduleId);
			q.setParameter(2, payIndex);
			q.setParameter(3, OrderPayStatus.PAID.getValue());
			q.setParameter(4, OrderCheckStatus.CHECK.getValue());
			List<Object[]> res = q.getResultList();
			JsonObj jo = JsonObj.instance().putProperty("services", res.stream().map(tuple -> {
				JsonObj joTmp = JsonObj.instance();
				String k = (String) tuple[0];
				joTmp.putProperty(null == k ? "null" : k, tuple[1]);
				return joTmp.toJson();
			}).collect(Collectors.toList()));
			return jo;
		} finally {
			em.close();
		}

	}

	//wcn 20170915
	@Override
	public JsonObj orderSVDetailServiceType(String scheduleVehicleId, String payIndex) {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					"select   p.service,a.name,p.amount,p.total,p.remarks,p.id,p.co_type  from passenger_bus_order p left join   station_coach_number   s1   on   p.terminal_station_coach_id = s1.id left join   station s2    on   s1.station_id = s2.id left join  sys_area a  on  s2.area_id = a.id where  p.schedule_vehicle_id = ?  and pay_method =? and pay_status= ? and check_status=?  order by service");
			q.setParameter(1, scheduleVehicleId);
			q.setParameter(2, payIndex);
			q.setParameter(3, OrderPayStatus.PAID.getValue());
			q.setParameter(4, OrderCheckStatus.CHECK.getValue());

			List<Object[]> res = q.getResultList();
			System.out.println(res.size());
			JsonObj joTmp = JsonObj.instance();
			JsonObj jo = JsonObj.instance().putProperty("services", res.stream().map(tuple -> {
				joTmp.putProperty("service", tuple[0])
				.putProperty("termini", tuple[1])
				.putProperty("amount", tuple[2])
				.putProperty("price", tuple[3])
				.putProperty("remarks", tuple[4])
				.putProperty("id", tuple[5])
				.putProperty("co_type", tuple[6]);
				
				return joTmp.toJson();
			}).collect(Collectors.toList()));
			return jo;
		} finally {
			em.close();
		}
	}
	
//	@Transactional(isolation = Isolation.REPEATABLE_READ)
//	@Override
//	public JsonObj getOrderStatisticMessagee(String scheduleVehicleId) {
//		EntityManager em = emf.createEntityManager();
//		try {
//			Query paidQuery = em.createNativeQuery(
//					"select amount, check_status, pay_method from passenger_bus_order where schedule_vehicle_id=? and pay_status=?"
////					+ " and check_status=?"
//					+ " ");
//			paidQuery.setParameter(1, scheduleVehicleId);
//			paidQuery.setParameter(2, OrderPayStatus.PAID.getValue());//前提是已支付
////			paidQuery.setParameter(3, OrderCheckStatus.CHECK.getValue());
//			List<Object[]> res = paidQuery.getResultList();
//			Long checkNum = 0L, paidNum = 0L, wxPayNum = 0L, ticketPayNum = 0L, qrCodePayNum = 0L, cashPayNum = 0L,
//					unCheckNum = 0L;
//			for (Object[] os : res) {
//				Integer amount = (Integer) os[0];
//				String checkStatus = (String) os[1];
//				String payMethod = (String) os[2];
//				
//				if (payMethod.equals(PayMethod.WEIXIN.getValue())) {
//					wxPayNum += amount;
//					if (checkStatus.equals(OrderCheckStatus.CHECK.getValue())) {
//						checkNum += amount;
//					} else {
//						unCheckNum += amount;
//					}
//				}
//				
//				if (checkStatus.equals(OrderCheckStatus.CHECK.getValue())) {
////					checkNum += amount;
//					paidNum += amount;
//					
//					if (payMethod.equals(PayMethod.TICKET.getValue())) {
//						ticketPayNum += amount;
//					} else if (payMethod.equals(PayMethod.CASH.getValue())) {
//						cashPayNum += amount;
//					} else if (payMethod.equals(PayMethod.QRCODE.getValue())) {
//						qrCodePayNum += amount;
//					} else if (!payMethod.equals(PayMethod.WEIXIN.getValue())){
//						throw new IllegalArgumentException("payMethod: " + payMethod + "is not defined");
//					}
//				} 
////				else {
////					unCheckNum += amount;
////				}
//				
//			}
//			JsonObj jsonObj = JsonObj.instance();
//			//前提是已支付
//			jsonObj.putProperty("checkNum", checkNum);//已验票数
//			jsonObj.putProperty("payNum", paidNum);//已支付订单（含微信、二维码、现金、票据订单）
//			jsonObj.putProperty("ticketPayNum", ticketPayNum);//票据订单
//			jsonObj.putProperty("qrCodePayNum", qrCodePayNum);//二维码订单
//			jsonObj.putProperty("wxOnlinePayNum", wxPayNum);//微信订单
//			jsonObj.putProperty("cashPayNum", cashPayNum);//现金订单
//			jsonObj.putProperty("uncheckNum", unCheckNum);//未验票数
//			//在此新增两个数：
//			//1. 未支付保留座位数： 未支付Constant.unpaid_order_hold_time 时间内的订单数
//			//2. 余座数
//			
//			//修改的时候备份该方法不要直接在上面改
//			return jsonObj;
//		} finally {
//			em.close();
//		}
//	}

	
	@Override
	public JsonObj getOrderStatisticMessagee(String scheduleVehicleId) {
		Long checkNum = 0L, paidNum = 0L, wxPayNum = 0L, ticketPayNum = 0L, qrCodePayNum = 0L, cashPayNum = 0L,
				unCheckNum = 0L, holdNum = 0L, remainNum = 0L;
		EntityManager em = emf.createEntityManager();
		try {
			Query paidQuery = em.createNativeQuery(
//					"select amount, check_status, pay_method from passenger_bus_order where schedule_vehicle_id=? and pay_status=?"
					"SELECT "
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND check_status = 1 AND service NOT LIKE '%货款%' THEN amount ELSE 0 END), 0) AS payNum,"//车上人数
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND pay_method = 1 THEN amount ELSE 0 END), 0) AS wxOnlinePayNum,"//微信购票人数
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND pay_method = 6 THEN amount ELSE 0 END), 0) AS ticketPayNum,"//票据人数
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND pay_method = 4 THEN amount ELSE 0 END), 0) AS qrcodePayNum,"//微信二维码购票人数
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND pay_method = 5 THEN amount ELSE 0 END), 0) AS cashPayNum,"//现金购票人数，含货款
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND pay_method = 1 AND check_status = 1 THEN amount ELSE 0 END), 0) AS checkNum,"//已验票数
					+ "IFNULL(SUM(CASE WHEN pay_status = 1 AND pay_method = 1 AND check_status = 0 THEN amount ELSE 0 END), 0) AS uncheckNum,"//未验票数
					+ "IFNULL(SUM(CASE WHEN pay_status = 0 AND pay_method = 1 AND (UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date)) < ? "
					+ "THEN amount ELSE 0 END), 0) AS holdNum"
					+ " FROM passenger_bus_order  "
					+ "WHERE "
					+ "schedule_vehicle_id= ?");
			paidQuery.setParameter(1, NOT_PAID_ORDER_VAILD_SECONDS);
			paidQuery.setParameter(2, scheduleVehicleId);
			List<Object[]> res = paidQuery.getResultList();
			if (res.isEmpty()) {
				logger.warn("无schduleVehicleId为:" + scheduleVehicleId + "的订单");
			} else {
				Object[] o = res.get(0);
				paidNum = ((BigDecimal)o[0]).longValue();
				wxPayNum = ((BigDecimal)o[1]).longValue();
				ticketPayNum = ((BigDecimal)o[2]).longValue();
				qrCodePayNum = ((BigDecimal)o[3]).longValue();
				cashPayNum = ((BigDecimal)o[4]).longValue();
				checkNum = ((BigDecimal)o[5]).longValue();
				unCheckNum = ((BigDecimal)o[6]).longValue();
				holdNum = ((BigDecimal)o[7]).longValue();
			}
		} finally {
			em.close();
		}
		JsonObj jsonObj = JsonObj.instance();
		jsonObj.putProperty("checkNum", checkNum);//已验票数
		jsonObj.putProperty("payNum", paidNum);//已支付订单（含微信、二维码、现金、票据订单）
		jsonObj.putProperty("ticketPayNum", ticketPayNum);//票据订单
		jsonObj.putProperty("qrCodePayNum", qrCodePayNum);//二维码订单
		jsonObj.putProperty("wxOnlinePayNum", wxPayNum);//微信订单
		jsonObj.putProperty("cashPayNum", cashPayNum);//现金订单
		jsonObj.putProperty("uncheckNum", unCheckNum);//未验票数
		jsonObj.putProperty("holdNum", holdNum);//未支付尚在支付有效期票数
		remainNum = remainTicketsAmount(scheduleVehicleId);
		jsonObj.putProperty("remainNum", remainNum);
		//在此新增两个数：
		//1. 未支付保留座位数： 未支付Constant.unpaid_order_hold_time 时间内的订单数
		//2. 余座数
		//原方法见上面被注释部分
		return jsonObj;
	}

	
	
	
	@Override
	public List<PassengerBusOrder> findByBuyer_NameOrderByCreateDateDesc(String username) {
		return this.passengerBusOrderDao.findByBuyer_NameOrderByCreateDateDesc(username);
	}

	@Override
	public List<PassengerBusOrder> findByBuyer_idOrderByCreateDateDesc(String buyerId) {
		return this.passengerBusOrderDao.findByBuyer_idOrderByCreateDateDesc(buyerId);
	}

	@Deprecated
	@Override
	//未用到
	public PassengerBusOrder save_bk(String buyerId, String busScheduleId, String schedule_vehicle_id, int total) {
		BusSchedule busSchedule = busScheduleService.getBusScheduleById(busScheduleId);
		ScheduleVehicle scheduleVehicle = scheduleVehicleService.getScheduleVehicleById(schedule_vehicle_id);
		PersonalAccount buyer = personalAccountService.getAccountById(buyerId);
		if (null == busSchedule || null == scheduleVehicle || null == buyer) {
			return null;
		}
		PassengerBusOrder order = new PassengerBusOrder();
		order.setBuyer(buyer);
		order.setId(RandomUtil.getId());
		order.setBusSchedule(busSchedule);
		order.setScheduleVehicle(scheduleVehicle);
		order.setTotal((double) total);
		order.setCode("OD" + TimeUtil.getSecondTimeStamp());
		// 设置未支付状态
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		configPayStatus.setId(OrderPayStatus.UNPAID.getValue());
		configPayStatus.setName(OrderPayStatus.UNPAID.getName());
		order.setConfigPayStatus(configPayStatus);
		// 设置未验票状态
		ConfigCheckStatus configCheckStatus = new ConfigCheckStatus();
		configCheckStatus.setId(OrderCheckStatus.CHECK.getValue());
		configCheckStatus.setName(OrderCheckStatus.CHECK.getName());
		order.setConfigCheckStatus(configCheckStatus);
		// passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		order.setCreateDate(new Date());
		// 设置支付方式
		ConfigPayMethod configPayMethod = new ConfigPayMethod();
		configPayMethod.setId(PayMethod.QRCODE.getValue());
		order.setConfigPayMethod(configPayMethod);
		// 设置逻辑删除字段
		order.setDelFlag(Constant.NOT_DELTED);
		return this.passengerBusOrderDao.save(order);
//		synchronized (this) {
//			Long remainTicketsAmount = remainTicketsAmount(scheduleVehicle.getId());
//			if (remainTicketsAmount - order.getAmount() > 0) {
//				return this.passengerBusOrderDao.save(order);
//			} else {
//				logger.info("请求保存订单失败，订单包含票数: " + order.getAmount() + ",实际剩余车票:" + remainTicketsAmount);
//				return null;
//			}
//		}
	}

	@Override
	public List<PassengerBusOrder> findList(String bus_schedule_id,String schedule_vehicle_id) {
		return passengerBusOrderDao.findByBusSchedule_IdAndScheduleVehicle_Id(bus_schedule_id, schedule_vehicle_id);
	}
	
	@Override
	public List<PassengerBusOrder> updateList(List<PassengerBusOrder> passengerbusorders) {
		return passengerBusOrderDao.save(passengerbusorders);
	}
	@Override
	public PassengerBusOrder save(PassengerBusOrder passengerBusOrder) {
		if (null == passengerBusOrder || null == passengerBusOrder.getBusSchedule()
				|| null == passengerBusOrder.getAmount() || !(passengerBusOrder.getAmount().intValue() >= 0)
				|| null == passengerBusOrder.getTotal() || !(passengerBusOrder.getTotal().intValue() >= 0)
				|| null == passengerBusOrder.getBuyer()
				|| null == personalAccountService.getAccountById(passengerBusOrder.getBuyer().getId())
				|| null == busScheduleService.getBusScheduleById(passengerBusOrder.getBusSchedule().getId()))
			return null;
		passengerBusOrder.setId(RandomUtil.getId());
		// 设置未支付状态
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		configPayStatus.setId(OrderPayStatus.UNPAID.getValue());
		configPayStatus.setName(OrderPayStatus.UNPAID.getName());
		passengerBusOrder.setConfigPayStatus(configPayStatus);
		// 设置未验票状态
		ConfigCheckStatus configCheckStatus = new ConfigCheckStatus();
		configCheckStatus.setId(OrderCheckStatus.UNCHECK.getValue());
		configCheckStatus.setName(OrderCheckStatus.UNCHECK.getName());
		passengerBusOrder.setConfigCheckStatus(configCheckStatus);

		// 设置支付方式
		ConfigPayMethod configPayMethod = new ConfigPayMethod();
		configPayMethod.setId(PayMethod.WEIXIN.getValue());
		passengerBusOrder.setConfigPayMethod(configPayMethod);
		// passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		passengerBusOrder.setCreateDate(new Date());
		String code = "OD" + TimeUtil.getSecondTimeStamp();
		passengerBusOrder.setCode(code);
		// 设置逻辑删除字段
		passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		return this.passengerBusOrderDao.save(passengerBusOrder);
//		synchronized (this) {
//			Long remainTicketsAmount = remainTicketsAmount(passengerBusOrder.getScheduleVehicle().getId());
//			if (remainTicketsAmount - passengerBusOrder.getAmount() > 0) {
//				return this.passengerBusOrderDao.save(passengerBusOrder);
//			} else {
//				logger.info("请求保存订单失败，订单包含票数: " + passengerBusOrder.getAmount() + ",实际剩余车票:" + remainTicketsAmount);
//				return null;
//			}
//		}
	}
	@Override
	@Transactional
	public PassengerBusOrder saveOrderByPs(PassengerBusOrder passengerBusOrder) {
//		if (null == passengerBusOrder || null == passengerBusOrder.getBusSchedule()
//				|| null == passengerBusOrder.getAmount() || !(passengerBusOrder.getAmount().intValue() >= 0)
//				|| null == passengerBusOrder.getTotal() || !(passengerBusOrder.getTotal().intValue() >= 0)
//				|| null == passengerBusOrder.getRemarks() || !"".equals(passengerBusOrder.getRemarks())) {
//			System.out.println(passengerBusOrder.getBusSchedule());
//			System.out.println(passengerBusOrder);
//			System.out.println(passengerBusOrder.getTotal().intValue());
//			System.out.println(passengerBusOrder.getRemarks());
//			System.out.println(passengerBusOrder.getAmount().intValue());
//			return null;
//		}
//			return null;
		passengerBusOrder.setId(RandomUtil.getId());
		
		// 设置未支付状态
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		configPayStatus.setId(OrderPayStatus.PAID.getValue());
		configPayStatus.setName(OrderPayStatus.PAID.getName());
		passengerBusOrder.setConfigPayStatus(configPayStatus);
		// 设置未验票状态
		ConfigCheckStatus configCheckStatus = new ConfigCheckStatus();
		configCheckStatus.setId(OrderCheckStatus.CHECK.getValue());
		configCheckStatus.setName(OrderCheckStatus.CHECK.getName());
		passengerBusOrder.setConfigCheckStatus(configCheckStatus);

		// 设置支付方式
		ConfigPayMethod configPayMethod = new ConfigPayMethod();
		configPayMethod.setId(PayMethod.CASH.getValue());
		passengerBusOrder.setConfigPayMethod(configPayMethod);
		
		passengerBusOrder.setCreateDate(new Date());
		String code = "OD" + TimeUtil.getSecondTimeStamp();
		passengerBusOrder.setCode(code);
		
		passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		
		return this.passengerBusOrderDao.save(passengerBusOrder);
	}
	//wcn20171019
	@Override
	@Transactional
	public PassengerBusOrder save(PassengerBusOrder passengerBusOrder,PassengerBusOrder oldOrder) {
		if (null == passengerBusOrder || null == passengerBusOrder.getBusSchedule()
				|| null == passengerBusOrder.getAmount() || !(passengerBusOrder.getAmount().intValue() >= 0)
				|| null == passengerBusOrder.getTotal() || !(passengerBusOrder.getTotal().intValue() >= 0)
				|| null == passengerBusOrder.getBuyer()
				|| null == personalAccountService.getAccountById(passengerBusOrder.getBuyer().getId())
				|| null == busScheduleService.getBusScheduleById(passengerBusOrder.getBusSchedule().getId()))
			return null;
		
		passengerBusOrder.setId(RandomUtil.getId());
		
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		// 设置未支付状态
		if(oldOrder==null) {//正常买票
			configPayStatus.setId(OrderPayStatus.UNPAID.getValue());
			configPayStatus.setName(OrderPayStatus.UNPAID.getName());
		}else {//改签各种状态
			ConfigPayStatus oldConfigPayStatus = new ConfigPayStatus();//旧订单
			Double total = passengerBusOrder.getTotal();
			Double oldTotal = oldOrder.getTotal();
			if(Double.doubleToLongBits(total) == Double.doubleToLongBits(oldTotal)) {
//				passengerBusOrder.setAlterPrice(0.00);
				configPayStatus.setId(OrderPayStatus.PAID.getValue());
				configPayStatus.setName(OrderPayStatus.PAID.getName());
				
				oldConfigPayStatus.setId(OrderPayStatus.COMPLETE_CHANGED.getValue());
				oldConfigPayStatus.setName(OrderPayStatus.COMPLETE_CHANGED.getName());

				//退回旧订单代金券，改签成功立刻退回代金券
				if(oldOrder.getCouponSn()!=null){
					couponSnService.refundCouponOnce(oldOrder.getCouponSn());
				}

			}else if(Double.doubleToLongBits(total) > Double.doubleToLongBits(oldTotal)) {//支付
				passengerBusOrder.setAlterPrice(total-oldTotal);//保存差价，新订单保存要额外支付的差价
				configPayStatus.setId(OrderPayStatus.CHANGE_UNPAID.getValue());
				configPayStatus.setName(OrderPayStatus.CHANGE_UNPAID.getName());
				
				oldConfigPayStatus.setId(OrderPayStatus.SIGN_CHANGING.getValue());
				oldConfigPayStatus.setName(OrderPayStatus.SIGN_CHANGING.getName());
			}else {//退款
				oldOrder.setAlterPrice(oldTotal-total);//保存差价，旧订单保存要退款的差价
				configPayStatus.setId(OrderPayStatus.PAID.getValue());
				configPayStatus.setName(OrderPayStatus.PAID.getName());
				
				oldConfigPayStatus.setId(OrderPayStatus.CHANGE_REFUND.getValue());
				oldConfigPayStatus.setName(OrderPayStatus.CHANGE_REFUND.getName());
				
				//退回旧订单代金券，改签成功立刻退回代金券
				if(oldOrder.getCouponSn()!=null){
					couponSnService.refundCouponOnce(oldOrder.getCouponSn());
				}
			}
			oldOrder.setConfigPayStatus(oldConfigPayStatus);
			passengerBusOrder.setAlterOrderId(oldOrder.getId());
		}
		passengerBusOrder.setConfigPayStatus(configPayStatus);
		// 设置未验票状态
		ConfigCheckStatus configCheckStatus = new ConfigCheckStatus();
		configCheckStatus.setId(OrderCheckStatus.UNCHECK.getValue());
		configCheckStatus.setName(OrderCheckStatus.UNCHECK.getName());
		passengerBusOrder.setConfigCheckStatus(configCheckStatus);

		// 设置支付方式
		ConfigPayMethod configPayMethod = new ConfigPayMethod();
		configPayMethod.setId(PayMethod.WEIXIN.getValue());
		passengerBusOrder.setConfigPayMethod(configPayMethod);
		// passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		passengerBusOrder.setCreateDate(new Date());
		String code = "OD" + TimeUtil.getSecondTimeStamp();
		passengerBusOrder.setCode(code);
		// 设置逻辑删除字段
		passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		if(oldOrder!=null) {
			passengerBusOrderDao.save(oldOrder);
			logger.info("旧订单" + oldOrder +"改签成功，新订单为: "+passengerBusOrder);
		}
		return this.passengerBusOrderDao.save(passengerBusOrder);
//		synchronized (this) {
//			Long remainTicketsAmount = remainTicketsAmount(passengerBusOrder.getScheduleVehicle().getId());
//			if (remainTicketsAmount - passengerBusOrder.getAmount() > 0) {
//				return this.passengerBusOrderDao.save(passengerBusOrder);
//			} else {
//				logger.info("请求保存订单失败，订单包含票数: " + passengerBusOrder.getAmount() + ",实际剩余车票:" + remainTicketsAmount);
//				return null;
//			}
//		}
	}

//	@Override
//	public Double getSumSpentByUserId1(String userId) {
//		return passengerBusOrderDao.getSumSpentByUserId(OrderPayStatus.PAID.getValue(), OrderCheckStatus.CHECK.getValue(), userId);
//	}

	@Override
	public PassengerBusOrder getOrderById(String id) {
		if (null == id)
			return null;
		return passengerBusOrderDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
	}

	@Deprecated
	@Override
	public boolean setOrderPaid(String orderId) {
		if (null == orderId || orderId.length() <= 0)
			return false;

		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		// configPayStatus=configService.findById_configPayStatus(statusToBeChanged);
		configPayStatus.setId(OrderPayStatus.PAID.getValue());
		PassengerBusOrder order = getOrderById(orderId);
		order.setConfigPayStatus(configPayStatus);
		order = passengerBusOrderDao.save(order);
		return true;

	}
	
	//wcn20171023
	@Transactional
	@Override
	public boolean cancelChangeSign(String statusToBeChanged, String orderId) {
		/*
		 * 取消改签
		 */
		if (null == orderId || orderId.length() <= 0 || null == statusToBeChanged || statusToBeChanged.length() <= 0)
			// return "订单Id或目标状态为空";
			return false;
		PassengerBusOrder order = getOrderById(orderId);
		if (null == order)
			// return "订单为空";
			return false;
		PassengerBusOrder oldOrder = getOrderById(order.getAlterOrderId());
		if (null == oldOrder)
			return false;
		
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		statusToBeChanged = OrderPayStatus.CHANGE_CANCEL.getValue();
		configPayStatus.setId(statusToBeChanged);
		order.setConfigPayStatus(configPayStatus);
		order = passengerBusOrderDao.save(order);
		
		//如新订单有使用代金券，则退回新订单代金券
		if(order.getCouponSn()!=null){
			couponSnService.refundCouponOnce(order.getCouponSn());
		}
		
		ConfigPayStatus oldConfigPayStatus = new ConfigPayStatus();
		statusToBeChanged = OrderPayStatus.PAID.getValue();
		oldConfigPayStatus.setId(statusToBeChanged);
		oldOrder.setConfigPayStatus(oldConfigPayStatus);
		oldOrder = passengerBusOrderDao.save(oldOrder);
		
		// configPayStatus=configService.findById_configPayStatus(statusToBeChanged);
		return true;
	}
	
	
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Transactional
	@Override
	public boolean changePayStatus(String statusToBeChanged, String orderId) {
		/*
		 * 1. 取消订单 2. 退款
		 */
		if (null == orderId || orderId.length() <= 0 || null == statusToBeChanged || statusToBeChanged.length() <= 0)
			// return "订单Id或目标状态为空";
			return false;
		PassengerBusOrder order = getOrderById(orderId);
		if (null == order)
			// return "订单为空";
			return false;
		// if(statusToBeChanged.equals(OrderPayStatus.PAID.getValue())
		// &&
		// order.getConfigPayStatus().getId()==OrderPayStatus.UNPAID.getValue()){
		// return "修改状态不合法，订单状态为‘未支付’，目标状态为‘支付’。";
		// }
		if ("toBeCancel".equals(statusToBeChanged)) {
			statusToBeChanged = OrderPayStatus.CANCEL.getValue();
		} else if ("toBeRefund".equals(statusToBeChanged)) {
			statusToBeChanged = OrderPayStatus.REFUND_SUBMIT.getValue();
		} else {
			return false;
		}
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		// configPayStatus=configService.findById_configPayStatus(statusToBeChanged);
		configPayStatus.setId(statusToBeChanged);

		if (( // 1. 取消订单
		statusToBeChanged.equals(OrderPayStatus.CANCEL.getValue())// 申请修改的支付状态为：取消
				&& order.getConfigPayStatus().getId().equals(OrderPayStatus.UNPAID.getValue())) // 原订单支付状态为：未支付
				|| (statusToBeChanged.equals(OrderPayStatus.REFUND_SUBMIT.getValue()) // 申请修改的支付状态为：退款状态
						&& order.getConfigPayStatus().getId().equals(OrderPayStatus.PAID.getValue()) // 原订单支付状态为：已支付
						&& order.getConfigCheckStatus().getId().equals(OrderCheckStatus.UNCHECK.getValue()))// 原订单支付状态为：未验票，即未上车

		) {
			order.setConfigPayStatus(configPayStatus);
			// 返回代金券
			CouponSn couponSn = order.getCouponSn();
			if (couponSn != null && couponSnService.findById(couponSn.getId()) != null) {
				couponSn.setRemainTimes(couponSn.getRemainTimes() + 1);
				couponSn = couponSnDao.save(couponSn);
				order.setCouponSn(null);
			}
			order = passengerBusOrderDao.save(order);
			// return "修改状态提交成功";
			return true;
		}

		// return
		// "修改状态提交失败，目标状态："+statusToBeChanged+"，订单状态为："+order.getConfigPayStatus().getId();
		return false;
	}
	
	@Override
	public boolean changeOrderStatus(String statusToBeChanged, String orderId) {
		/*
		 * 取消服务的表格
		 */
		if (null == orderId || orderId.length() <= 0 || null == statusToBeChanged || statusToBeChanged.length() <= 0)
			// return "订单Id或目标状态为空";
			return false;
		PassengerBusOrder order = getOrderById(orderId);
		if (null == order)
			// return "订单为空";
			return false;

		if ("toBeCancel".equals(statusToBeChanged)) {
			statusToBeChanged = OrderPayStatus.CANCEL.getValue();
		} else {
			return false;
		}
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		// configPayStatus=configService.findById_configPayStatus(statusToBeChanged);
		configPayStatus.setId(statusToBeChanged);

		if ( // 改变状态
		statusToBeChanged.equals(OrderPayStatus.CANCEL.getValue())// 申请修改的支付状态为：取消
				
		) {
			order.setConfigPayStatus(configPayStatus);
			// 返回代金券
			CouponSn couponSn = order.getCouponSn();
			if (couponSn != null && couponSnService.findById(couponSn.getId()) != null) {
				couponSn.setRemainTimes(couponSn.getRemainTimes() + 1);
				couponSn = couponSnDao.save(couponSn);
				order.setCouponSn(null);
			}
			order = passengerBusOrderDao.save(order);
			// return "修改状态提交成功";
			return true;
		}

		// return
		// "修改状态提交失败，目标状态："+statusToBeChanged+"，订单状态为："+order.getConfigPayStatus().getId();
		return false;
	}
	
	
	@Override
	public PassengerBusOrder getOrderByCode(String code) {
		if (null == code || code.isEmpty())
			return null;
		return passengerBusOrderDao.findByCodeAndDelFlag(code, Constant.NOT_DELTED);
	}

	@Override
	public List<PassengerBusOrder> getPaidOrderByBusScheduleId(String id) {
		List<PassengerBusOrder> orders = new ArrayList<>();
		if (!(null == id) && !id.isEmpty())
			orders = passengerBusOrderDao.findByBusSchedule_IdAndConfigPayStatus_Id(id, OrderPayStatus.PAID.getValue());
		return orders;
	}

	@Override
	public List<PassengerBusOrder> getPaidOrderByBusScheduleIdCheckStatus(String id, String checkStatus) {
		List<PassengerBusOrder> orders = new ArrayList<>();
		if (!(null == id) && !id.isEmpty())
			orders = passengerBusOrderDao
					.findByBusSchedule_IdAndConfigPayStatus_IdAndConfigCheckStatus_IdOrderByOriginalDateAsc(id,
							OrderPayStatus.PAID.getValue(), checkStatus);
		return orders;
	}

	@Override
	public List<PassengerBusOrder> getPaidOrderByBusScheduleIdCheckStatusAndScheduleVehicleId(String id,
			String checkStatus, String scheduleVehicleId) {
		List<PassengerBusOrder> orders = new ArrayList<>();
		if (!(null == id) && !id.isEmpty())
			orders = passengerBusOrderDao
					.findByBusSchedule_IdAndConfigPayStatus_IdAndConfigCheckStatus_IdAndConfigPayMethod_IdAndScheduleVehicle_IdOrderByOriginalDateAsc(
							id, OrderPayStatus.PAID.getValue(), checkStatus, PayMethod.WEIXIN.getValue(), scheduleVehicleId);
		return orders;
	}

	@Override
	@Transactional
	public int setOrderChecked(String id) {
		if (null == id || id.isEmpty())
			return -1; // id无效，没有对应订单
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
		if (null == order)
			return -1; // id无效
		if (order.getConfigPayStatus().getId().equals(OrderPayStatus.UNPAID.getValue()))
			return -2; // 订单尚未支付，不能设为已检票
		if (order.getConfigCheckStatus().getId().equals(OrderCheckStatus.CHECK.getValue()))
			return -3;// 该订单已经检票
		// 设置验票状态
		passengerBusOrderDao.setCheckStatusByOrderId(id, OrderCheckStatus.CHECK.getValue());
		return 0;
	}

	@Override
	public int setOrderCheckedByCode(String code) {
		if (null == code || code.isEmpty())
			return -1; // code无效，没有对应订单
		PassengerBusOrder order = passengerBusOrderDao.findByCodeAndDelFlag(code, Constant.NOT_DELTED);
		if (null == order)
			return -1; // id无效
		if (order.getConfigPayStatus().getId().equals(OrderPayStatus.UNPAID.getValue()))
			return -2; // 订单尚未支付，不能设为已检票
		if (order.getConfigCheckStatus().getId().equals(OrderCheckStatus.CHECK.getValue()))
			return -3;// 该订单已经检票
		// 设置验票状态
		passengerBusOrderDao.setCheckStatusByOrderCode(code, OrderCheckStatus.CHECK.getValue());
		return 0;
	}

	@Override
	@Transactional
	public int setOrderUnChecked(String id) {
		if (null == id || id.isEmpty())
			return -1; // id无效，没有对应订单
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
		if (null == order)
			return -1; // id无效
		if (!order.getConfigCheckStatus().getId().equals(OrderCheckStatus.UNCHECK.getValue())) {
			// 设置未验票状态
			passengerBusOrderDao.setCheckStatusByOrderId(id, OrderCheckStatus.UNCHECK.getValue());
		}
		return 0;
	}

//	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public String changeOrderCheckStatus(String id, String status,String checker) {
		if (null == id || id.isEmpty())
			return "-1";// id无效，没有对应订单
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
		if (null == order)
			return "-1"; // id无效
		if (status.equals(OrderCheckStatus.CHECK.getValue())) {
			if (!order.getConfigPayStatus().getId().equals(OrderPayStatus.PAID.getValue()))
				return "-2"; // 订单状态不是已支付，不能设为已检票
			if (order.getConfigCheckStatus().getId().equals(status))
				return "-4";// 订单已检票，不能重复检票
		}
		if (!order.getConfigCheckStatus().getId().equals(status)) {
			try {
				passengerBusOrderDao.setCheckStatusCheckerByOrderId(id, status,checker,new Date());
				logger.info("订单号："+order.getCode()+",  用户手机号"+order.getBuyer().getPhone()+", 原状态："+order.getConfigCheckStatus().getName()+", 目标状态："+status);
				CheckAccount user = (CheckAccount) SecurityUtils.getSubject().getPrincipal();
				if(status.equals(OrderCheckStatus.CHECK.getValue()))
					logger.info(user.getName()+"验票上车，"+"电话号码："+order.getBuyer().getPhone()+",数量："+order.getAmount()+"张,总价："+order.getTotal()+"元");
				else if(status.equals(OrderCheckStatus.UNCHECK.getValue()))
					logger.info(user.getName()+"取消验票，"+"电话号码："+order.getBuyer().getPhone()+",数量："+order.getAmount()+"张,总价："+order.getTotal()+"元");
			} catch (Exception e) {
				logger.info(""+e);
			}
		}
//		return order.getBuyer().getId();
		return "1";
	}

	@Override
	public Double getSumSpentByUserId(String userId) {
		// if (null == userId || userId.isEmpty()) return null;
		Double sum = passengerBusOrderDao.getSumSpentByUserId(OrderPayStatus.PAID.getValue(), OrderCheckStatus.CHECK.getValue(), userId);
		return null == sum ? 0 : sum.doubleValue();
		// return passengerBusOrderDao.getSumSpentByUserId(userId);
	}

	@Override
	public Double getSumSpentByBusScheduleIdAndScheduleVehicleId(String busScheduleId, String scheduleVehicleId) {
		// if (null == userId || userId.isEmpty()) return null;
		Double sum = passengerBusOrderDao.getSumSpentByBusScheduleIdAndScheduleVehicleId(OrderPayStatus.PAID.getValue(),
				busScheduleId, scheduleVehicleId);
		return null == sum ? 0 : sum.doubleValue();
		// return passengerBusOrderDao.getSumSpentByUserId(userId);
	}

	@Override
	@Transactional
	public int setPaidAndChecked(String orderId) {
		if (null == orderId || orderId.isEmpty())
			return -1;// 订单id无效
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(orderId, Constant.NOT_DELTED);
		if (null == order)
			return -1;// 订单id无效
		ConfigPayStatus configPayStatus = order.getConfigPayStatus();
		if (null == configPayStatus)
			return -2;// 订单异常，保存时没有设置支付状态
		if (configPayStatus.getId().equals(OrderPayStatus.PAID.getValue()))
			return -3; // 该订单已支付
		// passengerBusOrderDao.setPaidByOrderId(order.getId());
		passengerBusOrderDao.setPayCheckedStatusByOrderId(order.getId(), OrderPayStatus.PAID.getValue(), OrderCheckStatus.CHECK.getValue());
		// order = passengerBusOrderDao.findByIdAndDelFlag(orderId,
		// Constant.NOT_DELTED);
		// if (null == order ||
		// !order.getConfigPayStatus().getId().equals(OrderPayStatus.PAID.getValue()))
		// return -4; //修改支付状态失败
		return 0;// 成功设为已支付
	}

	@Override
	@Transactional
	public int setPaid(String orderId) {
		if (null == orderId || orderId.isEmpty())
			return -1;// 订单id无效
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(orderId, Constant.NOT_DELTED);
		if (null == order)
			return -1;// 订单id无效
		ConfigPayStatus configPayStatus = order.getConfigPayStatus();
		if (null == configPayStatus)
			return -2;// 订单异常，保存时没有设置支付状态
		if (configPayStatus.getId().equals(OrderPayStatus.PAID.getValue()))
			return -3; // 该订单已支付
		
		//改签
		Double alterPrice = null;
		String alterOrderId = null;
		if (order.getAlterPrice() != null && order.getAlterOrderId() !=null) {
			alterPrice = order.getAlterPrice();
			alterOrderId = order.getAlterOrderId();
			
			PassengerBusOrder oldOrder = passengerBusOrderDao.findById(order.getAlterOrderId());
			if(oldOrder!=null && oldOrder.getCouponSn()!=null){
				couponSnService.refundCouponOnce(oldOrder.getCouponSn());//退回旧订单代金券，改签成功立刻退回代金券
			}
		}
		// passengerBusOrderDao.setPaidByOrderId(order.getId());
		//Double.doubleToLongBits(alterPrice)!=Double.doubleToLongBits(0)
		if(alterOrderId!=null && alterPrice!=null) {//改签多次改回来的订单在前面就被过滤，不需要再判断
			//新订单->已支付
			passengerBusOrderDao.setPayStatusAndPayDateByOrderId(order.getId(), OrderPayStatus.PAID.getValue(), new Date());
			//旧订单->改签完成
			passengerBusOrderDao.setPayStatusByOrderId(order.getAlterOrderId(), OrderPayStatus.COMPLETE_CHANGED.getValue());
		}else {
			passengerBusOrderDao.setPayStatusAndPayDateByOrderId(order.getId(), OrderPayStatus.PAID.getValue(), new Date());
		}
		// order = passengerBusOrderDao.findByIdAndDelFlag(orderId,
		// Constant.NOT_DELTED);
		// if (null == order ||
		// !order.getConfigPayStatus().getId().equals(OrderPayStatus.PAID.getValue()))
		// return -4; //修改支付状态失败
		return 0;// 成功设为已支付
	}
	
	@Transactional
	@Override
	public int setUnPaid(String orderId) {
		if (null == orderId || orderId.isEmpty())
			return -1;// 订单id无效
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(orderId, Constant.NOT_DELTED);
		if (null == order)
			return -1;// 订单id无效
		ConfigPayStatus configPayStatus = order.getConfigPayStatus();
		if (null == configPayStatus)
			return -2;// 订单异常，保存时没有设置支付状态
		if (configPayStatus.getId().equals(OrderPayStatus.UNPAID.getValue()))
			return -3; // 该订单已是未支付状态
		// passengerBusOrderDao.setUnPaidByOrderId(order.getId());
		passengerBusOrderDao.setPayStatusByOrderId(order.getId(), OrderPayStatus.UNPAID.getValue());
		// order = passengerBusOrderDao.findByIdAndDelFlag(orderId,
		// Constant.NOT_DELTED);
		// if (null == order ||
		// !order.getConfigPayStatus().getId().equals(OrderPayStatus.UNPAID.getValue()))
		// return -4; //修改支付状态失败
		return 0;// 成功设为已支付
	}

	@Override
	public OrderJson getOrderJsonByOrderId(String id) {
		if (null == id || id.isEmpty())
			return null;
		PassengerBusOrder order = passengerBusOrderDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
		order = svCheckService.setCheckPhone(order);
		if (null == order)
			return null;
		return new OrderJson(order);
	}

	@Override
	public List<OrderJson> getUserOrdersJsonByUserId(String userId) {
		List<OrderJson> ordersJson = new ArrayList<>();
		if (null == userId || userId.isEmpty())
			return ordersJson;
		List<PassengerBusOrder> orders = this.getOrdersByUserId(userId);
		orders.forEach(order -> {
			ordersJson.add(new OrderJson(order));
		});
		return ordersJson;
	}

	private List<PassengerBusOrder> getOrdersByUserId(String userId) {
		return this.passengerBusOrderDao.findByBuyerId(userId);
	}

	// 未完成
	// 该方法主要为了生成四类状态的订单
	// 1 未支付，待支付
	// 2 已支付，未验票
	// 3 已支付，已验票
	// 4 已取消/过期
	// 这里参宿需要加上日期判断，
	@Override
	public List<OrderJson> getUserOrdersJsonByUserIdAndStatus(String userId, String status) {
		List<OrderJson> ordersJson = new ArrayList<>();
		if (null == userId || userId.isEmpty())
			return ordersJson;

		if (!"toBePaid".equals(status) && !"toBeGone".equals(status) && !"gone".equals(status)
				&& !"afterSale".equals(status)  && !"changeSign".equals(status)) {
			return ordersJson;
		}
		List<PassengerBusOrder> orders = new ArrayList<PassengerBusOrder>();
		// 未支付
		if ("toBePaid".equals(status)) {
			Date now = new Date();
			Date validCreateDate = new Date(now.getTime() - PassengerBusOrderService.NOT_PAID_ORDER_VAILD_SECONDS * 1000);
			orders = this.passengerBusOrderDao
					.findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idAndBusSchedule_DepartureTimeAfterAndCreateDateBetweenOrderByCreateDateDesc(
							userId, OrderPayStatus.UNPAID.getValue(), OrderCheckStatus.UNCHECK.getValue(), now, validCreateDate, now);

		}
		// 已支付，待出行
		if ("toBeGone".equals(status)) {
			// orders =
			// this.passengerBusOrderDao.findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idAndBusSchedule_DepartureTimeAfterOrderByCreateDateDesc
			// (userId, OrderPayStatus.PAID.getValue(),
			// OrderCheckStatus.UNCHECK.getValue(), new Date());
			orders = this.passengerBusOrderDao
					.findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idOrderByCreateDateDesc(userId,
							OrderPayStatus.PAID.getValue(), OrderCheckStatus.UNCHECK.getValue());
		}
		// 已出行
		if ("gone".equals(status)) {
			orders = this.passengerBusOrderDao
					.findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idOrderByCreateDateDesc(userId,
							OrderPayStatus.PAID.getValue(), OrderCheckStatus.CHECK.getValue());
		}
		// 售后，包括退款申请提交，退款申请完成
		if ("afterSale".equals(status)) {
			List<String> payStatusList = new ArrayList<String>();
			payStatusList.add(OrderPayStatus.REFUND_SUBMIT.getValue());
			payStatusList.add(OrderPayStatus.REFUND_COMPLETE.getValue());
			orders = this.passengerBusOrderDao.findByBuyerIdAndConfigPayStatus_IdInOrderByUpdateDateDesc(userId,
					payStatusList);
		}
		if ("changeSign".equals(status)) {
			List<String> payStatusList = new ArrayList<String>();
			payStatusList.add(OrderPayStatus.COMPLETE_CHANGED.getValue());
			payStatusList.add(OrderPayStatus.SIGN_CHANGING.getValue());
			payStatusList.add(OrderPayStatus.CHANGE_UNPAID.getValue());
			payStatusList.add(OrderPayStatus.CHANGE_REFUND.getValue());
			payStatusList.add(OrderPayStatus.CHANGE_CANCEL.getValue());
			orders = this.passengerBusOrderDao.findByBuyerIdAndConfigPayStatus_IdInOrderByUpdateDateDesc(userId,
					payStatusList);
		}
		orders.forEach(order -> {
			ordersJson.add(new OrderJson(order));
			if ("toBePaid".equals(status)) {
//				ordersJson.setRemainSeatNum(...);
			}
		});
		return ordersJson;
	}

	@Override
	public List<ConfigRefundJson> getAllRefundInfo() {
		List<ConfigRefundJson> configRefundJsons = new ArrayList<>();
		configRefundDao.findAll().forEach(refund -> {
			configRefundJsons.add(new ConfigRefundJson(refund));
		});

		return configRefundJsons;
	}

	//平台订票微信支付的数量：
	//总订单
	@Override
	public Integer getOrderNumByScheduleId(String busScheduleId, String scheduleVehicleId) {
		return passengerBusOrderDao.getOrderNumByScheduleId(OrderPayStatus.PAID.getValue(),PayMethod.WEIXIN.getValue(), busScheduleId,
				scheduleVehicleId);
	}
	@Override
	public Integer getOrderNumByScheduleVehicleId(String scheduleVehicleId) {
		return passengerBusOrderDao.getOrderNumByScheduleVehicleId(OrderPayStatus.PAID.getValue(),scheduleVehicleId);
	}
	//平台订票微信支付的数量：
	//已验总订单
	@Override
	public Integer getOrderNumByScheduleIdAndCheckStatus(String busScheduleId, String checkStatus,
			String scheduleVehicleId) {
		return passengerBusOrderDao.getOrderNumByScheduleIdAndCheckStatus(OrderPayStatus.PAID.getValue(),PayMethod.WEIXIN.getValue(), busScheduleId,
				checkStatus, scheduleVehicleId);
	}
	//平台订票微信支付的数量：
	//总票数
	@Override
	public Integer getOrderTicketSumByScheduleId(String busScheduleId, String scheduleVehicleId) {
		return passengerBusOrderDao.getOrderTicketSumByScheduleId(OrderPayStatus.PAID.getValue(),PayMethod.WEIXIN.getValue(), busScheduleId,
				scheduleVehicleId);
	}
	//平台订票微信支付的数量：
	//已验总票数
	@Override
	public Integer getOrderTicketSumByScheduleIdAndCheckStatus(String busScheduleId, String checkStatus,
			String scheduleVehicleId) {
		return passengerBusOrderDao.getOrderTicketSumByScheduleIdAndCheckStatus(OrderPayStatus.PAID.getValue(),PayMethod.WEIXIN.getValue(),
				busScheduleId, checkStatus, scheduleVehicleId);
	}

	@Override
	public PassengerBusOrder saveQRCodePayOrder(JsonObj orderParams) {
		return saveTypeAddCoType(orderParams, OrderPayStatus.UNPAID, OrderCheckStatus.UNCHECK, PayMethod.QRCODE);
	}

	@Override
	public PassengerBusOrder saveCashOrder(JsonObj orderParams) {
		return saveTypeAddCoType(orderParams, OrderPayStatus.PAID, OrderCheckStatus.CHECK, PayMethod.CASH);
	}

	@Override
	public PassengerBusOrder saveTickeOrder(JsonObj orderParams) {
		return saveType(orderParams, OrderPayStatus.PAID, OrderCheckStatus.CHECK, PayMethod.TICKET);
	}

	private PassengerBusOrder saveType(JsonObj orderParams, OrderPayStatus payStatus, OrderCheckStatus checkStatus,
			PayMethod payMethod) {
		String busScheduleId = (String) orderParams.getProperty("busScheduleId");
		String schedule_vehicle_id = (String) orderParams.getProperty("scheduleVehicleId");
		int amount = (int) orderParams.getProperty("amount");
		String create_by = (String) orderParams.getProperty("createBy");
		// String originalStationCoachNumberId =
		// (String)orderParams.getProperty("originalStationCoachNumberId");
		// String terminalStationCoachNumberId =
		// (String)orderParams.getProperty("terminalStationCoachNumberId");
//		double total = (double) orderParams.getProperty("total");
		
		Object t  =orderParams.getProperty("total");
		Double total = 0.0;
		if(t instanceof Integer){//判断类型
			total= (Integer) orderParams.getProperty("total")+0.0;
		}else{
			 total = (Double) orderParams.getProperty("total");
		}
		
		// String buyerId = "12345678987654321";
		String service = (String) orderParams.getProperty("service");
		String remarks = (String) orderParams.getProperty("remarks");
		return doSave(busScheduleId, schedule_vehicle_id, total, payStatus, checkStatus, payMethod, amount, service,
				remarks,create_by);
	}

	private PassengerBusOrder saveTypeAddCoType(JsonObj orderParams, OrderPayStatus payStatus, OrderCheckStatus checkStatus,
			PayMethod payMethod) {
		String busScheduleId = (String) orderParams.getProperty("busScheduleId");
		String schedule_vehicle_id = (String) orderParams.getProperty("scheduleVehicleId");
		int amount = (int) orderParams.getProperty("amount");
		String create_by = (String) orderParams.getProperty("createBy");
		String co_type = (String) orderParams.getProperty("co_type");
		// String originalStationCoachNumberId =
		// (String)orderParams.getProperty("originalStationCoachNumberId");
		// String terminalStationCoachNumberId =
		// (String)orderParams.getProperty("terminalStationCoachNumberId");
//		double total = (double) orderParams.getProperty("total");
		
		Object t  =orderParams.getProperty("total");
		Double total = 0.0;
		if(t instanceof Integer){//判断类型
			total= (Integer) orderParams.getProperty("total")+0.0;
		}else{
			 total = (Double) orderParams.getProperty("total");
		}
		
		// String buyerId = "12345678987654321";
		String service = (String) orderParams.getProperty("service");
		String remarks = (String) orderParams.getProperty("remarks");
		return doSaveAddCoType(busScheduleId, schedule_vehicle_id, total, payStatus, checkStatus, payMethod, amount, service,
				remarks,create_by,co_type);
	}
	
	private PassengerBusOrder doSave(String busScheduleId, String schedule_vehicle_id, double total,
			OrderPayStatus payStatus, OrderCheckStatus checkStatus, PayMethod payMethod, int amount, String service,
			String remarks,String create_by) {

		BusSchedule busSchedule = busScheduleService.getBusScheduleById(busScheduleId);
		ScheduleVehicle scheduleVehicle = scheduleVehicleService.getScheduleVehicleById(schedule_vehicle_id);
		// PersonalAccount buyer =
		// personalAccountService.getAccountById(buyerId);
		// StationCoachNumber oriScn =
		// stationCoachNumberService.getStationCoachNumberById(originalStationCoachNumberId);
		// StationCoachNumber terScn =
		// stationCoachNumberService.getStationCoachNumberById(terminalStationCoachNumberId);
		// if (null == busSchedule || null == scheduleVehicle || null == buyer
		// ||
		// null == oriScn || null == terScn || null == service) {
		// logger.debug(String.format("busSchedule: %s, scheduleVehicle: %s, "
		// + "buyer: %s, oriScn: %s, terScn: %s", null == busSchedule,
		// null == scheduleVehicle, null == buyer,
		// null == oriScn, null == terScn));
		// return null;
		// }
		if (null == busSchedule || null == scheduleVehicle) {

			return null;
		}
		PassengerBusOrder order = new PassengerBusOrder();
		// order.setBuyer(buyer);
		order.setId(RandomUtil.getId());
		order.setBusSchedule(busSchedule);
		order.setScheduleVehicle(scheduleVehicle);
		order.setTotal((double) total);
		order.setAmount(amount);
		order.setCode("OD" + TimeUtil.getSecondTimeStamp());
		// order.setOriginalStationCoach(oriScn);
		// order.setTerminalStationCoach(terScn);
		order.setService(service);
		order.setRemarks(remarks);
		order.setCreateBy(create_by);
		// 设置支付状态
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		configPayStatus.setId(payStatus.getValue());
		configPayStatus.setName(payStatus.getName());
		order.setConfigPayStatus(configPayStatus);
		// 设置验票状态
		ConfigCheckStatus configCheckStatus = new ConfigCheckStatus();
		configCheckStatus.setId(checkStatus.getValue());
		configCheckStatus.setName(checkStatus.getName());
		order.setConfigCheckStatus(configCheckStatus);
		// passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		Date now = new Date();
		order.setCreateDate(now);
		order.setOriginalDate(now);
		// 设置支付方式
		ConfigPayMethod configPayMethod = new ConfigPayMethod();
		configPayMethod.setId(payMethod.getValue());
		order.setConfigPayMethod(configPayMethod);
		// 设置逻辑删除字段
		order.setDelFlag(Constant.NOT_DELTED);
		return this.passengerBusOrderDao.save(order);
//		synchronized (this) {
//			Long remainTicketsAmount = remainTicketsAmount(scheduleVehicle.getId());
//			if (remainTicketsAmount - order.getAmount() > 0) {
//				logger.info("保存");
//				return this.passengerBusOrderDao.save(order);
//			} else {
//				logger.info("请求保存订单失败，订单包含票数: " + order.getAmount() + ",实际剩余车票:" + remainTicketsAmount);
//				return null;
//			}
//		}
	}

	private PassengerBusOrder doSaveAddCoType(String busScheduleId, String schedule_vehicle_id, double total,
			OrderPayStatus payStatus, OrderCheckStatus checkStatus, PayMethod payMethod, int amount, String service,
			String remarks,String create_by,String co_type) {

		BusSchedule busSchedule = busScheduleService.getBusScheduleById(busScheduleId);
		ScheduleVehicle scheduleVehicle = scheduleVehicleService.getScheduleVehicleById(schedule_vehicle_id);
		
		if (null == busSchedule || null == scheduleVehicle) {

			return null;
		}
		PassengerBusOrder order = new PassengerBusOrder();
		// order.setBuyer(buyer);
		order.setId(RandomUtil.getId());
		order.setBusSchedule(busSchedule);
		order.setScheduleVehicle(scheduleVehicle);
		order.setTotal((double) total);
		order.setAmount(amount);
		order.setCode("OD" + TimeUtil.getSecondTimeStamp());
		// order.setOriginalStationCoach(oriScn);
		// order.setTerminalStationCoach(terScn);
		order.setService(service);
		order.setRemarks(remarks);
		order.setCreateBy(create_by);
		order.setCoType(co_type);
		// 设置支付状态
		ConfigPayStatus configPayStatus = new ConfigPayStatus();
		configPayStatus.setId(payStatus.getValue());
		configPayStatus.setName(payStatus.getName());
		order.setConfigPayStatus(configPayStatus);
		// 设置验票状态
		ConfigCheckStatus configCheckStatus = new ConfigCheckStatus();
		configCheckStatus.setId(checkStatus.getValue());
		configCheckStatus.setName(checkStatus.getName());
		order.setConfigCheckStatus(configCheckStatus);
		// passengerBusOrder.setDelFlag(Constant.NOT_DELTED);
		Date now = new Date();
		order.setCreateDate(now);
		order.setOriginalDate(now);
		// 设置支付方式
		ConfigPayMethod configPayMethod = new ConfigPayMethod();
		configPayMethod.setId(payMethod.getValue());
		order.setConfigPayMethod(configPayMethod);
		// 设置逻辑删除字段
		order.setDelFlag(Constant.NOT_DELTED);
		return this.passengerBusOrderDao.save(order);
	}

	@Override
	public JsonObj refundMoney(String orderCode) throws SQLException, JsonProcessingException,IOException {
		/*
		 * 对应personal-web下order.yaml中id为refundQuery的sql
		 */
		String result= generalService.select("refundQuery", JsonObj.instance().putProperty("code", orderCode));
		JsonObj jo = new JsonObj(result);
		ArrayList rowDatas = (ArrayList) jo.getProperty("refundQuery");
		if (rowDatas.isEmpty()) {
			return null;
		}
		String res  = (String) rowDatas.get(0);
		
		jo = new JsonObj(res);
		if(jo.getProperty("openid")==null || jo.getProperty("total")==null || jo.getProperty("original_date")==null ){
			return null;
		}
		String openid = (String) jo.getProperty("openid");
		double dbTotal = ((double)jo.getProperty("total"));//数据库中钱单位为元
		Long departureDate = (Long)jo.getProperty("original_date");
		Date now = new Date();
		double percent;
		long remainTime = (departureDate - now.getTime()) / 1000/3600;//秒级/小时级
		if (remainTime <= 0) {//该情况不退款，改成人工退款
			return null;
		}

		List<ConfigRefundJson> allrefundJsonList= getAllRefundInfo();
		int index=-1;
		for(int i=0; i<allrefundJsonList.size();i++){
			allrefundJsonList.get(i);
			double thistimelimit= allrefundJsonList.get(i).getTimeLimit();
			if(remainTime>thistimelimit){
				if( index ==-1 ){
					index=i;
				}else if(thistimelimit>allrefundJsonList.get(index).getTimeLimit()){
					index=i;
				}
			}
		}
		if (index==-1) {//该情况不退款，改成人工退款
			return null;
		}
		percent=allrefundJsonList.get(index).getPercent();
		
		//业务逻辑上判断不满足退款，及业务逻辑上的退款比例
		Integer total = (int) (100 * dbTotal);//微信钱单位为分
		Integer refundMoney = (int) (total * percent) == 0 ? 1 : (int) (total * percent); //实际退款钱数必须大于0，否则请求会失败
		return JsonObj.instance()
				.putProperty("total", total)
				.putProperty("refundMoney", refundMoney)
				.putProperty("openid", openid);
	}
	
//	@Override
	@Deprecated
	public JsonObj refundMoney_old(String orderCode) throws SQLException {
		/*
		 * 对应personal-web下order.yaml中id为refundQuery的sql
		 */
		SQLGroupResult sqlGroupResult = generalService.selectResult("refundQuery", JsonObj.instance().putProperty("code", orderCode));
		List<Map<String, Object>> rowDatas = sqlGroupResult.getSQLResult("refundQuery");
		if (rowDatas.isEmpty()) {//查询结果无数据
			return null;
		} 
		Map<String, Object> rowData = rowDatas.get(0);
		double dbTotal = ((BigDecimal)rowData.get("total")).doubleValue();//数据库中钱单位为元
		String openid = (String) rowData.get("openid");
		Date departureDate = (Date) rowData.get("original_date");
		Date now = new Date();
		double percent;
		long remainTime = (departureDate.getTime() - now.getTime()) / 1000;//秒级
		if (remainTime <= 0) {//不满足退款条件
			return null;
		} else if (remainTime <= 3600 * 3) { //三小时
			percent = 0.3;
		} else if (remainTime <= 3600 * 24 ) {//一天
			percent = 0.8;
		} else {
			percent = 1.0;
		}
		
		//业务逻辑上判断不满足退款，及业务逻辑上的退款比例
		Integer total = (int) (100 * dbTotal);//微信钱单位为分
		Integer refundMoney = (int) (total * percent) == 0 ? 1 : (int) (total * percent); //实际退款钱数必须大于0，否则请求会失败
		return JsonObj.instance()
				.putProperty("total", total)
				.putProperty("refundMoney", refundMoney)
				.putProperty("openid", openid);
	}

	@Override
	public void refundOrder(String orderCode,Double refundMoney) throws SQLException {
		passengerBusOrderDao.setPayStatusRefundAndAlterPriceByOrderCode(orderCode,refundMoney,OrderPayStatus.REFUND_COMPLETE.getValue(),new Date());
//		generalService.update("refundOrder", JsonObj.instance().putProperty("code", orderCode));
	}
	
//	@Override
	@Deprecated
	public void refundOrder_old(String orderCode) throws SQLException {
		//修改支付状态，并且处理优惠券
		/*
		 * 对应personal-web下order.yaml中id为refundOrder的sql
		 */
		generalService.update("refundOrder", JsonObj.instance().putProperty("code", orderCode));
	}
	
	/*
		验票后操作
		1. 判断是否用户升级 
		2. 更新用户注册时送的代金券,remark设为remaintimes,remaintimes设为0
		3. 微信通知升级成功
	 */
	@Override
	public void afterOrderChecked(String orderId) throws SQLException {
		/*
		 *对应order.yaml中id为forSendCheckOrderSuccess的sql 
		 */
//		asynchronousService.sumbitRunnableTask(() -> {
		
			logger.info("验票后操作开始");
			String result;
			try {//下面是获取用户id,订单code,订单总价total,订单票数amount,openid 
				result = generalService.select("forSendCheckOrderSuccess", JsonObj.instance().putProperty("id", orderId));
				JsonObj jo = new JsonObj(result);
				List<String> rowDatas = (List<String>) jo.getProperty("orderDetail");
				if (rowDatas.isEmpty()) {
					return;
				}
				String res  = rowDatas.get(0);
				jo = new JsonObj(res);
				String id = (String) jo.getProperty("id");
				String openid = (String) jo.getProperty("openid");
				Double total = (Double) jo.getProperty("total");
				//增加该用户积分
				
				
				if (null != openid) {
					//验票成功后在公众号给该用户发送微信提示
//					String code = (String) jo.getProperty("code");
//					int amount = (Integer) jo.getProperty("amount");
//					double total = ((double) jo.getProperty("total"));
//				wxms.sendCheckOrderSuccessMessage(openid, code, total, amount);
				}
				//判断账号是否升级并升级
				String upgradeIndex = "-1";
				PersonalAccount pa= personalAccountDao.findById(id);
				if(pa==null){
					return ;
				}
				pa.setPoints(pa.getPoints()+total);
				//personalAccountService.update(pa);
				if(pa.getMemberLevel()==null){
					return;
				}
				MemberLevel thisPaMl= memberLevelDao.findById(pa.getMemberLevel().getId());
				int nextLevel = Integer.valueOf(pa.getMemberLevel().getId())+1;
				if(thisPaMl!=null &&  thisPaMl.getUpgradeCredit()>0 && 
						memberLevelDao.findById(nextLevel+"")!=null && pa.getPoints()>=thisPaMl.getUpgradeCredit() ){
					MemberLevel thisPaMl2= new MemberLevel();
					thisPaMl2.setId(nextLevel+"");
					pa.setMemberLevel(thisPaMl2);
					upgradeIndex="1";
				}
				personalAccountService.update(pa);
				
//				String upgradeIndex=personalAccountService.iFmemberOneUpgrade(id);//用户升级在里面完成
				if (!"-1".equals(upgradeIndex)) {
//					updateSignInCouponsn(id);//更新用户注册时送的代金券,remark设为remaintimes,remaintimes设为0，旧方法
					couponSnService.setBuyerSignInCouponSnZero(id);//更新用户注册时送的代金券,remark设为remaintimes,remaintimes设为0
					if (null != openid) {
						String account_level = (String) jo.getProperty("account_level");
//						String accountLevel = "'"+ConfigEnum.getAccountLevelStr(account_level)+"'-升至->'"+ConfigEnum.getAccountLevelStr(nextLevel+"")+"'";
						String accountLevel = ConfigEnum.getAccountLevelStr(nextLevel+"");
						//在公众号给该用户发送升级完成的提示
						wxms.sendAccountIncreaseLevelMessage(openid, accountLevel);
						logger.info("该用户升级成功");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		});
	}
	
	@Deprecated
//	@Override
	public void afterOrderChecked_old(String orderId) throws SQLException {
		/*
		 *对应order.yaml中id为forSendCheckOrderSuccess的sql 
		 */
		asynchronousService.sumbitRunnableTask(() -> {
			SQLGroupResult result;
			try {
				result = generalService.selectResult("forSendCheckOrderSuccess", JsonObj.instance().putProperty("id", orderId));
				List<Map<String, Object>> rowDatas = result.getSQLResult("orderDetail");
				if (rowDatas.isEmpty()) {
					return;
				}
				Map<String, Object> rowData = rowDatas.get(0);
				String id = (String) rowData.get("id");
				String code = (String) rowData.get("code");
				int amount = (Integer) rowData.get("amount");
				double total = ((BigDecimal) rowData.get("total")).doubleValue();
				String openid = (String) rowData.get("openid");
				if (null != openid) {
					//验票成功后在公众号给该用户发送微信提示
//				wxms.sendCheckOrderSuccessMessage(openid, code, total, amount);
				}
				//判断账号是否升级并升级
				String upgradeIndex=personalAccountService.iFmemberOneUpgrade(id);
				if (!"-1".equals(upgradeIndex)) {
					//更新用户注册时送的代金券,remark设为remaintimes,remaintimes设为0
					updateSignInCouponsn(id);
					if (null != openid) {
						String account_level = (String) rowData.get("account_level");
						String accountLevel = "'"+ConfigEnum.getAccountLevelStr(account_level)+"'-升至->'"+ConfigEnum.getAccountLevelStr(upgradeIndex)+"'";
						//在公众号给该用户发送升级完成的提示
						wxms.sendAccountIncreaseLevelMessage(openid, accountLevel);
						logger.info("该用户升级成功");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public void updateSignInCouponsn(String personalAccountId) {
		//对应order.yaml中selectSignInCouponsn和updateSignInCouponsn
		try {
			couponSnService.setBuyerSignInCouponSnZero(personalAccountId);
			logger.info("将id为： " + personalAccountId + "的用户注册优惠券remark设为remainTimes,remainTimes设为零");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	@Deprecated
	public void updateSignInCouponsn_old(String personalAccountId) {
		//对应order.yaml中selectSignInCouponsn和updateSignInCouponsn
		try {
			//根据用户id查找注册代金券id及remain_times
			SQLGroupResult res = generalService.selectResult("selectSignInCouponsn", JsonObj.instance().putProperty("personal_account_id", personalAccountId));
			List<Map<String, Object>> signInCouponsnList = res.getSQLResult("selectSignInCouponsn");
			if (signInCouponsnList.isEmpty()) {
				logger.warn("id为: " + personalAccountId + "的用户不存在注册优惠券");
				return;
			}
			logger.info("id为" + personalAccountId + "的用户拥有" + signInCouponsnList.size() + "注册代金券");
			JsonObj args = JsonObj.instance().putProperty("updateSignInCouponsn", signInCouponsnList);
			//更新每条注册代金券
			generalService.batchUpdate("updateSignInCouponsn", args);
			logger.info("将id为： " + personalAccountId + "的用户注册优惠券remark设为remainTimes,remainTimes设为零");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	
	@Transactional
	@Override
	public void changeOrderPayStatusByCode(String orderCode, OrderPayStatus payStatus) {
//		passengerBusOrderDao.setPayStatusByOrderCode(orderCode, payStatus.getValue());
		passengerBusOrderDao.setPayStatusAndRefundSubmitDateByOrderCode(orderCode, payStatus.getValue(),new Date());
		
	}

	@Override
	public boolean enableWXAutoRefund() {
		//对应order.yaml中id为configRefund的sql
		try {
			String result = generalService.select("configRefund", JsonObj.instance());
			JsonObj jo = new JsonObj(result);
			List<String> rowDatas = (List<String>) jo.getProperty("configRefund");
			if (rowDatas.isEmpty()) {
				return false;
			}
			String value  =  (String)new JsonObj(rowDatas.get(0)).getProperty("value");
			if ("yes".equalsIgnoreCase(value)) {
				logger.info("启用微信自动退款（调用api)");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
//	@Override
	@Deprecated
	public boolean enableWXAutoRefund_old() {
		//对应order.yaml中id为configRefund的sql
		try {
			SQLGroupResult sqlGroupResult = generalService.selectResult("configRefund", JsonObj.instance());
			List<Map<String, Object>> resList = sqlGroupResult.getSQLResult("configRefund");
			if (resList.isEmpty()) {
				return false;
			}
			String value = (String) resList.get(0).get("value");
			if ("yes".equalsIgnoreCase(value)) {
				logger.info("启用微信自动退款（调用api)");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Long remainTicketsAmount(String scheduleVehicleId) {
		//对应order.yaml中id为remainTickets的sql
		JsonObj args = JsonObj.instance().putProperty("scheduleVehicleId", scheduleVehicleId)
			.putProperty("validSeconds", NOT_PAID_ORDER_VAILD_SECONDS);
		String groupRes;
		try {
			groupRes = generalService.select("remainTickets", args);
			JsonObj jo = new JsonObj(groupRes);
			Object obj = jo.getProperty("remainTickets");
			List<String> a = (List<String>) obj;
			String res = a.get(0);
			jo = new JsonObj(res);
			int remainNum = (Integer)jo.getProperty("remain_num");
			return Long.valueOf(remainNum);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1L;
	}
	
	@Deprecated
	public Long remainTicketsAmount_old(String scheduleVehicleId) {
		//对应order.yaml中id为remainTickets的sql
		JsonObj args = JsonObj.instance().putProperty("scheduleVehicleId", scheduleVehicleId)
			.putProperty("validSeconds", NOT_PAID_ORDER_VAILD_SECONDS);
		SQLGroupResult groupRes;
		try {
			groupRes = generalService.selectResult("remainTickets", args);
			return ((BigDecimal) groupRes.getSQLResult("remainTickets").get(0).get("remain_num")).longValue();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1L;
	}
	
//	@Override
	public Long remainTicketsAmount_2(String scheduleVehicleId, String index) {
		EntityManager em = emf.createEntityManager();
		String sql="";
		if("geBusScheduletRemainSeats".equals(index)){
			sql=Constant.busScheduletRemainSeats;
			Query q = em.createNativeQuery(sql);
		}else if("getScheduleVehicleRemainSeats".equals(index)){
			sql=Constant.scheduleVehicleRemainSeats;
			Query q = em.createNativeQuery(sql);
		}else{
			sql=Constant.scheduleVehicleRemainSeats2;
			Query q = em.createNativeQuery(sql);
		}
		return null;
	}

	@Override
	public Integer getOrderNum(String scheduleVehicleId) {
		return passengerBusOrderDao.getNumByScheduleVehicleId(scheduleVehicleId);
	}



	
//	private JsonObj ticketsAmount(String scheduleVehicleId) {
//		JsonObj res = JsonObj.instance();
//		//对应order.yaml中id为scheduleVehicleId的sql
//		JsonObj args = JsonObj.instance().putProperty("scheduleVehicleId", scheduleVehicleId)
//			.putProperty("validSeconds", NOT_PAID_ORDER_VAILD_SECONDS);
//		SQLGroupResult groupRes;
//		try {
//			groupRes = generalService.selectResult("getRemainTickets", args);
//			long seatNum = (Long) groupRes.getSQLResult("seat_num").get(0).get("seat_num");
//			long paidNum = ((BigDecimal) groupRes.getSQLResult("paid_num").get(0).get("paid_num")).longValue();
//			long canPayNum = ((BigDecimal) groupRes.getSQLResult("can_pay_num").get(0).get("can_pay_num")).longValue();
//			long presellNum = ((BigDecimal) groupRes.getSQLResult("presell_num").get(0).get("presell_num")).longValue();
//			long remainTickets = seatNum - paidNum - canPayNum - presellNum;
//			res.putProperty("seatNum", seatNum)
//				.putProperty("paidNum", paidNum)
//				.putProperty("canPayNum", canPayNum)
//				.putProperty("presellNum", presellNum)
//				.putProperty("remainTickets", remainTickets);
//			logger.info(String.format("%s班次车辆座位数为:%d,已支付票数%d,尚在有效支付期内票数%d,"
//					+ "预售票数%d，余票%d", scheduleVehicleId, seatNum, paidNum, canPayNum, presellNum, remainTickets));
//			return res;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
	
}
