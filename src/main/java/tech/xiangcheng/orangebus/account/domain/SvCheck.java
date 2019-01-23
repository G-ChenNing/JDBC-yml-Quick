/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package tech.xiangcheng.orangebus.account.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;

/**
 * 乘务员的配置Entity
 * @version 2018-02-27
 */
@Entity
public class SvCheck{
	@Id
	protected String id;
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "scheduleVehicleId")
	@JsonBackReference
	private ScheduleVehicle scheduleVehicle;		// 车次配置id
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "checkAccountId")
	@JsonBackReference
	private CheckAccount checkAccount;		// 乘务员配置id
	private Integer role;		// 权限
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "busScheduleId")
	@JsonBackReference
	private BusSchedule busSchedule;
	
	public SvCheck() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ScheduleVehicle getScheduleVehicle() {
		return scheduleVehicle;
	}

	public void setScheduleVehicle(ScheduleVehicle scheduleVehicle) {
		this.scheduleVehicle = scheduleVehicle;
	}

	public CheckAccount getCheckAccount() {
		return checkAccount;
	}

	public void setCheckAccount(CheckAccount checkAccount) {
		this.checkAccount = checkAccount;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public BusSchedule getBusSchedule() {
		return busSchedule;
	}

	public void setBusSchedule(BusSchedule busSchedule) {
		this.busSchedule = busSchedule;
	}

}