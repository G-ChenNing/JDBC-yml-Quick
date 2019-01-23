package tech.xiangcheng.orangebus.order.domain.enumType;


/**
 * 所有配置状态
 * @author liuhuan
 */
public enum ConfigEnumInteger {
	
	
	;
	
	
	private int value;
	private String name;
	private String cssTextClass;
	private String cssBgClass;
	
	private ConfigEnumInteger(int value,String name,String cssTextClass,String cssBgClass){
		this.value = value;
		this.name = name;
		this.cssTextClass = cssTextClass;
		this.cssBgClass = cssBgClass;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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


	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}

	
	
}
