package tech.xiangcheng.orangebus.account.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import tech.xiangcheng.orangebus.parent.domain.Constant;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * @author yang
 *
 */
@Entity
@Table(name = "wxaccount_check")
public class WxaccountCheck extends DataEntity {
	@Id
	String id;
	@OneToOne
	private CheckAccount checkAccount;
	private String openid;
	String nickname;
	String sex;
	String city;
	String country;
	String province;
	String language;
	String headimgurl;
	String subscribe;
	Date subscibeTime;
	
	@SuppressWarnings("unused")
	private WxaccountCheck() {
		
	}
	public WxaccountCheck(String openid) {
		super();
		this.openid = openid;
		this.delFlag=Constant.NOT_DELTED;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public CheckAccount getCheckAccount() {
		return checkAccount;
	}


	public void setCheckAccount(CheckAccount checkAccount) {
		this.checkAccount = checkAccount;
	}


	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getHeadimgurl() {
		return headimgurl;
	}


	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}


	public String getSubscribe() {
		return subscribe;
	}


	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}


	public Date getSubscibeTime() {
		return subscibeTime;
	}


	public void setSubscibeTime(Date subscibeTime) {
		this.subscibeTime = subscibeTime;
	}

}
