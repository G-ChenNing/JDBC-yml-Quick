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

import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;


/**
 * 客企车站
 *
 */
@Entity
public class Station extends DataEntity {

	public Station() {
	}

	@Id
	protected String id;

	String code;
	
	protected String name;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "AREA_ID")
	@JsonBackReference
	SysArea sysArea;

	String detail;
//	@OneToMany(mappedBy = "originalStation",fetch = FetchType.LAZY,cascade=CascadeType.REMOVE)
//	@JsonManagedReference
//	protected List<CoachNumber> originalCoachNumbers = new ArrayList<CoachNumber>();
//	
//	@OneToMany(mappedBy = "terminalStation",fetch = FetchType.LAZY,cascade=CascadeType.REMOVE)
//	@JsonManagedReference
//	protected List<CoachNumber> terminalCoachNumbers = new ArrayList<CoachNumber>();
	
	@OneToMany(mappedBy = "station",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<StationCoachNumber> stationCoachNumbers =new ArrayList<StationCoachNumber>();


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StationCoachNumber> getStationCoachNumbers() {
		return stationCoachNumbers;
	}

	public void setStationCoachNumbers(List<StationCoachNumber> stationCoachNumbers) {
		this.stationCoachNumbers = stationCoachNumbers;
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

	public SysArea getSysArea() {
		return sysArea;
	}

	public void setSysArea(SysArea sysArea) {
		this.sysArea = sysArea;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	

}