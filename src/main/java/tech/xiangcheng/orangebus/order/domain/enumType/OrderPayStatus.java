package tech.xiangcheng.orangebus.order.domain.enumType;

/**
 * 订单支付状态
 * 
 * @author liuhuan
 */
public enum OrderPayStatus {

	UNPAID("0", "未支付", "", ""), PAID("1", "已支付", "", ""), CANCEL("2", "订单取消", "", ""), REFUND_SUBMIT("3", "退款已提交", "",
			""), REFUND_COMPLETE("4", "退款已完成", "", ""), COMPLETE_CHANGED("5", "改签完成", "", ""), SIGN_CHANGING("6", "改签中",
					"", ""), CHANGE_UNPAID("7", "改签待支付", "",
							""), CHANGE_REFUND("8", "改签待退款", "", ""), CHANGE_CANCEL("9", "取消改签", "", "");
	private String value;
	private String name;
	private String cssTextClass;
	private String cssBgClass;

	private OrderPayStatus(String value, String name, String cssTextClass, String cssBgClass) {
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
