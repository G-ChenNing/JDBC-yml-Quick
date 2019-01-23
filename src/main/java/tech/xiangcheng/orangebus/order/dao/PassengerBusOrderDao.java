package tech.xiangcheng.orangebus.order.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;

public interface PassengerBusOrderDao extends JpaRepository<PassengerBusOrder, String> {
	PassengerBusOrder findByIdAndDelFlag(String id, String delFlag);
	PassengerBusOrder findById(String id);
	PassengerBusOrder findByCodeAndDelFlag(String code, String delFlag);
	List<PassengerBusOrder> findByBusSchedule_IdAndConfigPayStatus_Id(String id, String payStatus);
	List<PassengerBusOrder> findByBusSchedule_IdAndScheduleVehicle_Id(String bus_schedule_id,String schedule_vehicle_id);
	List<PassengerBusOrder> findByBusSchedule_IdAndConfigPayStatus_IdAndConfigCheckStatus_IdOrderByOriginalDateAsc(String id, String payStatus, String checkStatus);
	List<PassengerBusOrder> findByBusSchedule_IdAndConfigPayStatus_IdAndConfigCheckStatus_IdAndConfigPayMethod_IdAndScheduleVehicle_IdOrderByOriginalDateAsc(String id, String payStatus, String checkStatus, String payMethod,String scheduleVehicleId);
//	List<PassengerBusOrder> findByBusSchedule_IdAndConfigPayStatus_IdAndConfigCheckStatus_IdAndScheduleVehicle_Id(String id, String payStatus, String checkStatus,String scheduleVehicleId);

