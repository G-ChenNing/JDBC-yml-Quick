package tech.xiangcheng.orangebus.order.domain;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.StationCoachNumber;
import tech.xiangcheng.orangebus.config.domain.ConfigCheckStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigPayMethod;
import tech.xiangcheng.orangebus.config.domain.ConfigPayStatus;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;


/**
 * 中短途客运车票订单
 *
 */
@Entity(name="passenger_bus_order")
public class PassengerBusOrder extends DataEntity {

	@Id
	protected String id;
	
	String code;
	Integer amount;
	Double total;//最终支付价格，减去代金券
	
	
	@ManyToOne
	@JoinColumn(name = "BUYER_ID")
	@JsonBackReference
	protected PersonalAccount buyer;//下单帐号
	/**
	 * 订单对应的班次
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "BUS_SCHEDULE_ID")
	@JsonBackReference
	protected BusSchedule busSchedule;

	@ManyToOne(optional = false)
	@JoinColumn(name = "pay_method")
	@JsonBackReference
	private ConfigPayMethod configPayMethod;		// 支付方式

	@ManyToOne(optional = false)
	@JoinColumn(name = "PAY_STATUS")
	@JsonBackReference
	private ConfigPayStatus configPayStatus;		// 支付状态

	@ManyToOne(optional = false)
	@JoinColumn(name = "CHECK_STATUS")
	@JsonBackReference
	private ConfigCheckStatus configCheckStatus;		// 验票状态
	
	/**
	 * 优惠金额
	 */
	protected Double discount;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "schedule_vehicle_id")
	@JsonBackReference
	protected ScheduleVehicle scheduleVehicle;		// 选择车辆的额外价格
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "original_station_coach_id")
	@JsonBackReference
	protected StationCoachNumber originalStationCoach;		// 乘客选择的上车站点
	
	protected Date originalDate;//  乘客选择的上车站点的时间
	protected Date terminalDate;//  乘客选择的下车站点的时间
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "terminal_station_coach_id")
	@JsonBackReference
	protected StationCoachNumber terminalStationCoach;		//  乘客选择的下车站点
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "coupon_sn_id")
	@JsonBackReference
	protected CouponSn couponSn;		// 优惠券金额
	
	//	protected boolean needStationsListOrNot=true;

	private String service; // 服务
	
	private String checker;//验票人
	private Date checkDate;//验票时间
	private Double alterPrice;//退款，改签时的调整价格，如：实际退款价格，改签中新订单比旧订单便宜的退款差价，新订单比旧订单贵的额外需支付差价，
	private String alterOrderId;//改签完成时，加入旧订单的id
	
	private Date payDate; //20171114支付日期，微信回调函数使用
	private Date refundSubmitDate;//refund_submit_date 20171211 退款时间，微信回调函数使用
	private String coType;//合作类型
	private String checkPhoneNum;//乘务员向导电话号
	public String getChecker() {
		return checker;
	}

	
	
	public Date getPayDate() {
		return payDate;
	}



	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}



	public void setChecker(String checker) {
		this.checker = checker;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	/**
	 * 订单包含的车票
	 */
//	@OneToMany(mappedBy = "order",cascade=CascadeType.ALL)
//	@JsonManagedReference
//	protected List<PassengerBusTicket> tickets = new ArrayList<PassengerBusTicket>();
	

	public BusSchedule getBusSchedule() {
		return busSchedule;
	}

	public void setBusSchedule(BusSchedule busSchedule) {
		this.busSchedule = busSchedule;
	}


	public PersonalAccount getBuyer() {
		return buyer;
	}

	public void setBuyer(PersonalAccount buyer) {
		this.buyer = buyer;
	}


	public Integer getAmount() {
		return amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public ConfigPayMethod getConfigPayMethod() {
		return configPayMethod;
	}

	public void setConfigPayMethod(ConfigPayMethod configPayMethod) {
		this.configPayMethod = configPayMethod;
	}

	public ConfigPayStatus getConfigPayStatus() {
		return configPayStatus;
	}

	public void setConfigPayStatus(ConfigPayStatus configPayStatus) {
		this.configPayStatus = configPayStatus;
	}

	public ConfigCheckStatus getConfigCheckStatus() {
		return configCheckStatus;
	}

	public void setConfigCheckStatus(ConfigCheckStatus configCheckStatus) {
		this.configCheckStatus = configCheckStatus;
	}

	public ScheduleVehicle getScheduleVehicle() {
		return scheduleVehicle;
	}

	public void setScheduleVehicle(ScheduleVehicle scheduleVehicle) {
		this.scheduleVehicle = scheduleVehicle;
	}

	public StationCoachNumber getOriginalStationCoach() {
		return originalStationCoach;
	}

	public void setOriginalStationCoach(StationCoachNumber originalStationCoach) {
		this.originalStationCoach = originalStationCoach;
	}

	public StationCoachNumber getTerminalStationCoach() {
		return terminalStationCoach;
	}

	public void setTerminalStationCoach(StationCoachNumber terminalStationCoach) {
		this.terminalStationCoach = terminalStationCoach;
	}

	public CouponSn getCouponSn() {
		return couponSn;
	}

	public void setCouponSn(CouponSn couponSn) {
		this.couponSn = couponSn;
	}

	public Date getOriginalDate() {
		return originalDate;
	}

	public void setOriginalDate(Date originalDate) {
		this.originalDate = originalDate;
	}

	public Date getTerminalDate() {
		return terminalDate;
	}

	public void setTerminalDate(Date terminalDate) {
		this.terminalDate = terminalDate;
	}

	
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Double getAlterPrice() {
		return alterPrice;
	}

	public void setAlterPrice(Double alterPrice) {
		this.alterPrice = alterPrice;
	}

	public String getAlterOrderId() {
		return alterOrderId;
	}

	public void setAlterOrderId(String alterOrderId) {
		this.alterOrderId = alterOrderId;
	}

	public Date getRefundSubmitDate() {
		return refundSubmitDate;
	}



	public void setRefundSubmitDate(Date refundSubmitDate) {
		this.refundSubmitDate = refundSubmitDate;
	}



	public String getCoType() {
		return coType;
	}



	public void setCoType(String coType) {
		this.coType = coType;
	}



	public String getCheckPhoneNum() {
		return checkPhoneNum;
	}



	public void setCheckPhoneNum(String checkPhoneNum) {
		this.checkPhoneNum = checkPhoneNum;
	}



	@Override
	public String toString() {
		return "PassengerBusOrder [id=" + id + ", code=" + code + ", amount=" + amount + ", total=" + total + ", buyer="
				+ buyer + ", busSchedule=" + busSchedule + ", configPayMethod=" + configPayMethod + ", configPayStatus="
				+ configPayStatus + ", configCheckStatus=" + configCheckStatus + ", discount=" + discount
				+ ", scheduleVehicle=" + scheduleVehicle + ", originalStationCoach=" + originalStationCoach
				+ ", originalDate=" + originalDate + ", terminalDate=" + terminalDate + ", terminalStationCoach="
				+ terminalStationCoach + ", couponSn=" + couponSn + ", service=" + service + ", checker=" + checker
				+ ", checkDate=" + checkDate + ", alterPrice=" + alterPrice + ", alterOrderId=" + alterOrderId
				+ ", payDate=" + payDate + ", refundSubmitDate=" + refundSubmitDate + ", coType=" + coType + "]";
	}


	
	
}
