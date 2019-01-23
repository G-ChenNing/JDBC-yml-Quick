package tech.xiangcheng.orangebus.company.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import tech.xiangcheng.orangebus.DBHelper;
import tech.xiangcheng.orangebus.company.dao.BusScheduleDao;
import tech.xiangcheng.orangebus.company.dao.BusScheduleJdbcDao;
import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.BusScheduleTowMonthJson;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleJson;
import tech.xiangcheng.orangebus.company.domain.TicketJson;
import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.order.domain.enumType.OrderCheckStatus;
import tech.xiangcheng.orangebus.parent.domain.Constant;

@Service
public class BusScheduleServiceImpl implements BusScheduleService {

	@Autowired
	ScheduleVehicleService scheduleVehicleService;
	@Autowired
	BusScheduleDao busScheduleDao;

	BusScheduleJdbcDao busScheduleJdbcDao;
	@Autowired
	private EntityManagerFactory emf;

	@Override
	public BusSchedule getBusScheduleById(String id) {
		if (null == id)
			return null;
		return busScheduleDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
	}

	@Override
	public BusSchedule save(BusSchedule busSchedule) {
		if (null == busSchedule || null == busSchedule.getCoachNumber())
			return null;
		busSchedule.setId(RandomUtil.getId());
		return busScheduleDao.save(busSchedule);
	}

	@Override
	public BusSchedule update(BusSchedule busSchedule) {
		if (null == busSchedule.getId() || null == busScheduleDao.findOne(busSchedule.getId()))
			return null;
		return busScheduleDao.save(busSchedule);
	}

	@Override
	public List<BusSchedule> findByBusScheduleByOriginalStationAreaCodeAndTerminalStationAreaCodeAndDepatureDate(
			String originalStationAreaCode, String terminalStationAreaCode, String date) throws ParseException {
		Date nowd = new Date();
		Date now = Constant.getNowDateYMDDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dated = sdf.parse(date);
		if (dated.before(now)) {// 如果是过去的日期，不允许搜索，返回空
			return null;
		}

		Date fromDate;
		Date toDate;

		/*
		 * //原计划是在后端做判断，发车前30分钟的班次不出现，现在改前端做
		 * if(dated.compareTo(now)==0){//如果要搜的日期是今天 Calendar calendar = new
		 * GregorianCalendar(); calendar.setTime(nowd);
		 * calendar.add(calendar.MINUTE,30);//把日期往后增加30分钟.整数往后推,负数往前移动
		 * nowd=calendar.getTime(); //这个时间就是日期往后推30分钟的结果
		 * fromDate=nowd;//则用现在的时间+30分钟，即发车前半个钟内不能订该票 }else{
		 * fromDate=setFromToCalender(date,
		 * 1).getTime();//不是今天就转换成那一天的0点如：2017-07-20 00:00:00 }
		 */

		fromDate = setFromToCalender(date, 1).getTime();// 转换成这一天的0点如：2017-07-20
														// 00:00:00
		toDate = setFromToCalender(date, 2).getTime();
		// Calendar fromCalendar = setFromToCalender(date, 1);
		// Calendar toCalendar = setFromToCalender(date, 2);

		List<BusSchedule> bs = busScheduleDao.findByIntervalsAndSCN(fromDate, toDate, ConfigEnum.DEPART.getValue(),
				originalStationAreaCode, ConfigEnum.TERMINAL.getValue(), terminalStationAreaCode, Constant.NOT_DELTED);
		List<BusSchedule> newbs = new ArrayList<>();

		// 是否有配置车辆，没有配置不显示
		bs.forEach(busSchedule -> {
			if (busSchedule.getScheduleVehicle() != null && busSchedule.getScheduleVehicle().size() > 0) {
				newbs.add(busSchedule);
			}
		});
		return newbs;
	}

	@Deprecated // 弃用
	public List<BusSchedule> findByBusScheduleByOriginalStationCodeAndTerminalStationCodeAndDepatureDate_old(
			String originalStationCode, String terminalStationCode, String date) throws ParseException {
		List<BusSchedule> bs = new ArrayList<BusSchedule>();
		bs = getBusScheduleSelectByUser(Constant.NOT_DELTED, originalStationCode, terminalStationCode,
				date + " 00:00:00", date + " 23:59:59");
		return bs;
	}

