package tech.xiangcheng.orangebus.config.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;
@Entity
public class ConfigSpendName extends DataEntity {
	@Id
	private String id;
	private String name;
	private String enName;
	private Integer sort;
	@ManyToOne(optional = false)
	@JoinColumn(name = "type_id")
	@JsonBackReference
	private ConfigSpendType ConfigSpendType;
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
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public ConfigSpendType getConfigSpendType() {
		return ConfigSpendType;
	}
	public void setConfigSpendType(ConfigSpendType configSpendType) {
		ConfigSpendType = configSpendType;
	}
	
	
	
	
	
}
