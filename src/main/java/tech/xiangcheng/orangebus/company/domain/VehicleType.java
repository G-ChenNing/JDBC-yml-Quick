package tech.xiangcheng.orangebus.company.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;


@Entity
public class VehicleType extends DataEntity {
	@Id
	String id;
	String name;
	String code;
	String description;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