	List<PassengerBusOrder> findByBuyerId(String id);
	//用户不受出发时间控制的各种类型订单，如适用：gone已出行订单，未支付订单，已支付订单，
	List<PassengerBusOrder> findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idOrderByCreateDateDesc(String userId, String payStatus, String checkStatus);
	//toBePaid，toBeGone未支付订单或已支付待出行订单，班次过期的订单不显示，未支付且超过支付时限的不显示   BusSchedule_DepartureTime after  
	List<PassengerBusOrder> findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idAndBusSchedule_DepartureTimeAfterAndCreateDateBetweenOrderByCreateDateDesc(String userId, String payStatus, String checkStatus, Date now, Date from, Date to);
	//BusSchedule_DepartureTime before 暂时未用到  已支付未乘车 已过期订单
	List<PassengerBusOrder> findByBuyerIdAndConfigPayStatus_IdAndConfigCheckStatus_idAndBusSchedule_DepartureTimeBeforeOrderByCreateDateDesc(String userId, String payStatus, String checkStatus, Date now);
	//afterSale 退款已提交 退款已完成订单
	List<PassengerBusOrder> findByBuyerIdAndConfigPayStatus_IdInOrderByUpdateDateDesc(String userId, Collection<String> s);
	
//	@Query(value = "select sum(pbo.total) from passengerBusOrder pbo where pbo.buyer.id =:userid")
//	Double getSumSpentByUserIdO(@Param("userid")String userid);
	@Query(value = "select count(1) from passenger_bus_order where PAY_STATUS = :payStatus and pay_method = :payMethod and bus_schedule_id = :busScheduleId and schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Integer getOrderNumByScheduleId(@Param("payStatus")String payStatus,@Param("payMethod")String payMethod, @Param("busScheduleId")String busScheduleId, @Param("scheduleVehicleId")String scheduleVehicleId);
	
	//是否有订单
	@Query(value = "select count(1) from passenger_bus_order where PAY_STATUS = :payStatus and schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Integer getOrderNumByScheduleVehicleId(@Param("payStatus")String payStatus, @Param("scheduleVehicleId")String scheduleVehicleId);
	//预定加已售
	@Query(value = "select count(1) from passenger_bus_order where schedule_vehicle_id = :scheduleVehicleId " , nativeQuery = true)
	Integer getNumByScheduleVehicleId(@Param("scheduleVehicleId")String scheduleVehicleId);
	
	@Query(value = "select if(isnull(sum(amount)),0,sum(amount)) from passenger_bus_order where PAY_STATUS = :payStatus and pay_method = :payMethod and bus_schedule_id = :busScheduleId and schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Integer getOrderTicketSumByScheduleId(@Param("payStatus")String payStatus,@Param("payMethod")String payMethod, @Param("busScheduleId")String busScheduleId, @Param("scheduleVehicleId")String scheduleVehicleId);
	
	@Query(value = "select count(1) from passenger_bus_order where PAY_STATUS = :payStatus and pay_method = :payMethod and bus_schedule_id = :busScheduleId and check_status= :checkStatus and schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Integer getOrderNumByScheduleIdAndCheckStatus(@Param("payStatus")String payStatus,@Param("payMethod")String payMethod, @Param("busScheduleId")String busScheduleId, @Param("checkStatus")String checkStatus, @Param("scheduleVehicleId")String scheduleVehicleId);

	@Query(value = "select if(isnull(sum(amount)),0,sum(amount)) from passenger_bus_order where PAY_STATUS = :payStatus and pay_method = :payMethod and bus_schedule_id = :busScheduleId and check_status= :checkStatus and schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Integer getOrderTicketSumByScheduleIdAndCheckStatus(@Param("payStatus")String payStatus,@Param("payMethod")String payMethod, @Param("busScheduleId")String busScheduleId, @Param("checkStatus")String checkStatus, @Param("scheduleVehicleId")String scheduleVehicleId);
	
	@Query(value = "select if(isnull(sum(total)),0,sum(total)) from passenger_bus_order where PAY_STATUS = :payStatus and check_status=:checkStatus and buyer_id =:userid" , nativeQuery = true)
	Double getSumSpentByUserId(@Param("payStatus")String payStatus,@Param("checkStatus")String checkStatus, @Param("userid")String userid);

	@Query(value = "select if(isnull(sum(total)),0,sum(total)) from passenger_bus_order where PAY_STATUS = :payStatus and bus_schedule_id = :busScheduleId and schedule_vehicle_id =:scheduleVehicleId" , nativeQuery = true)
	Double getSumSpentByBusScheduleIdAndScheduleVehicleId(@Param("payStatus")String payStatus,@Param("busScheduleId")String busScheduleId, @Param("scheduleVehicleId")String scheduleVehicleId);

	
//	@Query(value = "select sum(total) from passenger_bus_order where buyer_id =:userid" , nativeQuery = true)
//	String getSumSpentByUserIdString(@Param("userid")String userid);
	
	/**
	 * 该函数未作任何参数检查，应在调用前做处理
	 * @param id
	 * @param payStatus
	 */
	@Modifying
	@Query(value = "update passenger_bus_order set PAY_STATUS = :payStatus where id =:id", nativeQuery = true)
	void setPayStatusByOrderId(@Param("id")String id,@Param("payStatus")String payStatus);
	
	@Modifying
	@Query(value = "update passenger_bus_order set PAY_STATUS = :payStatus, PAY_DATE = :payDate where id =:id", nativeQuery = true)
	void setPayStatusAndPayDateByOrderId(@Param("id")String id,@Param("payStatus")String payStatus,@Param("payDate")Date payDate);
	
	/**
	 * 该函数未作任何参数检查，应在调用前做处理
	 * @param id
	 * @param payStatus
	 */
	@Modifying
	@Query(value = "update passenger_bus_order set PAY_STATUS = :payStatus, CHECK_STATUS = :checkStatus where id =:id", nativeQuery = true)
	void setPayCheckedStatusByOrderId(@Param("id")String id,@Param("payStatus")String payStatus,@Param("checkStatus")String checkStatus);
	
	
	/**
	 * 该函数未作任何参数检查，应在调用前做处理
	 * @param id
	 * @param payStatus checker checkDate
	 */
	@Modifying
	@Query(value = "update passenger_bus_order set CHECK_STATUS = :checkStatus  where id =:id", nativeQuery = true)
	void setCheckStatusByOrderId(@Param("id")String id,@Param("checkStatus")String checkStatus);
	
	@Modifying
	@Transactional
	@Query(value = "update passenger_bus_order set CHECK_STATUS = :checkStatus , CHECKER = :checker , CHECK_DATE = :checkDate  where id =:id", nativeQuery = true)
	void setCheckStatusCheckerByOrderId(@Param("id")String id,@Param("checkStatus")String checkStatus,@Param("checker")String checker,@Param("checkDate")Date checkDate);
	
	@Modifying
	@Query(value = "update passenger_bus_order set CHECK_STATUS = :checkStatus where code =:code", nativeQuery = true)
	void setCheckStatusByOrderCode(@Param("code")String code,@Param("checkStatus")String checkStatus);

	@Modifying
	@Query(value = "update passenger_bus_order set PAY_STATUS = :payStatus where code =:code", nativeQuery = true)
	void setPayStatusByOrderCode(@Param("code")String code,@Param("payStatus")String payStatus);
	
	@Modifying
	@Query(value = "update passenger_bus_order set PAY_STATUS = :payStatus, refund_submit_date = :RefundSubmitDate where code =:code", nativeQuery = true)
	void setPayStatusAndRefundSubmitDateByOrderCode(@Param("code")String code,@Param("payStatus")String payStatus,@Param("RefundSubmitDate")Date RefundSubmitDate);
	
	@Modifying
	@Query(value = "update passenger_bus_order set PAY_STATUS = :payStatus, alter_price=:alterPrice, refund_submit_date = :RefundSubmitDate where code =:code", nativeQuery = true)
	void setPayStatusRefundAndAlterPriceByOrderCode(@Param("code")String code, @Param("alterPrice")Double alterPrice, @Param("payStatus")String payStatus,@Param("RefundSubmitDate")Date RefundSubmitDate);
	
	
	List<PassengerBusOrder> findByBuyer_NameOrderByCreateDateDesc(String username);
	List<PassengerBusOrder> findByBuyer_idOrderByCreateDateDesc(String buyerId);
	
	/**
	 * 已验票数量
	 * @param id
	 * @param checkStatusId
	 * @return
	 */
	Long countByScheduleVehicle_IdAndConfigCheckStatus_Id(String id, String checkStatusId);
	/**
	 * 已支付数量
	 * @param id
	 * @param payStatusId
	 * @return
	 */
	Long countByScheduleVehicle_IdAndConfigPayStatus_Id(String id, String payStatusId);
	/**
	 * 获得相应支付方式已支付数量
	 * @param id
	 * @param payStatusIs
	 * @param payMethodId
	 * @return
	 */
	Long countByScheduleVehicle_idAndConfigPayStatus_IdAndConfigPayMethod_Id(String id, String payStatusIs, String payMethodId);
	
}
