package tech.xiangcheng.orangebus.company.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;


@Entity
public class ScheduleVehicleSpend extends DataEntity {
	
	@Id
	protected String id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BUS_SCHEDULE_ID")
	@JsonBackReference
	protected BusSchedule busSchedule;		// 班次
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "schedule_vehicle_id")
	@JsonBackReference
	protected ScheduleVehicle scheduleVehicle;		// 班次配置车辆
	
	protected String name;		//名称
	protected Double price;		// 价
	
	public ScheduleVehicleSpend() {
	}

	public ScheduleVehicleSpend(BusSchedule busSchedule,ScheduleVehicle scheduleVehicle, String name, Double price, String remarks) {
		super();
		this.busSchedule=busSchedule;
		this.scheduleVehicle=scheduleVehicle;
		this.name=name;
		this.price=price;
		this.remarks=remarks;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BusSchedule getBusSchedule() {
		return busSchedule;
	}

	public void setBusSchedule(BusSchedule busSchedule) {
		this.busSchedule = busSchedule;
	}

	public ScheduleVehicle getScheduleVehicle() {
		return scheduleVehicle;
	}

	public void setScheduleVehicle(ScheduleVehicle scheduleVehicle) {
		this.scheduleVehicle = scheduleVehicle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	
	
}