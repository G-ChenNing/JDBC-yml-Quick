/**
 * 
 */
package tech.xiangcheng.orangebus.company.domain;

import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.leaf.util.json.JsonUtil;

/**
 * 包装scheduleVehicle
 * @author yang
 *
 */
public class VehicleJson {
	String vehicleId;
	
	String vehicleName;
	int seatNum;
	String color;
	String services;
	String imgUrl;//车辆图片
	String sysOfficeId;
	String sysOfficeName;
	
//	String typeId;//车辆类型
//	String brand;//品牌
//	String model;//型号
	String licenceNum;//车牌号码
	{
		vehicleId = "";
		vehicleName = "";
		seatNum = 0;
		color = "";
		services = "";
		imgUrl="";
		sysOfficeId="";
		sysOfficeName="";
	}
	public VehicleJson(Vehicle vehicle) {
		if (null == vehicle) return;
		vehicleId = vehicle.getId() == null ? "" : vehicle.getId();
		vehicleName = vehicle.getName() == null ? "" : vehicle.getName();
		seatNum = vehicle.getSeatNum();
		services = vehicle.getServices() == null ? "" : vehicle.getServices();
		imgUrl= vehicle.getImgUrl() == null ? "" : vehicle.getImgUrl();
		color = vehicle.getColor() == null ? "" : vehicle.getColor();
		sysOfficeId=vehicle.getSysOffice().getId()==null?"":vehicle.getSysOffice().getId();
		sysOfficeName=vehicle.getSysOffice().getName()==null?"":vehicle.getSysOffice().getName();
		licenceNum = vehicle.getLicenceNum() == null?"":vehicle.getLicenceNum();
 	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleName() {
		return vehicleName;
	}
	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}
	public int getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getSysOfficeId() {
		return sysOfficeId;
	}
	public void setSysOfficeId(String sysOfficeId) {
		this.sysOfficeId = sysOfficeId;
	}
	public String getSysOfficeName() {
		return sysOfficeName;
	}
	public void setSysOfficeName(String sysOfficeName) {
		this.sysOfficeName = sysOfficeName;
	}
	public String getLicenceNum() {
		return licenceNum;
	}
	public void setLicenceNum(String licenceNum) {
		this.licenceNum = licenceNum;
	}

	
}
