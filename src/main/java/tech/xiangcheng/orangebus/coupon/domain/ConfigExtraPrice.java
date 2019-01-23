package tech.xiangcheng.orangebus.coupon.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 班次
 * 
 *
 */
@Entity
public class ConfigExtraPrice extends DataEntity {

	public ConfigExtraPrice() {
	}

	@Id
	protected String id;
	
	/**
	 * 该班次的所属车次
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "config_price_id")
	@JsonBackReference 
	protected ConfigPrice configPrice;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	protected Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	protected Date endDate;

	double extraPrice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ConfigPrice getConfigPrice() {
		return configPrice;
	}

	public void setConfigPrice(ConfigPrice configPrice) {
		this.configPrice = configPrice;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getExtraPrice() {
		return extraPrice;
	}

	public void setExtraPrice(double extraPrice) {
		this.extraPrice = extraPrice;
	}
	


	

}