	// 放弃该方法
	@Deprecated // 弃用
	public List<BusSchedule> getBusScheduleSelectByUser(String delFlag, String originalCityCode,
			String terminalCityCode, String fromDate, String toDate) {
		List<BusSchedule> busScheduleList = new ArrayList<BusSchedule>();
		// 增加默认设置 del_flag=0,1 on_off=0,4

		String sql1 = "select bs.id, bs.coach_number_id, bs.code, bs.departure_time, bs.arrival_time, bs.limit_ticket_num, bs.remarks, bs.del_flag "
				+ "from  bus_schedule bs, coach_number cn, "
				+ "station_coach_number scn_ori, station stat_ori,sys_area sysa_ori, "
				+ "station_coach_number scn_ter, station stat_ter,sys_area sysa_ter "
				+ "where bs.departure_time >= str_to_date('" + fromDate + "','%Y-%m-%d %T') "
				+ "and bs.departure_time <= str_to_date('" + toDate + "','%Y-%m-%d %T') "
				+ "and bs.coach_number_id= cn.id " + "and scn_ori.coach_number_id = cn.id and scn_ori.on_off ='"
				+ ConfigEnum.DEPART.getValue() + "' and scn_ori.station_id=stat_ori.id "
				+ "and stat_ori.area_id=sysa_ori.id and sysa_ori.code='" + originalCityCode + "' "
				+ "and scn_ter.coach_number_id = cn.id and scn_ter.on_off ='" + ConfigEnum.TERMINAL.getValue()
				+ "' and scn_ter.station_id=stat_ter.id " + "and stat_ter.area_id=sysa_ter.id and sysa_ter.code='"
				+ terminalCityCode + "' and bs.del_flag='" + Constant.NOT_DELTED + "' ";
		/*
		 * 样例： SELECT bs.id, bs.coach_number_id, bs.code, bs.departure_time,
		 * bs.arrival_time , bs.limit_ticket_num, bs.remarks, bs.del_flag FROM
		 * bus_schedule bs, coach_number cn, station_coach_number scn_ori,
		 * station stat_ori, sys_area sysa_ori, station_coach_number scn_ter,
		 * station stat_ter, sys_area sysa_ter WHERE bs.departure_time >=
		 * str_to_date('2017-05-24 00:00:00', '%Y-%m-%d %T') AND
		 * bs.departure_time <= str_to_date('2017-05-24 23:59:59', '%Y-%m-%d
		 * %T') AND bs.coach_number_id = cn.id AND scn_ori.coach_number_id =
		 * cn.id AND scn_ori.on_off = '0' AND scn_ori.station_id = stat_ori.id
		 * AND stat_ori.area_id = sysa_ori.id AND sysa_ori.code = '430200' AND
		 * scn_ter.coach_number_id = cn.id AND scn_ter.on_off = '3' AND
		 * scn_ter.station_id = stat_ter.id AND stat_ter.area_id = sysa_ter.id
		 * AND sysa_ter.code = '430100' AND bs.del_flag = '0'
		 */
		// db1 = new DBHelper(sql);//创建DBHelper对象

		Connection conn = DBHelper.getConn();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql1);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BusSchedule busSchedule = new BusSchedule();
				String id = rs.getString(1);
				// String coachNumberId = ret.getString(2);
				// String code = ret.getString(3);
				// String departureTime = ret.getString(4);
				// String arrivalTime = ret.getString(5);
				// String limitTicketNum = ret.getString(6);
				// String remarks = ret.getString(7);
				// String delFlag1 = ret.getString(8);

