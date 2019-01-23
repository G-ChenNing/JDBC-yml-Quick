package tech.xiangcheng.orangebus.account.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * 代金券明细Entity
 * @author kyle
 * @version 2017-05-15
 */
@Entity
public class MemberLevel extends DataEntity {
	
	@Id
	protected String id;
	
	protected String name;

	protected int upgradeCredit;
	
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

	public int getUpgradeCredit() {
		return upgradeCredit;
	}

	public void setUpgradeCredit(int upgradeCredit) {
		this.upgradeCredit = upgradeCredit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + upgradeCredit;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberLevel other = (MemberLevel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (upgradeCredit != other.upgradeCredit)
			return false;
		return true;
	}

	
	
	
}