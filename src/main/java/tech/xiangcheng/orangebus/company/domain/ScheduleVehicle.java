/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
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

import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.order.domain.PreSale;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 班次配置车辆Entity
 * 
 * @author kyle
 * @version 2017-04-26
 */
@Entity
public class ScheduleVehicle extends DataEntity {

	@Id
	protected String id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "BUS_SCHEDULE_ID")
	@JsonBackReference
	protected BusSchedule busSchedule; // 班次

	@ManyToOne(optional = false)
	@JoinColumn(name = "vehicle_id")
	@JsonBackReference
	protected Vehicle vehicle; // 车辆
	protected Double addPrice; // 原价格额外的加价

	@OneToMany(mappedBy = "scheduleVehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	protected List<PreSale> preSaleList = new ArrayList<PreSale>();

	/**
	 * 该班次所有订单
	 */
	@OneToMany(mappedBy = "scheduleVehicle", cascade = CascadeType.ALL)
	@JsonManagedReference
	protected List<PassengerBusOrder> passengerBusOrders = new ArrayList<PassengerBusOrder>();

	protected Integer driverConfirm; // 司机确认
	protected String combinedSvId; //合并的目标sv的id
	
	public ScheduleVehicle() {
	}

	public Integer getDriverConfirm() {
		return driverConfirm;
	}

	public void setDriverConfirm(Integer driverConfirm) {
		this.driverConfirm = driverConfirm;
	}

	public ScheduleVehicle(String id) {
		id = this.id;
	}

	public BusSchedule getBusSchedule() {
		return busSchedule;
	}

	public void setBusSchedule(BusSchedule busSchedule) {
		this.busSchedule = busSchedule;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Double getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(Double addPrice) {
		this.addPrice = addPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PreSale> getPreSaleList() {
		return preSaleList;
	}

	public void setPreSaleList(List<PreSale> preSaleList) {
		this.preSaleList = preSaleList;
	}

	public List<PassengerBusOrder> getPassengerBusOrders() {
		return passengerBusOrders;
	}

	public void setPassengerBusOrders(List<PassengerBusOrder> passengerBusOrders) {
		this.passengerBusOrders = passengerBusOrders;
	}

	public String getCombinedSvId() {
		return combinedSvId;
	}

	public void setCombinedSvId(String combinedSvId) {
		this.combinedSvId = combinedSvId;
	}

}