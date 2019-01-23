package tech.xiangcheng.orangebus.account.domain;

public class DetailPersonalAccountJson {
	String level;
	public DetailPersonalAccountJson(PersonalAccount account) {
		level = account.getMemberLevel().getName();
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
