package tech.xiangcheng.orangebus.order.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.CheckAccountStationCoach;
import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleJson;
import tech.xiangcheng.orangebus.company.domain.StationCoachNumber;
import tech.xiangcheng.orangebus.coupon.domain.CouponGroup;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;
import tech.xiangcheng.orangebus.leaf.util.json.JsonUtil;
import tech.xiangcheng.orangebus.leaf.util.time.DateFormatUtil;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;

/**
 * 包装passengerBusOrder，返回给前端
 * 
 * @author yang
 *
 */
public class OrderJson {
	String id;// 订单id
	String coachNumberName = "";// 车次名称
	String payStatus = "";// 支付状态
	String checkStatus = "";// 检票状态 未检票； 已检票
	String checkStatusId = "";//
	String busScheduleId = "";// 班次id
	String payMethod;// 支付方式
	double total = 0;// 总价格
	String originalStationCoachName = ""; // 乘客选择的上车站点名称
	String originalStationCoachTime = ""; // 乘客选择的上车站点时间
	long originalStationCoachTimeX = 0;
	String originalStationCityName = ""; // 出发城市
	String originalStationCityCode = ""; // 出发城市编号
	String terminalStationCoachName = ""; // 乘客选择的下车站点名称
	String terminalStationCoachTime = ""; // 乘客选择的上车站点时间
	long terminalStationCoachTimeX = 0;
	String terminalStationCityName = ""; // 目标城市
	String terminalStationCityCode = ""; // 目标城市编号
	String orderCreateTime = "";// 下单时间
	String code = "";// 订单code
	String originalStationCoachAddress = ""; // 乘客选择的上车站点地址
	String checkAccountPhoneNum = ""; // 乘务员电话
	int amount;// 订单票数
	String userPhone = "";
	String couponSnId; // 优惠券id
	int couponValue;// 代金券抵扣金额
	int couponMin;// 代金券满减金额
	long couponEndDate;
	String companyName;// 车次所属公司名称
	int remainSeatNum = 0;// 该班次剩余座位
	Double alterPrice = null;
	String alertOrderId = "";
	// String departureTime="";//发车时间
	// String couponSnCode;//优惠券code
	// String couponName;//优惠券名称
	// String busScheduleCode;
	// String coachNumberCode;
	// double price;//车次价格
	// int kilometre;//车次里程

	ScheduleVehicleJson vehicleInfo;// 车辆信息

