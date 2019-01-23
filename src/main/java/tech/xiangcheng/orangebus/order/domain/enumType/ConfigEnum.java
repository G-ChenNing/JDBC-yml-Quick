package tech.xiangcheng.orangebus.order.domain.enumType;


/**
 * 所有配置状态
 * @author liuhuan
 */
public enum ConfigEnum {
	//车站类型配置，表：config_station_on_off
	DEPART("0","出发站","",""),GETON("1","上车站","",""),GETOFF("2","下车站","",""),TERMINAL("3","终点站","",""),
	//车辆类型配置，表：vehicle_type
	GRAND("3","大型车","",""),MEDIUM("2","中型车","",""),SMALL("1","小型客车","",""),
	//地区类型，表：sys_area类型type
	COUNTRY("1","国家","",""),PROVINCE("2","省","",""),CITY("3","城市","",""),
	//用户等级，表：member_level，对应personal_account表的account_level字段
	MEMBERZERO("0","青铜","","")//未注册显示青铜的价格，为原价
	,MEMBERONE("1","白银","",""),//注册立即升为白银，在AccountHandler中搜ConfigEnum.MEMBERONE.getValue()
	MEMBERTWO("2","黄金","",""),//老用户是黄金，在SmsHander中搜ConfigEnum.MEMBERTWO.getValue()，黄金用户发的短信不一样
	MEMBERTHREE("3","铂金","",""),MEMBERFOUR("4","黑金","",""),MEMBERFIVE("5","钻石","",""),//这三个暂时未用到
	MEMBERADMIN("100","管理员","",""),
	//来源
	OUTSIDE("1","外源","", ""),
	PEER("2", "同行", "", ""),
	INDIVIDUAL("3", "散客", "", ""),
	PHONE_CALL("4", "电话", "", ""),
	STATION("5", "车站", "", ""),
	OTHERS("6", "其他", "", ""),
	
	//pre_sale 预售 status    0预售成功,1已上车,2已取消
	pre_sale_successful("0","已预约","", ""),
	pre_sale_aboard("1","已上车","", ""),
	pre_sale_cancel("2","取消","", ""),
	
	//CheckAccountStationCoach的role
	CAS_MAIN_CHECK("1","主乘务员","",""),
	
	//司机，乘务员
	SV_CHECK_MANAGER("0","管理员","",""),
	SV_CHECK_CHECK("1","乘务员","",""),
	SV_CHECK_DRIVER("2","司机","",""),
	SV_CHECK_SCHEDULE("3","调度员","",""),
	
	//check_salary 乘务员工资信息 type 0按月，1按天，2按班次
	CHECK_SALARY_MONTH("0","按月","",""),
	CHECK_SALARY_DAY("1","按天","",""),
	CHECK_SALARY_BUS("2","按班次","",""),
	
	//CheckAccount司机，乘务员
	CA_CHECK("1","乘务员","",""),
	CA_DRIVER("2","司机","",""),
	
	//platform_compay_pay_rule 平台客企提成规则 type 1按票数量固定提成 2按票价百分比提成 3按月支付 4按年支付 
	COMPAY_PAY_NUM("1","按票数量固定提成","",""),
	COMPAY_PAY_PERCENT("2","按票价百分比提成","",""),
	COMPAY_PAY_MONTH("3","按月支付","",""),
	COMPAY_PAY_YEAR("4","按年支付","","")
	;
	public static ConfigEnum getSourceById(String id) {
		switch (id) {
		case "1": return OUTSIDE;
		case "2": return PEER;
		case "3" : return INDIVIDUAL;
		case "4" : return PHONE_CALL;
		case "5" : return STATION;
		case "6" : return OTHERS;
		default : return OTHERS;
		}
	}
	public static String getAccountLevelStr (String id) {
		switch (id) {
		case "0" : return MEMBERZERO.name;
		case "1" : return MEMBERONE.name;
		case "2" : return MEMBERTWO.name;
		case "3" : return MEMBERTHREE.name;
		case "4" : return MEMBERFOUR.name;
		case "5" : return MEMBERFIVE.name;
		default : return "未定义";
		}
	}
	public static ConfigEnum getStatusById(Integer id) {
		switch (id) {
		case 0 : return pre_sale_successful;
		case 1: return pre_sale_aboard;
		case 2: return pre_sale_cancel;
		default : return pre_sale_cancel;
		}
	}
	
	private String value;
	private String name;
	private String cssTextClass;
	private String cssBgClass;
	
	private ConfigEnum(String value,String name,String cssTextClass,String cssBgClass){
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
