package tech.xiangcheng.orangebus.company.service;

import java.text.ParseException;
import java.util.List;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.BusScheduleTowMonthJson;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.TicketJson;
import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;

public interface BusScheduleService {
	/**
	 * 返回有效的busSchedule,被标记删除的不作为返回结果
	 * @param id
	 * @return id为null或查找失败时返回null
	 */
	BusSchedule getBusScheduleById(String id);
	/**
	 * 
	 * @param busSchedule
	 * @return 保存失败返回null
	 */
	BusSchedule save(BusSchedule busSchedule);
	/**
	 * 更新busSchedule
	 * @param busSchedule, id必须有效
	 * @return id无效时返回null
	 */
	BusSchedule update(BusSchedule busSchedule);
	/**
	 * 
	 * @param originalStationAreaCode 出发站所在城市code
	 * @param terminalStationAreaCode 到达站所在城市code
	 * @param date
	 * @return
	 * @throws ParseException 日期格式有错
	 */
	List<BusSchedule> findByBusScheduleByOriginalStationAreaCodeAndTerminalStationAreaCodeAndDepatureDate
	(String originalStationAreaCode, String terminalStationAreaCode, String date) throws ParseException;
	
	/**
	 * 
	 * @param id
	 * @param date
	 * @return
	 * @throws ParseException 日期格式有错
	 */
	List<BusSchedule> findByCoachNumberIdAndDepartureDate(String id, String date) throws ParseException;
	List<BusSchedule> findByCoachNumberId(String id) throws ParseException;
	/**
	 * 查找多个车次某天的班次
	 * @param ids
	 * @param date
	 * @return
	 * @throws ParseException 日期格式有错
	 */
	List<BusSchedule> findByCoachNumberIdsAndDepartureDate(String ids, String date) throws ParseException;
	/**
	 * 根据busSchedule的id返回详细信息
	 * @param id
	 * @return 返回null为无效
	 */
//	TicketJson getBusScheduleDetailById(String id);
	/**
	 * 根据检票账号id和发车日期查找班次
	 * @param checkAccountId
	 * @param departDate yyyy-MM-dd
	 * @return
	 * @throws ParseException 日期格式错误
	 */
	List<BusSchedule> getBusScheduleByCheckAccountIdAndDepartDate(String checkAccountId, String departDate) throws ParseException;
	TicketJson getBusScheduleDetailByIdAndConfigPrice(String id, ConfigPrice cp) throws Exception;
	JsonObj getBusScheduleInfo(String scheduleVehicleId);
	JsonObj getStationCoachNumberInfo(String busSchudleId);
	List<BusSchedule> getBusScheduleByCheckAccountIdAndDepartDateForRole(String checkAccountId, String departDate)
			throws ParseException;
	List<ScheduleVehicle> getScheduleVehicleByCheckAccountIdAndDepartDateForRole(String checkAccountId,
			String departDate) throws ParseException;
	List<BusScheduleTowMonthJson> getBusScheduleTowMonthInfo(ConfigPrice cp,String selectDate);
}
