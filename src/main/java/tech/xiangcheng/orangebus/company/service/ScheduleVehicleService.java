package tech.xiangcheng.orangebus.company.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleJson;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;

public interface ScheduleVehicleService {

	ScheduleVehicle getScheduleVehicleById(String scheduleVehicleId);
	/**
	 * 根据班次id获得scheduleVehicle
	 * @param busScheduleId
	 * @return
	 */
	List<ScheduleVehicle> getScheduleVehicleByBusScheduleId(String busScheduleId);
	/**
	 * 获得统计信息
	 * @param scheduleVehicleId
	 * @return
	 */
	JsonObj getStatisticMsg(String scheduleVehicleId);
	Map<String, Integer> getRemainSeats(List<String> ids, String index) throws IOException, SQLException;
	
	public ScheduleVehicle save(ScheduleVehicle scheduleVehicle);
	public ScheduleVehicle update(ScheduleVehicle scheduleVehicle);
	public ScheduleVehicle deleteById(ScheduleVehicle scheduleVehicle);
	Map<String, Map> geSVStatisticsByBSid(List<String> ids) throws IOException, SQLException;
}
