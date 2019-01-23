package tech.xiangcheng.orangebus.account.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.company.domain.SysOffice;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;
@Entity
public class SysRole extends DataEntity{

	@Id
	private String id;
    private String name;
    
//	@ManyToOne(targetEntity = SysOffice.class)
    @ManyToOne
	@JoinColumn(name = "OFFICE_ID")
    @JsonBackReference
	protected SysOffice sysOffice;
	
	private String enname;
	private String roleType;
	private String data_scope;
	private String is_sys;
	private String useable;
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
	public SysOffice getSysOffice() {
		return sysOffice;
	}
	public void setSysOffice(SysOffice sysOffice) {
		this.sysOffice = sysOffice;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getData_scope() {
		return data_scope;
	}
	public void setData_scope(String data_scope) {
		this.data_scope = data_scope;
	}
	public String getIs_sys() {
		return is_sys;
	}
	public void setIs_sys(String is_sys) {
		this.is_sys = is_sys;
	}
	public String getUseable() {
		return useable;
	}
	public void setUseable(String useable) {
		this.useable = useable;
	}

	

}
