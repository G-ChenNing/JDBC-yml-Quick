package tech.xiangcheng.orangebus.company.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.account.domain.CheckAccount;
import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

@Entity
public class SysOffice extends DataEntity {
	@Id
	protected String id;
	String parentId;//父级编号
    String parentIds;//所有父级编号
	String name;
	BigDecimal sort;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "AREA_ID")
	@JsonBackReference
	SysArea sysArea;
	
	@OneToMany(mappedBy = "sysOffice", targetEntity = CheckAccount.class)
//	@JsonManagedReference
	protected List<CheckAccount> checkAccounts =new ArrayList<>();
	
	String code;//区域编码
	String type;//机构类型
	String grade;//机构等级
	String address;//联系地址
	String zipCode;//邮政编码
	String master;//负责人
	String phone;//电话
	String fax;//传真
	String email;//邮箱
	
	/**
	 * 公司的所有车次
	 */
	@OneToMany(mappedBy = "sysOffice",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<CoachNumber> coachNubmers = new ArrayList<CoachNumber>();
	
	/**
	 * 公司的车辆
	 */
	@OneToMany(mappedBy = "sysOffice",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<Vehicle> vehicles = new ArrayList<Vehicle>();


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public SysArea getSysArea() {
		return sysArea;
	}

	public void setSysArea(SysArea sysArea) {
		this.sysArea = sysArea;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public List<CoachNumber> getCoachNubmers() {
		return coachNubmers;
	}

	public void setCoachNubmers(List<CoachNumber> coachNubmers) {
		this.coachNubmers = coachNubmers;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public List<CheckAccount> getCheckAccounts() {
		return checkAccounts;
	}

	public void setCheckAccounts(List<CheckAccount> checkAccounts) {
		this.checkAccounts = checkAccounts;
	}

	public BigDecimal getSort() {
		return sort;
	}

	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	
	
}