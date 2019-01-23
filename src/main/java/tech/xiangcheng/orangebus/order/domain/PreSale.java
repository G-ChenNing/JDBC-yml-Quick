package tech.xiangcheng.orangebus.order.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.StationCoachNumber;
import tech.xiangcheng.orangebus.config.domain.ConfigSource;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

@Entity(name="pre_sale")
public class PreSale extends DataEntity {
	@Id
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "SOURCE_ID")
	@JsonBackReference
	private ConfigSource configSource;
	@ManyToOne
	@JoinColumn(name = "STATION_COACH_NUMBER_ID")
	@JsonBackReference
	private StationCoachNumber stationCoachNumber;
	@ManyToOne
	@JoinColumn(name = "BUS_SCHEDULE_ID")
	private BusSchedule busSchedule;
	@ManyToOne
	@JoinColumn(name = "SCHEDULE_VEHICLE_ID")
	private ScheduleVehicle scheduleVehicle;
	
	private String name;
	private String phone;
	private String service;
	private Integer amount;
	private Double total;
	private Integer status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ConfigSource getConfigSource() {
		return configSource;
	}
	public void setConfigSource(ConfigSource configSource) {
		this.configSource = configSource;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	
	public StationCoachNumber getStationCoachNumber() {
		return stationCoachNumber;
	}
	public void setStationCoachNumber(StationCoachNumber stationCoachNumber) {
		this.stationCoachNumber = stationCoachNumber;
	}
	/**
	 * @throws IllegalStateException 只有已持久化对象可调用该方法
	 * @return
	 */
	public String toJson() {
		if (null == busSchedule || null == configSource || null == scheduleVehicle) {
			throw new IllegalStateException("只能对持久化对象调用该方法");
		}
		JsonObj jo = JsonObj.instance();
		jo.putProperty("id", id)
			.putProperty("service", service)
			.putProperty("amount", amount)
			.putProperty("total", total)
			.putProperty("source", configSource.getName())
			.putProperty("phone", phone)
			.putProperty("status", status)
			.putProperty("stationCoachNumberId", stationCoachNumber.getId())
			.putProperty("busScheduleId", busSchedule.getId())
			.putProperty("scheduleVehicleId", scheduleVehicle.getId());
		return jo.toJson();
	}
	
}
