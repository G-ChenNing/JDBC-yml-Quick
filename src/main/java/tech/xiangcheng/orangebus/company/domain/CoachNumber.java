package tech.xiangcheng.orangebus.company.domain;

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

import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;
/**
 * 车次信息
 *
 */
@Entity
public class CoachNumber extends DataEntity {

	public CoachNumber() {
	}

	@Id
	protected String id;

	protected String name;
	
	String code;
	/**
	 * 车次的基本票价
	 */
	protected Double price;

	/**
	 * 车次所属客运公司
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "OFFICE_ID")
	@JsonBackReference
	protected SysOffice sysOffice;
	
	protected int kilometre;

	@OneToMany(mappedBy = "coachNumber",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<StationCoachNumber> stationCoachNumbers =new ArrayList<StationCoachNumber>();

//	@OneToMany(mappedBy = "coachNumber",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
//	@JsonManagedReference
//	protected List<CheckAccountStationCoach> checkAccountStationCoach =new ArrayList<CheckAccountStationCoach>();

	/**
	 * 车次的所有班次
	 */
	@OneToMany(mappedBy = "coachNumber",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<BusSchedule> busSchedules = new ArrayList<BusSchedule>();
	
	/**
	 * 车次的所有班次
	 */
	@OneToMany(mappedBy = "coachNumber",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<ConfigPrice> configPrices = new ArrayList<ConfigPrice>();
	
	/**
	 * 20170918wcn  额外价格
	 */
//	@OneToMany(mappedBy = "coachNumber",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
//	@JsonManagedReference
//	protected List<ConfigExtraPrice> configExtraPricess = new ArrayList<ConfigExtraPrice>();
//	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setBusSchedules(List<BusSchedule> busSchedules) {
		this.busSchedules = busSchedules;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 所属线路
	 */
//	@ManyToOne
//	@JoinColumn(name = "BUS_LINE_ID", updatable = false, insertable = false)
//	@NotNull
//	@JsonBackReference
//	protected BusLine busLine;
	
	
	

//	public Company getCompany() {
//		return company;
//	}


//	public void setCompany(Company company) {
//		this.company = company;
//	}

	public String getName() {
		return name;
	}

	public SysOffice getSysOffice() {
		return sysOffice;
	}

	public void setSysOffice(SysOffice sysOffice) {
		this.sysOffice = sysOffice;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BusSchedule> getBusSchedules() {
		return busSchedules;
	}

	public void setBuSchedules(List<BusSchedule> busSchedules) {
		this.busSchedules = busSchedules;
	}

	public List<StationCoachNumber> getStationCoachNumbers() {
		return stationCoachNumbers;
	}

	public void setStationCoachNumbers(List<StationCoachNumber> stationCoachNumbers) {
		this.stationCoachNumbers = stationCoachNumbers;
	}

//	public List<CheckAccountStationCoach> getCheckAccountStationCoach() {
//		return checkAccountStationCoach;
//	}
//
//	public void setCheckAccountStationCoach(List<CheckAccountStationCoach> checkAccountStationCoach) {
//		this.checkAccountStationCoach = checkAccountStationCoach;
//	}

	public int getKilometre() {
		return kilometre;
	}

	public void setKilometre(int kilometre) {
		this.kilometre = kilometre;
	}

	public List<ConfigPrice> getConfigPrices() {
		return configPrices;
	}

	public void setConfigPrices(List<ConfigPrice> configPrices) {
		this.configPrices = configPrices;
	}

//	public List<ConfigExtraPrice> getConfigExtraPricess() {
//		return configExtraPricess;
//	}
//
//	public void setConfigExtraPricess(List<ConfigExtraPrice> configExtraPricess) {
//		this.configExtraPricess = configExtraPricess;
//	}

	

}