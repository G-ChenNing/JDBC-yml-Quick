/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package tech.xiangcheng.orangebus.config.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 验票状态配置Entity
 * @author kyle
 * @version 2017-04-25
 */
@Entity
public class ConfigCheckStatus extends DataEntity {
	
	@Id
	protected String id;
	
	private String name;		// 名称
	
	public ConfigCheckStatus() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}