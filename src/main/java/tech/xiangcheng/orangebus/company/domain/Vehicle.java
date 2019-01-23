package tech.xiangcheng.orangebus.company.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;
@Entity

public class Vehicle extends DataEntity {

	//注意 该表的作用变成，车辆类型表，原vehicle_type改成车辆大类型表
	//用户下订单时选取车辆类型是在该表选取，后期需要增加车辆表，故删除车牌号码，品牌，信号，和车辆颜色，并将座位数改成string
	public Vehicle() {
	}

	@Id
	protected String id;
	
	String name;//车辆名字


	@ManyToOne(optional = false)
	@JoinColumn(name = "office_id")
	@JsonBackReference
	protected SysOffice sysOffice;
	
	String typeId;//车辆类型
	String services;//车辆服务
	String imgUrl;//车辆图片
	int seatNum;//座位数
	String brand;//品牌
	String model;//型号
	String color;//车辆颜色
	String licenceNum;//车牌号码

	@OneToMany(mappedBy = "vehicle",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<CheckAccountStationCoach> checkAccountStationCoach =new ArrayList<CheckAccountStationCoach>();



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}


	public SysOffice getSysOffice() {
		return sysOffice;
	}

	public void setSysOffice(SysOffice sysOffice) {
		this.sysOffice = sysOffice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}


	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLicenceNum() {
		return licenceNum;
	}

	public void setLicenceNum(String licenceNum) {
		this.licenceNum = licenceNum;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<CheckAccountStationCoach> getCheckAccountStationCoach() {
		return checkAccountStationCoach;
	}

	public void setCheckAccountStationCoach(List<CheckAccountStationCoach> checkAccountStationCoach) {
		this.checkAccountStationCoach = checkAccountStationCoach;
	}

	

}