				// 此处性能差，后期需修改
				busSchedule = busScheduleDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
				if (busSchedule != null) {
					busScheduleList.add(busSchedule);
				}
			}
			// 关闭链接
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return busScheduleList;
	}

	@Override
	public List<BusSchedule> findByCoachNumberIdAndDepartureDate(String id, String date) throws ParseException {
		Calendar fromCalendar = setFromToCalender(date, 1);
		Calendar toCalendar = setFromToCalender(date, 2);
		List<BusSchedule> bs = busScheduleDao
				.findByDelFlagAndCoachNumberIdAndDepartureTimeBetweenOrderByDepartureTimeAsc(Constant.NOT_DELTED, id,
						fromCalendar.getTime(), toCalendar.getTime());
		return bs;
	}

	@Override
	public List<BusSchedule> findByCoachNumberIdsAndDepartureDate(String ids, String date) throws ParseException {
		String[] idArr = ids.split(",");
		Calendar fromCalendar = setFromToCalender(date, 1);
		Calendar toCalendar = setFromToCalender(date, 2);
		List<BusSchedule> bs = busScheduleDao
				.findByDelFlagAndCoachNumberIdInAndDepartureTimeBetweenOrderByDepartureTimeAsc(Constant.NOT_DELTED,
						idArr, fromCalendar.getTime(), toCalendar.getTime());
		return bs;
	}

	// @Override
	// public TicketJson getBusScheduleDetailById(String id) {
	// List<ScheduleVehicle> scheduleVehicles =
	// scheduleVehicleService.getScheduleVehicleByBusScheduleId(id);
	// if (null == scheduleVehicles || scheduleVehicles.size() ==0) return null;
	// return new TicketJson(scheduleVehicles, true);
	// }

	@Override
	public TicketJson getBusScheduleDetailByIdAndConfigPrice(String id, ConfigPrice cp) throws IOException, SQLException {
		List<ScheduleVehicle> scheduleVehicles = scheduleVehicleService.getScheduleVehicleByBusScheduleId(id);
		List<String> ids = scheduleVehicles.stream().map(sv -> sv.getId()).collect(Collectors.toList());
		Map<String, Integer> remainSeatsMap = scheduleVehicleService.getRemainSeats(ids,"getScheduleVehicleRemainSeats");
		if (null == scheduleVehicles || scheduleVehicles.size() == 0)
			return null;
		return new TicketJson(scheduleVehicles,remainSeatsMap, true, cp);
	}
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date departureDate;
		try {
			departureDate = sdf.parse("2018-01-12");
			Calendar thisCalendar = Calendar.getInstance();
			thisCalendar.setTime(departureDate);
			thisCalendar.set(Calendar.HOUR_OF_DAY, 0);
			thisCalendar.set(Calendar.MINUTE, 0);
			thisCalendar.set(Calendar.SECOND, 0);
			
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  System.out.println(sdf2.format(thisCalendar.getTime()));
			  
			  
			  thisCalendar.add(Calendar.MONTH, +2);
			  int MaxDay=thisCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			  thisCalendar.set( thisCalendar.get(Calendar.YEAR), thisCalendar.get(Calendar.MONTH), MaxDay, 23, 59, 59);
			  System.out.println(sdf2.format(thisCalendar.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		Calendar cal = Calendar.getInstance();
//		  //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
////		  cal.set(Calendar.MONTH, 1-1);
//		  //调到上个月
////		  cal.add(Calendar.MONTH, -1);
//		  //得到一个月最最后一天日期(31/30/29/28)
//		  
//		  cal.add(Calendar.MONTH, +2);
//		  int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//		  //按你的要求设置时间
//		  cal.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay, 23, 59, 59);
//		  //按格式输出
//		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		  System.out.println(sdf.format(cal.getTime()));
//		  String maxDay = sdf.format(cal.getTime());
//		  Calendar cal2 = Calendar.getInstance();
////		  int minDay=cal2.getActualMinimum(Calendar.DAY_OF_MONTH);
////		  cal2.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), minDay, 0, 0, 0);
////		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		  System.out.println(sdf.format(cal2.getTime()));
//		  String nowDay = sdf.format(cal2.getTime());
		
	}
	@Override
	public List<BusScheduleTowMonthJson> getBusScheduleTowMonthInfo(ConfigPrice cp,String selectDate){
		String maxDay = null;
		String nowDay = null;
		if(selectDate==null||"".equals(selectDate)){
			Calendar cal = Calendar.getInstance();
			  //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
//			  cal.set(Calendar.MONTH, 1-1);
			  //调到上个月
//			  cal.add(Calendar.MONTH, -1);
			  //得到一个月最最后一天日期(31/30/29/28)
			  
			  cal.add(Calendar.MONTH, +2);
			  int MaxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			  //按你的要求设置时间
			  cal.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), MaxDay, 23, 59, 59);
			  //按格式输出
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  System.out.println(sdf.format(cal.getTime()));
			  maxDay = sdf.format(cal.getTime());
			  Calendar cal2 = Calendar.getInstance();
//			  int minDay=cal2.getActualMinimum(Calendar.DAY_OF_MONTH);
//			  cal2.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), minDay, 0, 0, 0);
//			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  System.out.println(sdf.format(cal2.getTime()));
			  nowDay = sdf.format(cal2.getTime());
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date departureDate;
			try {
				departureDate = sdf.parse(selectDate);
				Calendar thisCalendar = Calendar.getInstance();
				thisCalendar.setTime(departureDate);
				thisCalendar.set(Calendar.HOUR_OF_DAY, 0);
				thisCalendar.set(Calendar.MINUTE, 0);
				thisCalendar.set(Calendar.SECOND, 0);
				
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  System.out.println(sdf2.format(thisCalendar.getTime()));
				  nowDay = sdf2.format(thisCalendar.getTime());
				  
				  thisCalendar.add(Calendar.MONTH, +2);
				  int MaxDay=thisCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				  thisCalendar.set( thisCalendar.get(Calendar.YEAR), thisCalendar.get(Calendar.MONTH), MaxDay, 23, 59, 59);
				  System.out.println(sdf2.format(thisCalendar.getTime()));
				  maxDay = sdf2.format(thisCalendar.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					"select MIN(total),MAX(total) ,dTimt " + 
					"from " + 
					"(select bsId , bsCode ,dt ,cpPrice ,cpid ,IFNULL(cep.extra_price,0) , a.bscoachNumberId,(cpprice+IFNULL(cep.extra_price,0)) total,DATE_FORMAT(a.dt,'%Y-%m-%d') dTimt  " + 
					"from  " + 
					"(select bs.id bsId,bs.code bsCode,bs.departure_time dt,cp.price cpPrice,cp.id cpid,bs.coach_number_id bscoachNumberId " + 
					"from bus_schedule bs ,config_price cp  " + 
					"where  bs.departure_time BETWEEN  " + 
					"? AND ? and bs.coach_number_id = cp.coach_number_id and cp.member_level_id = ?)a  " + 
					"left JOIN config_extra_price cep on cep.config_price_id = a.cpid and a.dt BETWEEN cep.start_date and cep.end_date  " + 
					")b GROUP BY dTimt order by dTimt "
					);
			q.setParameter(1, nowDay);
			q.setParameter(2, maxDay);
			q.setParameter(3, cp.getMemberLevel().getId());
			List<Object[]> sqlRes = q.getResultList();

			List<BusScheduleTowMonthJson> list=new ArrayList<BusScheduleTowMonthJson>();
			for (Object[] objects : sqlRes) {
				BusScheduleTowMonthJson bstmj = new BusScheduleTowMonthJson();
				bstmj.setMinPrice(Double.valueOf(objects[0].toString()));
				bstmj.setMaxPrice(Double.valueOf(objects[1].toString()));
				bstmj.setDate(objects[2].toString());
			    list.add(bstmj);
			}
			return list;
//			return svs;
		} finally {
			em.close();
		}
	}
//	@Override
//	public TicketJson getBusScheduleDetailByIdAndConfigPrice(String id, ConfigPrice cp) throws Exception {
//		System.out.println(1111);
//		List<ScheduleVehicleJson> scheduleVehicleJsonList = scheduleVehicleService.getScheduleVehicleJsonByBusScheduleId(id);
//		System.out.println(2222);
//		if (null == scheduleVehicleJsonList || scheduleVehicleJsonList.size() == 0)
//			return null;
//		return new TicketJson(scheduleVehicleJsonList, true, cp);
//	}
	
	
	@Override
	public List<BusSchedule> findByCoachNumberId(String id) throws ParseException {
		busScheduleDao.findByDelFlagAndCoachNumber_Id(Constant.NOT_DELTED, id);
		return null;
	}

	@Override
	public List<BusSchedule> getBusScheduleByCheckAccountIdAndDepartDate(String checkAccountId, String departDate)
			throws ParseException {
		Date fromTime = setFromToCalender(departDate, 1).getTime();
		Date toTime = setFromToCalender(departDate, 2).getTime();
		return busScheduleDao.findByCheckAccountIdAndDepartDate(fromTime, toTime, checkAccountId, Constant.NOT_DELTED);
	}

	@Override
	public List<BusSchedule> getBusScheduleByCheckAccountIdAndDepartDateForRole(String checkAccountId, String departDate)
			throws ParseException {
		Date fromTime = setFromToCalender(departDate, 1).getTime();
		Date toTime = setFromToCalender(departDate, 2).getTime();
//		List<BusSchedule> bss = new ArrayList<BusSchedule>();
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					"select bs.* from sv_check svc LEFT JOIN bus_schedule bs ON svc.bus_schedule_id = bs.id "
					+ "WHERE svc.check_account_id = ? AND bs.departure_time BETWEEN ?  AND ?  GROUP BY bs.id ORDER BY bs.departure_time "
					);
			q.setParameter(1, checkAccountId);
			q.setParameter(2, fromTime);
			q.setParameter(3, toTime);
			List<Object[]> sqlRes = q.getResultList();
			//不知道什么错
//			bss.add((BusSchedule) sqlRes.stream().map(os -> {
//				BusSchedule busSchedule =getBusScheduleById((String)os[0]);
////				JsonObj jo = JsonObj.instance();
////				jo.putProperty("busSchudleId", os[0]).putProperty("code", os[1]).putProperty("departureTime", os[2])
////						.putProperty("scheduleVehicleId", os[3]).putProperty("vehicleName", os[4])
////						.putProperty("coachNumberName", os[5]).putProperty("seatNum", os[6]);
//				return busSchedule;
//			}));
			List<BusSchedule> list=new ArrayList<BusSchedule>();

			for (Object[] objects : sqlRes) {
				BusSchedule bs=getBusScheduleById(objects[0].toString());

			    list.add(bs);

			}
			return list;
		} finally {
			em.close();
		}
	}
	@Override
	public List<ScheduleVehicle> getScheduleVehicleByCheckAccountIdAndDepartDateForRole(String checkAccountId, String departDate)
			throws ParseException {
		Date fromTime = setFromToCalender(departDate, 1).getTime();
		Date toTime = setFromToCalender(departDate, 2).getTime();
//		List<ScheduleVehicle> svs = new ArrayList<ScheduleVehicle>();
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					"select sv.* from  (sv_check svc LEFT JOIN schedule_vehicle sv ON svc.schedule_vehicle_id = sv.id) " + 
					"Left Join bus_schedule bs on sv.bus_schedule_id = bs.id WHERE svc.check_account_id = ?" + 
					" AND bs.departure_time BETWEEN ? AND ? "
					);
			q.setParameter(1, checkAccountId);
			q.setParameter(2, fromTime);
			q.setParameter(3, toTime);
			List<Object[]> sqlRes = q.getResultList();
			//不知道什么错
