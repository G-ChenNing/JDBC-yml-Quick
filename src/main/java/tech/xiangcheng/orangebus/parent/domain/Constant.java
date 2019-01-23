package tech.xiangcheng.orangebus.parent.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
	//表中的del_flag字段
	public static final String DELTED = "1";
	public static final String NOT_DELTED = "0";
	//
	public static final int SIGNINSEND=1;
	//接口返回的jason信息
	public static final String RETURN_CODE = "returnCode";
	public static final String RETURN_VALUE = "returnValue";
	//两种登陆方式的shiro权限信息
	public static final String PHONECODE_LOGIN_SHIRO_STRING="phoneCodeLogin";
	public static final String WECHAT_LOGIN_SHIRO_STRING="wechatLogin";

	//申请Token时使用
//	public static final String appId = "";
//	public static final String appSecret = "";
//	public  static final String TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&";
//	public static final String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
	
	// 微信支付使用
//	public static final String mch_id="";
//	public static final String body="香橙巴士-车票订单";
//	public static final String key="";
	
	public static final String busScheduletRemainSeats = 
			" SELECT CASE WHEN a > b THEN c - a ELSE c - b END AS remainSeats,id FROM (SELECT "+
			" (SELECT IFNULL(SUM(amount), 0) FROM passenger_bus_order WHERE bus_schedule_id = bs.id AND (pay_status = 1 OR (pay_status = 0 AND pay_method = 1 AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date) < ?))) AS a, "+ 
			" (SELECT IFNULL(SUM(amount), 0) FROM pre_sale WHERE bus_schedule_id = bs.id AND status != 2) + (SELECT IFNULL(SUM(amount), 0) FROM passenger_bus_order "+
			" WHERE bus_schedule_id = bs.id AND pay_method = 1 AND (pay_status = 1 OR (pay_status = 0 AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date) < ?)) AS b, "+
			" (SELECT ifnull(sum(v.seat_num),0) totalSeat   from vehicle v, schedule_vehicle sv where sv.vehicle_id = v.id and sv.bus_schedule_id = bs.id) AS c, "+
			" bs.id from bus_schedule bs where bs.id in (?)) t ";
	
	public static final String scheduleVehicleRemainSeats = 
			" SELECT CASE WHEN a > b THEN c - a ELSE c - b END AS remainSeats,id FROM (SELECT "+
			" (SELECT IFNULL(SUM(amount), 0) FROM passenger_bus_order WHERE schedule_vehicle_id = sv.id AND (pay_status = 1 OR (pay_status = 0 AND pay_method = 1 AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date) <?))) AS a, "+ 
			" (SELECT IFNULL(SUM(amount), 0) FROM pre_sale WHERE schedule_vehicle_id = sv.id AND status != 2 ) + (SELECT IFNULL(SUM(amount), 0) FROM passenger_bus_order "+
			" WHERE schedule_vehicle_id = sv.id AND pay_method = 1 AND (pay_status = 1 OR (pay_status = 0 AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date) <?))) AS b, "+ 
			" (SELECT IFNULL(seat_num, 0) FROM vehicle WHERE id = (ELECT vehicle_id FROM schedule_vehicle WHERE id = sv.id)) AS c,"+
			" sv.id from schedule_vehicle sv where sv.id in (?)) t ";
	
	public static final String scheduleVehicleRemainSeats2 = 
			" SELECT CASE WHEN a > b THEN c - a ELSE c - b END AS remain FROM (SELECT "+ 
			" (SELECT IFNULL(SUM(amount), 0) FROM passenger_bus_order HERE schedule_vehicle_id = ? AND (pay_status = 1 OR (pay_status = 0 AND pay_method = 1 AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date) < ?))) AS a, "+ 
			" (SELECT IFNULL(SUM(amount), 0) FROM pre_sale WHERE schedule_vehicle_id = ? AND status != 2) + (SELECT IFNULL(SUM(amount), 0) FROM passenger_bus_order "+ 
			" WHERE schedule_vehicle_id = ? AND pay_method = 1 AND (pay_status = 1 OR (pay_status = 0 AND UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(create_date) < ?))) AS b, "+
			" (SELECT IFNULL(seat_num, 0) FROM vehicle WHERE id = ( SELECT vehicle_id FROM schedule_vehicle WHERE id = ?)) AS c "+
			" ) t ";

	
	
	public static final String getNowDateYMDString(){
		Date dt=new Date();
	     SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
	     String date=  matter1.format(dt);
	     //Date today = matter1.parse(date);  
	     String dates="str_to_date('"+date+"', '%Y-%m-%d')";
	     return dates;
	}
	
	public static final Date getNowDateYMDDate() throws ParseException{
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");//因为new Date()包含小时分钟，要把这些去掉
		String date=  matter1.format(new Date());
		return matter1.parse(date);
	}
	//string格式如：2017-01-01
	public static final Date StringToDate(String timeString) throws ParseException{
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//
		String date=  matter1.format(timeString);
		return matter1.parse(date);
	}
	
	//时间戳
	public static  Date timestampToDate(String timeString) throws ParseException{
		long l = Long.valueOf(timeString).longValue();
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Long time=new Long(l);
	    String d = format.format(time);
	    Date date=format.parse(d);
		return date;
	}
}
