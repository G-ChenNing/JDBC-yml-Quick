package tech.xiangcheng.orangebus.company.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.order.domain.PreSale;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 班次
 * 
 *
 */
@Entity
public class BusSchedule extends DataEntity {

	public BusSchedule() {
	}
	public BusSchedule(String id) {
		id=this.id;
	}

	@Id
	protected String id;
	
	String code;

	/**
	 * 该班次的所属车次
	 */
	@ManyToOne(optional = false)
	@JoinColumn(name = "COACH_NUMBER_ID")
	@JsonBackReference 
	protected CoachNumber coachNumber;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	protected Date departureTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	protected Date arrivalTime;

	Integer limitTicketNum;
	
	@OneToMany(mappedBy = "busSchedule",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<ScheduleVehicle> scheduleVehicle =new ArrayList<ScheduleVehicle>();

	@OneToMany(mappedBy = "busSchedule",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<PreSale> preSaleList =new ArrayList<PreSale>();
	
	/**
	 * 该班次所有订单
	 */
	@OneToMany(mappedBy = "busSchedule", cascade = CascadeType.ALL)
	@JsonManagedReference
	protected List<PassengerBusOrder> passengerBusOrders = new ArrayList<PassengerBusOrder>();
	

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public CoachNumber getCoachNumber() {
		return coachNumber;
	}

	public void setCoachNumber(CoachNumber coachNumber) {
		this.coachNumber = coachNumber;
	}

	public List<PassengerBusOrder> getPassengerBusOrders() {
		return passengerBusOrders;
	}

	public void setPassengerBusOrders(List<PassengerBusOrder> passengerBusOrders) {
		this.passengerBusOrders = passengerBusOrders;
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

	public Integer getLimitTicketNum() {
		return limitTicketNum;
	}

	public void setLimitTicketNum(Integer limitTicketNum) {
		this.limitTicketNum = limitTicketNum;
	}

	public List<ScheduleVehicle> getScheduleVehicle() {
		return scheduleVehicle;
	}

	public void setScheduleVehicle(List<ScheduleVehicle> scheduleVehicle) {
		this.scheduleVehicle = scheduleVehicle;
	}

	public List<PreSale> getPreSaleList() {
		return preSaleList;
	}

	public void setPreSaleList(List<PreSale> preSaleList) {
		this.preSaleList = preSaleList;
	}



	

}