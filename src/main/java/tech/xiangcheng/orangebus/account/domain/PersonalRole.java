package tech.xiangcheng.orangebus.account.domain;

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
public class PersonalRole extends DataEntity {

	public PersonalRole() {
	}

	@Id
	protected String id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "SYS_ROLE_ID")
	@JsonBackReference 
	protected SysRole sysRole;

	@ManyToOne(optional = false)
	@JoinColumn(name = "PERSONAL_ACCOUNT_ID")
	@JsonBackReference 
	protected PersonalAccount personalAccount;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public SysRole getSysRole() {
		return sysRole;
	}


	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}


	public PersonalAccount getPersonalAccount() {
		return personalAccount;
	}


	public void setPersonalAccount(PersonalAccount personalAccount) {
		this.personalAccount = personalAccount;
	}

	
	
}