package tech.xiangcheng.orangebus.order.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.config.domain.ConfigRefundJson;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.order.domain.OrderJson;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.order.domain.enumType.OrderPayStatus;

public interface PassengerBusOrderService {
	
	
	JsonObj getOrderStatisticMessagee(String scheduleVehicleId);
	/**
	 * 返回null代表保存失败
	 * @param passengerBusOrder
	 * @return
	 */
	PassengerBusOrder save(PassengerBusOrder passengerBusOrder,PassengerBusOrder oldOrder);
	/**
	 * 返回null代表保存失败
	 * @param passengerBusOrder
	 * @return
	 */
	PassengerBusOrder save(PassengerBusOrder passengerBusOrder);
	/**
	 * @param buyerId TODO
	 * @param busScheduleId
	 * @param schedule_vehicle_id
	 * @param total
	 * @return
	 */
	PassengerBusOrder save_bk(String buyerId, String busScheduleId, String schedule_vehicle_id, int total);
	
	/**
	 * 保存用于乘务员二维码支付的订单
	 * @param orderParams
	 * @return
	 */
	PassengerBusOrder saveQRCodePayOrder(JsonObj orderParams);
	/**
	 * 保存乘务员收取现金的订单
	 * @param orderParams
	 * @return
	 */
	PassengerBusOrder saveCashOrder(JsonObj orderParams);
	
	/**
	 * 保存乘务员收取的票据订单
	 * @param orderParams
	 * @return
	 */
	PassengerBusOrder saveTickeOrder(JsonObj orderParams);
	
	/**
	 * 
	 * @param id
	 * @return 若无相应有效订单则返回null
	 */
	PassengerBusOrder getOrderById(String id);
	/**
	 * 
	 * @param code
	 * @return
	 */
	PassengerBusOrder getOrderByCode(String code);
	/**
	 * 更改id订单的支付状态
	 * @param statusToBeChanged 
	 * @param id
	 * @return
	 */
	boolean changePayStatus(String statusToBeChanged, String id);
	/**
	 * 取消改签
	 * @param statusToBeChanged
	 * @param id
	 * @return
	 */
	boolean cancelChangeSign(String statusToBeChanged, String id);
	/**
	 * 取消order支付状态
	 * @param statusToBeChanged 
	 * @param id
	 * @return
	 */
	boolean changeOrderStatus(String statusToBeChanged, String id);
	
	/**
	 * 返回某班次已支付订单
	 * @param id
	 * @return
	 */
	List<PassengerBusOrder> getPaidOrderByBusScheduleId(String id);
	/**
	 * 将订单设为已检票
	 * @param id
	 * @return 0 成功检票；-1 无相应订单；-2订单尚未支付，不能设为已检票； -3 该订单已经检票
	 */
	int setOrderChecked(String id);
	/**
	 * 将订单设为已检票
	 * @param code
	 * @return 0 成功检票；-1 无相应订单；-2订单尚未支付，不能设为已检票； -3 该订单已经检票
	 */
	int setOrderCheckedByCode(String code);
	/**
	 * 将订单设为未检票
	 * @param id
	 * @return 0 成功；-1 无相应订单；
	 */
	int setOrderUnChecked(String id);
	/**
	 * 设置订单检票状态
	 * @param id
	 * @param status 只能为0和1，0未检，1已检
	 * @return 0 成功；-1 无相应订单；-2 订单尚未支付，不能设为已检票；-3 status无效，status只能为0或1； -4 该订单已经检票，不能重复检票
	 */
	String changeOrderCheckStatus(String id, String status,String checker);
	
	/**
	 * 返回某账号所有订单总额
	 * @param userId
	 * @return userId无效时返回null
	 */
	Double getSumSpentByUserId(String userId);
	
	/**
	 * 根据订单id将订单设为已支付
	 * @param orderId
	 * @return 0 成功； -1 订单id无效；-2 订单异常，保存时未设置支付状态; -3 该订单已支付； 
	 */
	int setPaid(String orderId);
	/**
	 * 根据订单id将订单设为未支付
	 * @param orderId
	 * @return 0 成功； -1 订单id无效；-2 订单异常，保存时未设置支付状态; -3 该订单已经为未支付；
	 */
	int setUnPaid(String orderId);
	
	/**
	 * 返回订单详细信息
	 * @param id
	 * @return 返回null代表订单不存在
	 */
	OrderJson getOrderJsonByOrderId(String id);

