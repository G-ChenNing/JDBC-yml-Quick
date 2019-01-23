package tech.xiangcheng.orangebus.coupon.domain;

import java.util.Date;


/**
 * json
 * @author kyle
 该用户的代金券表coupon_sn，在coupon_sn的personal_account_id关联用户，
 需要字段coupon_sn(remain_times)及关联coupon_group表的 coupon_name, 
 coupon_value, is_limit_office, min_amount, use_times, 
 office_id（不用关联office表），start_time, end_time
 *
 */
public class CouponSnJson {
	String id;
	Integer remainTimes;
	String couponName;
	String couponCode;
	Integer couponValue;		// 代金券可抵扣金额
	Integer minAmount;		// 满足金额可以抵扣
	String officeId;		//所属公司
	Integer isLimitOffice;	//是否限制仅限该公司使用
	Integer useTimes;		// 代金券使用次数
	Date startTime;		// 可使用开始时间
	Date endTime;		// 可使用结束时间
	
	public CouponSnJson(CouponSn couponSn) {
		if (null != couponSn) {
			id= couponSn.getId();
			remainTimes = couponSn.getRemainTimes();
			couponCode = couponSn.getCode();
			couponName=couponSn.getCouponGroup().getCouponName();
			couponValue=couponSn.getCouponGroup().getCouponValue();		// 代金券可抵扣金额
			minAmount=couponSn.getCouponGroup().getMinAmount();		// 满足金额可以抵扣
			officeId=couponSn.getCouponGroup().getSysOffice().getId();
			isLimitOffice=couponSn.getCouponGroup().getIsLimitOffice();
			useTimes=couponSn.getCouponGroup().getUseTimes();		// 代金券使用次数
			startTime=couponSn.getCouponGroup().getStartTime();		// 可使用开始时间
			endTime=couponSn.getCouponGroup().getEndTime();		// 可使用结束时间
		}
	}

	public Integer getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(Integer remainTimes) {
		this.remainTimes = remainTimes;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(Integer couponValue) {
		this.couponValue = couponValue;
	}

	public Integer getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public Integer getIsLimitOffice() {
		return isLimitOffice;
	}

	public void setIsLimitOffice(Integer isLimitOffice) {
		this.isLimitOffice = isLimitOffice;
	}

	public Integer getUseTimes() {
		return useTimes;
	}

	public void setUseTimes(Integer useTimes) {
		this.useTimes = useTimes;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	
	
}