	public OrderJson(PassengerBusOrder order) {
		if (null == order)
			return;
		id = order.getId() == null ? "" : order.getId();
		orderCreateTime = order.getCreateDate() == null ? ""
				: DateFormatUtil.getyyyy_MM_dd_hh_mmDateStr(order.getCreateDate());
		code = order.getCode() == null ? "" : order.getCode();
		amount = order.getAmount() == null ? 0 : order.getAmount();
		alterPrice = order.getAlterPrice() == null ? null : order.getAlterPrice();
		List<CheckAccountStationCoach> cascs = order.getScheduleVehicle().getVehicle().getCheckAccountStationCoach();
//		if (cascs.size() > 0) {
//			for (int i = 0; i < cascs.size(); i++) {
//				if (cascs.get(i).getRole() == Integer.valueOf(ConfigEnum.CAS_MAIN_CHECK.getValue()).intValue()) {
//					checkAccountPhoneNum = cascs.get(i).getCheckAccount().getPhone();
//					break;
//				}
//			}
//		}
		checkAccountPhoneNum = order.getCheckPhoneNum();
		if ("".equals(checkAccountPhoneNum)) {
			checkAccountPhoneNum = "未配置";
		}
		// if(cascs!=null && !cascs.isEmpty() &&
		// cascs.get(0).getCheckAccount().getPhone()!=null){
		// checkAccountPhoneNum=cascs.get(0).getCheckAccount().getPhone();
		// }else{
		// checkAccountPhoneNum= "未配置";
		// }
		total = order.getTotal() == null ? 0 : order.getTotal();
		payMethod = order.getConfigPayMethod() == null ? "" : order.getConfigPayMethod().getName();
		payStatus = order.getConfigPayStatus() == null ? "" : order.getConfigPayStatus().getName();
		checkStatus = order.getConfigCheckStatus() == null ? "" : order.getConfigCheckStatus().getName();
		checkStatusId = order.getConfigCheckStatus() == null ? "" : order.getConfigCheckStatus().getId();

		busScheduleId = order.getBusSchedule().getId() == null ? "" : order.getBusSchedule().getId();

		originalStationCoachName = order.getOriginalStationCoach() == null ? ""
				: order.getOriginalStationCoach().getStation().getName(); // 乘客选择的上车站点名称
		originalStationCoachTime = order.getOriginalDate() == null ? ""
				: DateFormatUtil.getyyyy_MM_dd_hh_mmDateStr(order.getOriginalDate()); // 乘客选择的上车站点时间
		originalStationCoachTimeX = order.getOriginalDate() == null ? 0
				: order.getOriginalDate().getTime(); // 乘客选择的上车站点时间
		originalStationCityName = order.getOriginalStationCoach() == null ? ""
				: order.getOriginalStationCoach().getStation().getSysArea().getName(); // 出发城市
		originalStationCityCode = order.getOriginalStationCoach() == null ? ""
				: order.getOriginalStationCoach().getStation().getSysArea().getCode(); // 出发城市编号
		terminalStationCoachName = order.getTerminalStationCoach() == null ? ""
				: order.getTerminalStationCoach().getStation().getName(); // 乘客选择的下车站点名称
		terminalStationCoachTime = order.getTerminalDate() == null ? ""
				: DateFormatUtil.getyyyy_MM_dd_hh_mmDateStr(order.getTerminalDate()); // 乘客选择的上车站点时间
		terminalStationCoachTimeX = order.getTerminalDate() == null ? 0
				: order.getTerminalDate().getTime(); // 乘客选择的上车站点时间
		terminalStationCityName = order.getTerminalStationCoach() == null ? ""
				: order.getTerminalStationCoach().getStation().getSysArea().getName(); // 目标城市
		terminalStationCityCode = order.getTerminalStationCoach() == null ? ""
				: order.getTerminalStationCoach().getStation().getSysArea().getCode(); // 目标城市编号
		alertOrderId = order.getAlterOrderId() == null ? "" : order.getAlterOrderId();// 获得改签前旧订单的id
		originalStationCoachAddress = order.getOriginalStationCoach() == null ? ""
				: order.getOriginalStationCoach().getStation().getDetail(); // 乘客选择的上车站点地址
		userPhone = order.getBuyer() == null ? "" : order.getBuyer().getPhone();

		CouponSn cs = order.getCouponSn();
		couponSnId = "";
		// couponSnCode = "";
		// couponName = "";
		couponValue = 0;
		couponMin = 0;
		couponEndDate = 0;
		if (null != cs) {
			couponSnId = cs.getId();
			// couponSnCode = cs.getCode();
			CouponGroup couponGroup = cs.getCouponGroup();
			if (null != couponGroup) {
				// couponName = couponGroup.getCouponName() == null ? "" :
				// couponGroup.getCouponName();
				couponValue = couponGroup.getCouponValue() == null ? 0 : couponGroup.getCouponValue();
				couponMin = couponGroup.getMinAmount() == null ? 0 : couponGroup.getMinAmount();
				couponEndDate = couponGroup.getEndTime() == null ? 0 : couponGroup.getEndTime().getTime();
			}
		}

		// BusSchedule busSchedule = sv.getBusSchedule();
		BusSchedule busSchedule = order.getBusSchedule();
		// busScheduleCode = "";
		// departureTime = "";
		// coachNumberCode = "";
		coachNumberName = "";
		// price = 0.0;
		// companyName = "";
		// kilometre = 0;//车次里程
		if (null != busSchedule) {
			// busScheduleCode = busSchedule.getCode() == null ? "" : busSchedule.getCode();
			// departureTime =
			// DateFormatUtil.getyyyy_MM_dd_hh_mmDateStr(busSchedule.getDepartureTime());
			CoachNumber cn = busSchedule.getCoachNumber();
			if (null != cn) {
				// coachNumberCode = cn.getCode() == null ? "" : cn.getCode();
				coachNumberName = cn.getName() == null ? "" : cn.getName();
				// price = cn.getPrice() == null ? 0 : cn.getPrice().doubleValue();
				companyName = cn.getSysOffice() == null ? ""
						: (cn.getSysOffice().getName() == null ? "" : cn.getSysOffice().getName());
				// kilometre = cn.getKilometre();
			}
			/**/
			ScheduleVehicle sv = order.getScheduleVehicle();
			if (null != sv) {
				vehicleInfo = new ScheduleVehicleJson(sv);
			}

		}
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getCouponSnId() {
		return couponSnId;
	}

	public void setCouponSnId(String couponSnId) {
		this.couponSnId = couponSnId;
	}

	public int getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(int couponValue) {
		this.couponValue = couponValue;
	}

	public String getCoachNumberName() {
		return coachNumberName;
	}

	public void setCoachNumberName(String coachNumberName) {
		this.coachNumberName = coachNumberName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ScheduleVehicleJson getVehicleInfo() {
		return vehicleInfo;
	}

	public void setVehicleInfo(ScheduleVehicleJson vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public static void main(String[] args) throws JsonProcessingException {
		CoachNumber coachNumber = new CoachNumber();
		List<StationCoachNumber> stationCoachNumbers = new ArrayList<StationCoachNumber>();
		for (int i = 0; i < 3; ++i) {
			StationCoachNumber stationCoachNumber = new StationCoachNumber();
			stationCoachNumber.setRank(i);
			stationCoachNumbers.add(stationCoachNumber);
		}
		coachNumber.setStationCoachNumbers(stationCoachNumbers);
		BusSchedule busSchedule = new BusSchedule();
		busSchedule.setCoachNumber(coachNumber);

		PassengerBusOrder order = new PassengerBusOrder();
		order.setBusSchedule(busSchedule);
		ScheduleVehicle scheduleVehicle = new ScheduleVehicle();
		order.setScheduleVehicle(scheduleVehicle);
		OrderJson oj = new OrderJson(order);
		// System.out.println(JsonUtil.getJsonStr(oj));
	}

	public String getOriginalStationCoachName() {
		return originalStationCoachName;
	}

	public void setOriginalStationCoachName(String originalStationCoachName) {
		this.originalStationCoachName = originalStationCoachName;
	}

	public String getOriginalStationCoachTime() {
		return originalStationCoachTime;
	}

	public void setOriginalStationCoachTime(String originalStationCoachTime) {
		this.originalStationCoachTime = originalStationCoachTime;
	}

	public String getOriginalStationCityName() {
		return originalStationCityName;
	}

	public void setOriginalStationCityName(String originalStationCityName) {
		this.originalStationCityName = originalStationCityName;
	}

	public String getTerminalStationCoachName() {
		return terminalStationCoachName;
	}

	public void setTerminalStationCoachName(String terminalStationCoachName) {
		this.terminalStationCoachName = terminalStationCoachName;
	}

	public String getTerminalStationCoachTime() {
		return terminalStationCoachTime;
	}

	public void setTerminalStationCoachTime(String terminalStationCoachTime) {
		this.terminalStationCoachTime = terminalStationCoachTime;
	}

	public String getTerminalStationCityName() {
		return terminalStationCityName;
	}

	public void setTerminalStationCityName(String terminalStationCityName) {
		this.terminalStationCityName = terminalStationCityName;
	}

	public String getOriginalStationCoachAddress() {
		return originalStationCoachAddress;
	}

	public void setOriginalStationCoachAddress(String originalStationCoachAddress) {
		this.originalStationCoachAddress = originalStationCoachAddress;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getBusScheduleId() {
		return busScheduleId;
	}

	public void setBusScheduleId(String busScheduleId) {
		this.busScheduleId = busScheduleId;
	}

	public String getCheckStatusId() {
		return checkStatusId;
	}

	public void setCheckStatusId(String checkStatusId) {
		this.checkStatusId = checkStatusId;
	}

	public String getCheckAccountPhoneNum() {
		return checkAccountPhoneNum;
	}

	public void setCheckAccountPhoneNum(String checkAccountPhoneNum) {
		this.checkAccountPhoneNum = checkAccountPhoneNum;
	}

	public int getRemainSeatNum() {
		return remainSeatNum;
	}

	public void setRemainSeatNum(int remainSeatNum) {
		this.remainSeatNum = remainSeatNum;
	}

	public String getOriginalStationCityCode() {
		return originalStationCityCode;
	}

	public void setOriginalStationCityCode(String originalStationCityCode) {
		this.originalStationCityCode = originalStationCityCode;
	}

	public String getTerminalStationCityCode() {
		return terminalStationCityCode;
	}

	public void setTerminalStationCityCode(String terminalStationCityCode) {
		this.terminalStationCityCode = terminalStationCityCode;
	}

	public int getCouponMin() {
		return couponMin;
	}

	public void setCouponMin(int couponMin) {
		this.couponMin = couponMin;
	}

	public long getCouponEndDate() {
		return couponEndDate;
	}

	public void setCouponEndDate(long couponEndDate) {
		this.couponEndDate = couponEndDate;
	}

	public Double getAlterPrice() {
		return alterPrice;
	}

	public void setAlterPrice(Double alterPrice) {
		this.alterPrice = alterPrice;
	}

	public String getAlertOrderId() {
		return alertOrderId;
	}

	public void setAlertOrderId(String alertOrderId) {
		this.alertOrderId = alertOrderId;
	}

	public long getOriginalStationCoachTimeX() {
		return originalStationCoachTimeX;
	}

	public void setOriginalStationCoachTimeX(long originalStationCoachTimeX) {
		this.originalStationCoachTimeX = originalStationCoachTimeX;
	}

	public long getTerminalStationCoachTimeX() {
		return terminalStationCoachTimeX;
	}

	public void setTerminalStationCoachTimeX(long terminalStationCoachTimeX) {
		this.terminalStationCoachTimeX = terminalStationCoachTimeX;
	}

}