	/**
	 * 根据用户id返回其所有订单详情
	 * @param userId
	 * @return
	 */
	List<OrderJson> getUserOrdersJsonByUserId(String userId);
	List<PassengerBusOrder> findByBuyer_NameOrderByCreateDateDesc(String username);
	List<PassengerBusOrder> findByBuyer_idOrderByCreateDateDesc(String buyerId);
//	Double getSumSpentByUserId1(String userId);
	@Deprecated
	/**
	 * 该方法已废弃，请调用setPaid方法
	 * @param orderId
	 * @return
	 */
	boolean setOrderPaid(String orderId);
	List<OrderJson> getUserOrdersJsonByUserIdAndStatus(String userId, String status);
	List<PassengerBusOrder> getPaidOrderByBusScheduleIdCheckStatus(String id, String checkStatus);
	List<PassengerBusOrder> getPaidOrderByBusScheduleIdCheckStatusAndScheduleVehicleId(String id, String checkStatus,
			String scheduleVehicleId);
	List<ConfigRefundJson> getAllRefundInfo();
	Integer getOrderNumByScheduleId(String busScheduleId, String scheduleVehicleId);
	Integer getOrderNumByScheduleIdAndCheckStatus(String busScheduleId, String checkStatus, String scheduleVehicleId);
	Integer getOrderTicketSumByScheduleId(String busScheduleId, String scheduleVehicleId);
	Integer getOrderTicketSumByScheduleIdAndCheckStatus(String busScheduleId, String checkStatus,
			String scheduleVehicleId);
	
	Double getSumSpentByBusScheduleIdAndScheduleVehicleId(String busScheduleId, String scheduleVehicleId);

	JsonObj orderServiceType(String vehicleScheduleId, String payIndex);
	
	JsonObj orderSVDetailServiceType(String scheduleVehicleId, String payIndex);
	/**
	 * 根据订单id将订单设为已支付
	 * @param orderId
	 * @return 0 成功； -1 订单id无效；-2 订单异常，保存时未设置支付状态; -3 该订单已支付； 
	 */
	int setPaidAndChecked(String orderId);
	/**
	 * 获取退款参数
	 * @param orderCode 待退款订单code
	 * @return 返回值为null代表不能退款，不为null时jsonObj中包含total,refundMoney字段
	 * 其中total代表该订单支付总金额，refunMoney代表退款金额，单位均为分
	 * @throws SQLException 
	 */
	JsonObj refundMoney(String orderCode)  throws SQLException, JsonProcessingException,IOException  ;
	/**
	 * 完成实际退款操作
	 * @param orderCode 订单code
	 * @throws SQLException 
	 */
	void refundOrder(String orderCode,Double refundMoney) throws SQLException;
	/**
	 * 验票成功后执行的操作，现包括发送验票通知，和等级检查
	 * @param orderId
	 * @throws SQLException
	 */
	public void afterOrderChecked(String orderId) throws SQLException;
	/**
	 * 根据code更改订单支付状态
	 * @param orderCode
	 * @param payStatus
	 */
	public void changeOrderPayStatusByCode(String orderCode, OrderPayStatus payStatus);
	/**
	 * 升级后更改注册时优惠券
	 * @param personalAccountId
	 */
	void updateSignInCouponsn(String personalAccountId);
	/**
	 * 是否采用微信api退款
	 * @return 返回true代表采用微信api退款，返回false仅将订单状态改为退款已提交
	 */
	boolean enableWXAutoRefund();
	/**
	 * 获取scheduleVehicleId对应班次相应车辆的余票
	 * @return
	 * @throws SQLException 
	 */
	Long remainTicketsAmount(String scheduleVehicleId);
	/**
	 * 下单后有效支付时间，单位为秒
	 */
	int NOT_PAID_ORDER_VAILD_SECONDS = 60 * 2;
	Integer getOrderNumByScheduleVehicleId(String scheduleVehicleId);
	Integer getOrderNum(String scheduleVehicleId);
	List<PassengerBusOrder> findList(String bus_schedule_id, String schedule_vehicle_id);
	List<PassengerBusOrder> updateList(List<PassengerBusOrder> passengerbusorders);
	PassengerBusOrder saveOrderByPs(PassengerBusOrder passengerBusOrder);
}
