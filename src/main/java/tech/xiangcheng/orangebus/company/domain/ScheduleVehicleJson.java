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
public class ScheduleVehicleJson {
	String scheduleVehicleId;
	double addPrice;
	String vehicleId;
	String vehicleName;
//	String vehicleLicenseNum;
	String vehicleTypeId;//3 大型车；2 中型车；1 小型客车
	int seatNum;
//	String color;
	String services;
	String imgUrl;//车辆图片
	Integer scheduleVehicleRemainSeats;
	{
		
		scheduleVehicleId = "";
		addPrice = 0;
		vehicleId = "";
		vehicleName = "";
//		vehicleLicenseNum = "";
		vehicleTypeId = "";//3 大型车；2 中型车；1 小型客车
		seatNum = 0;
//		color = "";
		services = "";
		imgUrl="";
		scheduleVehicleRemainSeats=0;
	}
	public ScheduleVehicleJson(ScheduleVehicle sv) {
		if (null == sv) return;
		scheduleVehicleId = sv.getId() == null ? "" : sv.getId();
		addPrice = sv.getAddPrice() == null ? 0 : sv.getAddPrice().doubleValue();
		Vehicle vehicle = sv.getVehicle();
		if (null == vehicle) return;
		vehicleId = vehicle.getId() == null ? "" : vehicle.getId();
		vehicleName = vehicle.getName() == null ? "" : vehicle.getName();
//		vehicleLicenseNum = vehicle.getLicenceNum() == null ? "" : vehicle.getLicenceNum();
		vehicleTypeId = vehicle.getTypeId() == null ? "" : vehicle.getTypeId();
		seatNum = vehicle.getSeatNum();
//		color = vehicle.getColor() == null ? "" : vehicle.getColor();
		services = vehicle.getServices() == null ? "" : vehicle.getServices();
		imgUrl= vehicle.getImgUrl() == null ? "" : vehicle.getImgUrl();
 	}
	
	public ScheduleVehicleJson setRemainSeats(Integer scheduleVehicleRemainSeats) {
		this.scheduleVehicleRemainSeats = scheduleVehicleRemainSeats;
		return this;
	}

	public String getScheduleVehicleId() {
		return scheduleVehicleId;
	}

	public void setScheduleVehicleId(String scheduleVehicleId) {
		this.scheduleVehicleId = scheduleVehicleId;
	}

	public double getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(double addPrice) {
		this.addPrice = addPrice;
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


	public String getVehicleTypeId() {
		return vehicleTypeId;
	}

	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public static void main(String[] args) throws JsonProcessingException {
		ScheduleVehicle scheduleVehicle = new ScheduleVehicle();
//		scheduleVehicle.setVehicle(new Vehicle());
//		System.out.println(JsonUtil.getJsonStr(new ScheduleVehicleJson(scheduleVehicle)));
	}


	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getScheduleVehicleRemainSeats() {
		return scheduleVehicleRemainSeats;
	}

	public void setScheduleVehicleRemainSeats(Integer scheduleVehicleRemainSeats) {
		this.scheduleVehicleRemainSeats = scheduleVehicleRemainSeats;
	}


	
	
}
