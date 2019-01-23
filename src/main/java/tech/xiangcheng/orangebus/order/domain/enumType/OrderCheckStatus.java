package tech.xiangcheng.orangebus.order.domain.enumType;

public enum OrderCheckStatus {

	UNCHECK("0","未验票","",""),
	CHECK("1","已验票","","");
	
	
	private String value;
	private String name;
	private String cssTextClass;
	private String cssBgClass;
	
	
	private OrderCheckStatus(String value, String name, String cssTextClass, String cssBgClass) {
		this.value = value;
		this.name = name;
		this.cssTextClass = cssTextClass;
		this.cssBgClass = cssBgClass;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCssTextClass() {
		return cssTextClass;
	}
	public void setCssTextClass(String cssTextClass) {
		this.cssTextClass = cssTextClass;
	}
	public String getCssBgClass() {
		return cssBgClass;
	}
	public void setCssBgClass(String cssBgClass) {
		this.cssBgClass = cssBgClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
