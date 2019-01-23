package tech.xiangcheng.orangebus.company.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;

//@RepositoryDefinition(domainClass = BusSchedule.class, idClass = String.class) 

public interface BusScheduleDao extends JpaRepository<BusSchedule, String> {
	BusSchedule findByIdAndDelFlag(String id, String delFlag);
//	List<BusSchedule> findByDelFlagAndCoachNumber_OriginalStation_CodeAndCoachNumber_TerminalStation_CodeAndDepartureTimeBetweenOrderByDepartureTimeAsc
//	(String delFlag, String originalCode, String terminalCode,Date fromDate,Date toDate);
//	
//	@Query("select b from BusSchedule b where b.departureTime>:fromTime and b.departureTime<:toTime and"
//			+ " b.coachNumber.originalStation.id=:originalStationId")
//	List<BusSchedule> findByIntervalsAndOriginalStationId(@Param("fromTime")Date fromTime,
//			@Param("toTime")Date toTime,@Param("originalStationId")Long id);
	
	List<BusSchedule> findByDelFlagAndCoachNumberIdAndDepartureTimeBetweenOrderByDepartureTimeAsc
	(String delFlag, String id, Date fromDate,Date toDate);
	
	List<BusSchedule> findByDelFlagAndCoachNumber_Id(String delFlag, String id );
	
	List<BusSchedule> findByDelFlagAndCoachNumberIdInAndDepartureTimeBetweenOrderByDepartureTimeAsc(
			String delFlag, String[] ids , Date fromDate, Date toDate 
			);
	
	@Query("select b from BusSchedule b,StationCoachNumber scn_ori,StationCoachNumber scn_ter where b.departureTime>=:fromTime and b.departureTime<=:toTime"
			+ " and b.coachNumber.id=scn_ori.coachNumber.id and scn_ori.onOff.id=:oriOnOffId and scn_ori.station.sysArea.code=:oriAreaCode "+
			 "  and b.coachNumber.id=scn_ter.coachNumber.id and scn_ter.onOff.id=:terOnOffId and scn_ter.station.sysArea.code=:terAreaCode "+
			 " and b.delFlag=:delFlag and b.coachNumber.delFlag=:delFlag")
	List<BusSchedule> findByIntervalsAndSCN(@Param("fromTime")Date fromTime,
			@Param("toTime")Date toTime,@Param("oriOnOffId")String oriOnOffId,@Param("oriAreaCode")String oriAreaCode,
			@Param("terOnOffId")String terOnOffId,@Param("terAreaCode")String terAreaCode,@Param("delFlag")String delFlag);
	
	
//	@Query("select b from BusSchedule b,CheckAccountStationCoach casc where b.departureTime>=:fromTime and b.departureTime<=:toTime"
//			+ " and b.coachNumber.id=casc.coachNumber.id and casc.checkAccount.id=:id and b.delFlag=:delFlag")
//	List<BusSchedule> findByCheckAccountIdAndDepartDate(@Param("fromTime")Date fromTime, @Param("toTime") Date toTime, @Param("id") String id, @Param("delFlag") String delFlag);
	
	@Query("select b from BusSchedule b, ScheduleVehicle sv, Vehicle v, CheckAccountStationCoach casc where b.departureTime>=:fromTime and b.departureTime<=:toTime and "
			+ " sv.busSchedule.id=b.id and sv.vehicle.id=v.id and casc.vehicle.id=v.id and casc.checkAccount.id=:id and "
//			+ "sv.delFlag=:delFlag and "
			+ "b.delFlag=:delFlag and b.coachNumber.delFlag=:delFlag "
			+ " group by b.id ORDER BY departure_time")
	List<BusSchedule> findByCheckAccountIdAndDepartDate(@Param("fromTime")Date fromTime, @Param("toTime") Date toTime, @Param("id") String id, @Param("delFlag") String delFlag);

	//	select b.* from Bus_Schedule b, Schedule_Vehicle sv, Vehicle v, Check_Account_Station_Coach casc
	//where sv.bus_schedule_id=b.id and sv.vehicle_id=v.id and casc.vehicle_id=v.id and casc.check_account_id='1c397c7171244817bd1412561eb21ccc'
	
	
/*
 SELECT bs.id, bs.coach_number_id, bs.code, bs.departure_time, bs.arrival_time
			, bs.limit_ticket_num, bs.remarks, bs.del_flag
		FROM bus_schedule bs, coach_number cn, station_coach_number scn_ori, station stat_ori, 
		sys_area sysa_ori, station_coach_number scn_ter, station stat_ter, sys_area sysa_ter
		WHERE bs.departure_time >= str_to_date('2017-05-24 00:00:00', '%Y-%m-%d %T')
			AND bs.departure_time <= str_to_date('2017-05-24 23:59:59', '%Y-%m-%d %T')
			AND bs.coach_number_id = cn.id
			AND scn_ori.coach_number_id = cn.id
			AND scn_ori.on_off = '0'
			AND scn_ori.station_id = stat_ori.id
			AND stat_ori.area_id = sysa_ori.id
			AND sysa_ori.code = '430200'
			AND scn_ter.coach_number_id = cn.id
			AND scn_ter.on_off = '3'
			AND scn_ter.station_id = stat_ter.id
			AND stat_ter.area_id = sysa_ter.id
			AND sysa_ter.code = '430100'
			AND bs.del_flag = '0'
 */
	
	//List<BusSchedule> findByDelFlagAndDepartureTimeBetweenAndCoachNumber_stationCoachNumbers_
}