//			svs.add((ScheduleVehicle) sqlRes.stream().map(os -> {
//				ScheduleVehicle scheduleVehicle = scheduleVehicleService.getScheduleVehicleById((String)os[0]);
////				JsonObj jo = JsonObj.instance();
////				jo.putProperty("busSchudleId", os[0]).putProperty("code", os[1]).putProperty("departureTime", os[2])
////						.putProperty("scheduleVehicleId", os[3]).putProperty("vehicleName", os[4])
////						.putProperty("coachNumberName", os[5]).putProperty("seatNum", os[6]);
//				return scheduleVehicle;
//			}));
			List<ScheduleVehicle> list=new ArrayList<ScheduleVehicle>();

			for (Object[] objects : sqlRes) {
				ScheduleVehicle sv=scheduleVehicleService.getScheduleVehicleById(objects[0].toString());

			    list.add(sv);

			}
			return list;
//			return svs;
		} finally {
			em.close();
		}
	}
	@Override
	public JsonObj getBusScheduleInfo(String scheduleVehicleId) {
//		EntityManager em = emf.createEntityManager();
//		try {
//			Query q = em.createNativeQuery(
//					"select bs.id bsid, bs.code, bs.departure_time, sv.id svid, v.name vn,cn.name vnn,v.seat_num "
//							+ " from bus_schedule bs, schedule_vehicle sv, coach_number cn, vehicle v "
//							+ " where cn.del_flag =0 and bs.id = sv.bus_schedule_id and bs.coach_number_id = cn.id and sv.vehicle_id = v.id "
//							+ " and sv.id = ? ");
//			q.setParameter(1, scheduleVehicleId);
//			List<Object[]> sqlRes = q.getResultList();
//			JsonObj res = JsonObj.instance();
//			sqlRes.stream().map(os -> {
//				JsonObj jo = JsonObj.instance();
//				jo.putProperty("busSchudleId", os[0]).putProperty("code", os[1]).putProperty("departureTime", os[2])
//						.putProperty("scheduleVehicleId", os[3]).putProperty("vehicleName", os[4])
//						.putProperty("coachNumberName", os[5]).putProperty("seatNum", os[6]);
//				return jo.toJson();
//			}).collect(Collectors.toList());
//			return res;
//		} finally {
//			em.close();
//		}
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					"select bs.id bsid, bs.code, bs.departure_time, sv.id svid, v.name vn,cn.name vnn,v.seat_num "
							+ " from bus_schedule bs, schedule_vehicle sv, coach_number cn, vehicle v "
							+ " where cn.del_flag =0 and bs.id = sv.bus_schedule_id and bs.coach_number_id = cn.id and sv.vehicle_id = v.id "
							+ " and sv.id = ? ");
			q.setParameter(1, scheduleVehicleId);
			List<Object[]> sqlRes = q.getResultList();
			JsonObj res = JsonObj.instance();
			res.putProperty("busScheduleInfo", sqlRes.stream().map(os -> {
				JsonObj jo = JsonObj.instance();
				jo.putProperty("busSchudleId", os[0]).putProperty("code", os[1]).putProperty("departureTime", os[2])
						.putProperty("scheduleVehicleId", os[3]).putProperty("vehicleName", os[4])
						.putProperty("coachNumberName", os[5]).putProperty("seatNum", os[6]);
				return jo.toJson();
			}).collect(Collectors.toList()));
			return res;
		} finally {
			em.close();
		}

	}

	@Override
	public JsonObj getStationCoachNumberInfo(String busSchudleId) {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createNativeQuery(
					" select scn.id, scn.duration, scn.rank,s.name sn, sa.name san  from station_coach_number scn, station s, sys_area sa, bus_schedule bs "
							+ " where scn.station_id= s.id and s.area_id=sa.id and bs.coach_number_id = scn.coach_number_id and scn.on_off in ('0','1') "
							+ " and bs.id=? order by rank ");
			q.setParameter(1, busSchudleId);
			List<Object[]> sqlRes = q.getResultList();
			JsonObj res = JsonObj.instance();
			res.putProperty("stationCoachNumberInfo", sqlRes.stream().map(os -> {
				JsonObj jo = JsonObj.instance();
				jo.putProperty("stationCoachNumberId", os[0]).putProperty("duration", os[1]).putProperty("rank", os[2])
						.putProperty("stationName", os[3]).putProperty("cityName", os[4]);
				return jo.toJson();
			}).collect(Collectors.toList()));
			return res;
		} finally {
			em.close();
		}
	}

	private Calendar setFromToCalender(String dateTime, int fromOrTo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date departureDate = sdf.parse(dateTime);
		Calendar thisCalendar = Calendar.getInstance();
		thisCalendar.setTime(departureDate);
		if (fromOrTo == 1) {
			thisCalendar.set(Calendar.HOUR_OF_DAY, 0);
			thisCalendar.set(Calendar.MINUTE, 0);
			thisCalendar.set(Calendar.SECOND, 0);
		} else {
			thisCalendar.set(Calendar.HOUR_OF_DAY, 23);
			thisCalendar.set(Calendar.MINUTE, 59);
			thisCalendar.set(Calendar.SECOND, 59);
		}

		return thisCalendar;
	}
}
