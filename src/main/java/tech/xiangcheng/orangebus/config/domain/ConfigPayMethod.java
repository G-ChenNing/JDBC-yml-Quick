/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package tech.xiangcheng.orangebus.config.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 支付方式配置Entity
 * @author kyle
 * @version 2017-04-25
 */
@Entity
public class ConfigPayMethod extends DataEntity{
	
	private String name;		// 支付名称
	
	public ConfigPayMethod() {
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Id
	protected String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}