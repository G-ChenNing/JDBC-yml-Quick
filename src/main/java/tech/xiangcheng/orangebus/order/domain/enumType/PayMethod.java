package tech.xiangcheng.orangebus.order.domain.enumType;


/**
 * 支付方式
 * @author liuhuan
 */
public enum PayMethod {
	
	WEIXIN("1","微信支付","",""),
	ALIPAY("2","支付宝支付","",""),
	EBANK("3","站内余额支付","",""),
	QRCODE("4", "乘务员微信", "", ""),
	CASH("5","现金支付","",""),
	TICKET("6","票据","",""),
	PRESALE("7", "预售", "", "");
	private String value;
	private String text;
	private String cssTextClass;
	private String cssBgClass;
	
	private PayMethod(String value,String text,String cssTextClass,String cssBgClass){
		this.value = value;
		this.text = text;
		this.cssTextClass = cssTextClass;
		this.cssBgClass = cssBgClass;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

}
