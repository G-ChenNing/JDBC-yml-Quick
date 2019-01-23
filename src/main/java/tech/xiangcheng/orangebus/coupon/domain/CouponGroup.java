package tech.xiangcheng.orangebus.coupon.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.company.domain.SysOffice;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 代金券组信息Entity
 * @author kyle
 * @version 2017-05-15
 */
@Entity
public class CouponGroup extends DataEntity {
	
	@Id
	protected String id;
	/**
	 * 车次所属客运公司
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "OFFICE_ID")
	@JsonBackReference
	protected SysOffice sysOffice;
	
	protected Integer isLimitOffice;		// 是否限制使用公司（0：不限制；1：限制）
	protected String couponName;		// 代金券名称
	protected Integer couponValue;		// 代金券可抵扣金额
	protected Integer useTimes;		// 代金券使用次数
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	protected Date startTime;		// 可使用开始时间
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	protected Date endTime;		// 可使用结束时间
	
	protected Integer minAmount;		// 满足金额可以抵扣
	protected String couponBg;		// 代金券背景图片
	protected String content;		// 代金券相关内容描述
	
	protected Integer signInSend;//是否为注册赠送代金券，是为1
	
	public CouponGroup() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysOffice getSysOffice() {
		return sysOffice;
	}

	public void setSysOffice(SysOffice sysOffice) {
		this.sysOffice = sysOffice;
	}
	
	public Integer getIsLimitOffice() {
		return isLimitOffice;
	}

	public void setIsLimitOffice(Integer isLimitOffice) {
		this.isLimitOffice = isLimitOffice;
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
	
	public Integer getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}
	
	public String getCouponBg() {
		return couponBg;
	}

	public void setCouponBg(String couponBg) {
		this.couponBg = couponBg;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CouponGroup [id=" + id + ", sysOffice=" + sysOffice + ", isLimitOffice=" + isLimitOffice
				+ ", couponName=" + couponName + ", couponValue=" + couponValue + ", useTimes=" + useTimes
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", minAmount=" + minAmount + ", couponBg="
				+ couponBg + ", content=" + content + ", signInSend=" + signInSend + "]";
	}

	
	
}