package tech.xiangcheng.orangebus.account.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.company.domain.CheckAccountStationCoach;
import tech.xiangcheng.orangebus.company.domain.SysOffice;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

@Entity
public class CheckAccount  extends DataEntity
{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;


	protected String name;//登陆用户名
	private String password;//密码
	private String phone;
	String realName;
	private String role;
	@ManyToOne(targetEntity = SysOffice.class)
	@JoinColumn(name = "OFFICE_ID")
	protected SysOffice sysOffice;
	
	@OneToMany(mappedBy = "checkAccount",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<CheckAccountStationCoach> checkAccountStationCoach =new ArrayList<CheckAccountStationCoach>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public List<CheckAccountStationCoach> getCheckAccountStationCoach() {
		return checkAccountStationCoach;
	}

	public void setCheckAccountStationCoach(List<CheckAccountStationCoach> checkAccountStationCoach) {
		this.checkAccountStationCoach = checkAccountStationCoach;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SysOffice getSysOffice() {
		return sysOffice;
	}

	public void setSysOffice(SysOffice sysOffice) {
		this.sysOffice = sysOffice;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
