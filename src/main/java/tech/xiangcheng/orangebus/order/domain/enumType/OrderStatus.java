//package tech.xiangcheng.orangebus.order.domain.enumType;
//
//
///**
// * 订单状态
// * @author liuhuan
// */
//public enum OrderStatus {
//	
//	All("0","全部状态","","",""),//未设置订单状态，此状态用于在查询时，设置包含全部订单的条件
//	CREATED("1","订单创建成功","","","fa-check"),
//	CANCELED("2","订单已取消","","","fa-rotate-left"),
//	EXPIRES("3","订单已失效","","","fa-times"),
//	FULFILLED("4","订单已完成","","","fa-flag");
//	private String value;
//	private String text;
//	private String cssTextClass;
//	private String cssBgClass;
//	private String cssIcon;
//	
//	public String getCssIcon() {
//		return cssIcon;
//	}
//
//	public void setCssIcon(String cssIcon) {
//		this.cssIcon = cssIcon;
//	}
//
//	private OrderStatus(String value,String text,String cssTextClass,String cssBgClass,String cssIcon){
//		this.value = value;
//		this.text = text;
//		this.cssTextClass = cssTextClass;
//		this.cssBgClass = cssBgClass;
//		this.cssIcon = cssIcon;
//	}
//
//	public String getText() {
//		return text;
//	}
//
//	public void setText(String text) {
//		this.text = text;
//	}
//
//	public String getValue() {
//		return value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}
//
//	public String getCssTextClass() {
//		return cssTextClass;
//	}
//
//	public void setCssTextClass(String cssTextClass) {
//		this.cssTextClass = cssTextClass;
//	}
//
//	public String getCssBgClass() {
//		return cssBgClass;
//	}
//
//	public void setCssBgClass(String cssBgClass) {
//		this.cssBgClass = cssBgClass;
//	}
//
//}
