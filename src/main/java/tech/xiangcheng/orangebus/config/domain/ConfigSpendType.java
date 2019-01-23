package tech.xiangcheng.orangebus.config.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;
@Entity
public class ConfigSpendType extends DataEntity {
	@Id
	private String id;
	private String name;
	private String enName;
	private Integer sort;
	
//	@OneToMany(mappedBy = "ConfigSpendType",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
//	@JsonManagedReference
//	protected List<ConfigSpendName> configSpendNames = new ArrayList<ConfigSpendName>();

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

	public String getEn_name() {
		return enName;
	}

	public void setEn_name(String enName) {
		this.enName = enName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

//	public List<ConfigSpendName> getConfigSpendNames() {
//		return configSpendNames;
//	}
//
//	public void setConfigSpendNames(List<ConfigSpendName> configSpendNames) {
//		this.configSpendNames = configSpendNames;
//	}
	
	
	
}
