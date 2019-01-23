/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package tech.xiangcheng.orangebus.config.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 退款规则Entity
 * @author kyle
 * @version 2017-05-15
 */
@Entity
public class ConfigRefund extends DataEntity {
	
	private Double timeLimit;		// 提前多久时间内(小时)
	private Double percent;		// 退回百分比
	
	public ConfigRefund() {
	}


	public Double getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Double timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
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