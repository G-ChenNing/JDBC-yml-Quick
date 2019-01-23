package tech.xiangcheng.orangebus.account.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.company.domain.CheckAccountStationCoach;
import tech.xiangcheng.orangebus.config.domain.ConfigPayMethod;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;
/**
 * 不提供默认构造函数，只用一个有參构造函数，其中参数为必填
 * @author yang
 *
 */
@Entity
public class PersonalAccount extends DataEntity {

	
	@Id
	private String id;
	protected String name;
	private String password;
	private String phone;
	private String email;
	protected String code;
	private String accountType;
	private Double points;
//	private String accountLevel;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "accountLevel")
	@JsonBackReference
	private MemberLevel memberLevel;		// 会员等级
	
	private String certified;
	private String safefyLevel = "0";
	private String roles;

	@OneToMany(mappedBy = "personalAccount",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<PersonalRole> personalRole =new ArrayList<PersonalRole>();

	@SuppressWarnings("unused")
	private PersonalAccount() {
	}
	
	public PersonalAccount(String phone, String accountLevel) {
		super();
		this.phone = phone;
		MemberLevel memberLevel = new MemberLevel();
		memberLevel.setId(accountLevel);
		this.memberLevel=memberLevel;
	}

	@OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL
			)
	@JsonManagedReference
	List<PassengerBusOrder> passengerBusOrders=new ArrayList<>() ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public List<PassengerBusOrder> getPassengerBusOrders() {
		return passengerBusOrders;
	}

	public void setPassengerBusOrders(List<PassengerBusOrder> passengerBusOrders) {
		this.passengerBusOrders = passengerBusOrders;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public MemberLevel getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return password;
	}

	public String getCertified() {
		return certified;
	}

	public void setCertified(String certified) {
		this.certified = certified;
	}

	public String getSafefyLevel() {
		return safefyLevel;
	}

	public void setSafefyLevel(String safefyLevel) {
		this.safefyLevel = safefyLevel;
	}

	public List<PersonalRole> getPersonalRole() {
		return personalRole;
	}

	public void setPersonalRole(List<PersonalRole> personalRole) {
		this.personalRole = personalRole;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}



//	@Override
//	public String toString() {
//		return "PersonalAccount [id=" + id + ", name=" + name + ", password=" + password + ", phone=" + phone
//				+ ", email=" + email + ", code=" + code + ", accountType=" + accountType + ", accountLevel="
//				+ accountLevel + ", certified=" + certified + ", safefyLevel=" + safefyLevel + ", roles=" + roles
//				+ ", personalRole=" + personalRole + ", passengerBusOrders=" + passengerBusOrders + "]";
//	}
	
	
}
