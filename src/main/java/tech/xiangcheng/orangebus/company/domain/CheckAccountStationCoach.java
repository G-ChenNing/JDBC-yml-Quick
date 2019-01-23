package tech.xiangcheng.orangebus.company.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.account.domain.CheckAccount;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * @author 
 *
 */
@Entity
public class CheckAccountStationCoach extends DataEntity {

	public CheckAccountStationCoach() {
	}

	@Id
	protected String id;
	
//	@ManyToOne(optional = false)
//	@JoinColumn(name = "COACH_NUMBER_ID")
//	@JsonBackReference 
//	protected CoachNumber coachNumber;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "VEHICLE_ID")
	@JsonBackReference 
	protected Vehicle vehicle;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CHECK_ACCOUNT_ID")
	@JsonBackReference 
	protected CheckAccount checkAccount;

	protected int role;//角色，主要针对用户订单上面显示的乘务员电话是定位那个checkaccount，参照ConfigEnum中的CAS_MAIN_CHECK


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


//	public CoachNumber getCoachNumber() {
//		return coachNumber;
//	}
//
//
//	public void setCoachNumber(CoachNumber coachNumber) {
//		this.coachNumber = coachNumber;
//	}


	public CheckAccount getCheckAccount() {
		return checkAccount;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}


	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


	public void setCheckAccount(CheckAccount checkAccount) {
		this.checkAccount = checkAccount;
	}


	public int getRole() {
		return role;
	}


	public void setRole(int role) {
		this.role = role;
	}

	
}