package tech.xiangcheng.orangebus.coupon.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.account.domain.MemberLevel;
import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 代金券明细Entity
 * @author kyle
 * @version 2017-05-15
 */
@Entity
public class ConfigPrice extends DataEntity {
	
	@Id
	protected String id;
	
	/**
	 * 该班次的所属车次
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "COACH_NUMBER_ID")
	@JsonBackReference 
	protected CoachNumber coachNumber;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "MEMBER_LEVEL_ID")
	@JsonBackReference 
	protected MemberLevel memberLevel;		// 会员等级
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "original_city")
	@JsonBackReference
	protected SysArea originalCity;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "terminal_city")
	@JsonBackReference
	protected SysArea terminalCity;
	
	protected Double price;		// 价格
	
	/**
	 * 20170918wcn  额外价格
	 */
	@OneToMany(mappedBy = "configPrice",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<ConfigExtraPrice> configExtraPriceList = new ArrayList<ConfigExtraPrice>();
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CoachNumber getCoachNumber() {
		return coachNumber;
	}

	public void setCoachNumber(CoachNumber coachNumber) {
		this.coachNumber = coachNumber;
	}

	public MemberLevel getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}

	public SysArea getOriginalCity() {
		return originalCity;
	}

	public void setOriginalCity(SysArea originalCity) {
		this.originalCity = originalCity;
	}

	public SysArea getTerminalCity() {
		return terminalCity;
	}

	public void setTerminalCity(SysArea terminalCity) {
		this.terminalCity = terminalCity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<ConfigExtraPrice> getConfigExtraPriceList() {
		return configExtraPriceList;
	}

	public void setConfigExtraPriceList(List<ConfigExtraPrice> configExtraPriceList) {
		this.configExtraPriceList = configExtraPriceList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coachNumber == null) ? 0 : coachNumber.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((memberLevel == null) ? 0 : memberLevel.hashCode());
		result = prime * result + ((originalCity == null) ? 0 : originalCity.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((terminalCity == null) ? 0 : terminalCity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigPrice other = (ConfigPrice) obj;
		if (coachNumber == null) {
			if (other.coachNumber != null)
				return false;
		} else if (!coachNumber.equals(other.coachNumber))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (memberLevel == null) {
			if (other.memberLevel != null)
				return false;
		} else if (!memberLevel.equals(other.memberLevel))
			return false;
		if (originalCity == null) {
			if (other.originalCity != null)
				return false;
		} else if (!originalCity.equals(other.originalCity))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (terminalCity == null) {
			if (other.terminalCity != null)
				return false;
		} else if (!terminalCity.equals(other.terminalCity))
			return false;
		return true;
	}

	
	
	
}