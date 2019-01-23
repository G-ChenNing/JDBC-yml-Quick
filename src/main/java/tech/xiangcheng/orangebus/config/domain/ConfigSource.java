package tech.xiangcheng.orangebus.config.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;
/**
 * 预售源
 * @author yang
 *
 */
@Entity
public class ConfigSource extends DataEntity {
	@Id
	private String id;
	private String name;		// 支付名称
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
	
	
}
