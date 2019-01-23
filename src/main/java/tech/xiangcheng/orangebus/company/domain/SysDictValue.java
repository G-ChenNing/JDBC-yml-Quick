package tech.xiangcheng.orangebus.company.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;
@Entity
public class SysDictValue extends DataEntity{
	@Id
	private String id;

	//private String dictTypeId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "dict_type_id")
	@JsonBackReference
	private SysDictType sysDictType;
	
	private String label;
	private String value;
	private String sort;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	public SysDictType getSysDictType() {
		return sysDictType;
	}
	public void setSysDictType(SysDictType sysDictType) {
		this.sysDictType = sysDictType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	
	
	
}
