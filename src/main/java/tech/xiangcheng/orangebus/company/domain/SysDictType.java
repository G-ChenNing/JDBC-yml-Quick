package tech.xiangcheng.orangebus.company.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;

@Entity
public class SysDictType extends DataEntity{
	@Id
	private String id;
	private String type;
	private String description;
	
	@OneToMany(mappedBy = "sysDictType",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JsonManagedReference
	protected List<SysDictValue> sysDictValue = new ArrayList<SysDictValue>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SysDictValue> getSysDictValue() {
		return sysDictValue;
	}

	public void setSysDictValue(List<SysDictValue> sysDictValue) {
		this.sysDictValue = sysDictValue;
	}
	
	
	
	
	
}
