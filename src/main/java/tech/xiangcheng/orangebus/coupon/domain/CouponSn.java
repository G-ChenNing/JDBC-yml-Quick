package tech.xiangcheng.orangebus.coupon.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 代金券明细Entity
 * @author kyle
 * @version 2017-05-15
 */
@Entity
public class CouponSn extends DataEntity {
	
	@Id
	protected String id;
	
	protected String code;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COUPON_GROUP_ID")
	@JsonBackReference
	protected CouponGroup couponGroup;		// 代金券组id 父类
	//优惠券名称|金额|有效期|
	protected Integer remainTimes;		// 剩余使用次数
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PERSONAL_ACCOUNT_ID")
	@JsonBackReference
	protected PersonalAccount personalAccount;		// 用户id
	
	public CouponSn() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CouponGroup getCouponGroup() {
		return couponGroup;
	}

	public void setCouponGroup(CouponGroup couponGroup) {
		this.couponGroup = couponGroup;
	}
	
	public Integer getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(Integer remainTimes) {
		this.remainTimes = remainTimes;
	}
	
	public PersonalAccount getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(PersonalAccount personalAccount) {
		this.personalAccount = personalAccount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "CouponSn [id=" + id + ", code=" + code + ", couponGroup=" + couponGroup + ", remainTimes=" + remainTimes
				+ ", personalAccount=" + personalAccount + "]";
	}
	
	
	
}