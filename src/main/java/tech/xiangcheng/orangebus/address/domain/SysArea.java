package tech.xiangcheng.orangebus.address.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.company.domain.Station;
import tech.xiangcheng.orangebus.company.domain.SysOffice;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;


@Entity
public class SysArea extends DataEntity {

	@Id
	String id;
	String parentId;
	String parentIds;
	String name;
	BigDecimal sort;
	String code;
	String type;
	
	
	
	
	@OneToMany(mappedBy = "sysArea",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<SysOffice> sysOffices = new ArrayList<SysOffice>();


	@OneToMany(mappedBy = "sysArea",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<Station> stations = new ArrayList<Station>();


	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getParentId() {
		return parentId;
	}




	public void setParentId(String parentId) {
		this.parentId = parentId;
	}




	public String getParentIds() {
		return parentIds;
	}




	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public BigDecimal getSort() {
		return sort;
	}




	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}




	public String getCode() {
		return code;
	}




	public void setCode(String code) {
		this.code = code;
	}




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public List<SysOffice> getSysOffices() {
		return sysOffices;
	}




	public void setSysOffices(List<SysOffice> sysOffices) {
		this.sysOffices = sysOffices;
	}
	
}
