package tech.xiangcheng.orangebus.account.domain;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.xiangcheng.orangebus.account.service.CouponSnService;
import tech.xiangcheng.orangebus.order.service.PassengerBusOrderService;

/**
 * json
 * @author yang
 *
 */
public class PersonalAccountJson {
	String id;
	String name;//可能为空
	String phone;//手机号登陆
	String email;
	Long couponSnNum;
//	Double spentMoney;
	Double points;
	String memberLevel;
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PersonalAccountJson.class);
	
	public PersonalAccountJson(PersonalAccount account) {
		if (null != account) {
			id = account.getId();
			name = account.getName();
			phone= account.getPhone();
			email=account.getEmail();
			memberLevel=account.getMemberLevel().getName();
			points=account.getPoints();
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCouponSnNum() {
		return couponSnNum;
	}
	public void setCouponSnNum(Long couponSnNum) {
		this.couponSnNum = couponSnNum;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
//	public Double getSpentMoney() {
//		return spentMoney;
//	}
//	public void setSpentMoney(Double spentMoney) {
//		this.spentMoney = spentMoney;
//	}
	public String getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}
	public Double getPoints() {
		return points;
	}
	public void setPoints(Double points) {
		this.points = points;
	}
	
	
}
