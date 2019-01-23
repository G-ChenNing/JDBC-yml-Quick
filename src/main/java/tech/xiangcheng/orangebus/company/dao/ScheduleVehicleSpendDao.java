package tech.xiangcheng.orangebus.company.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleSpend;
import tech.xiangcheng.orangebus.company.domain.TotalIncomeOBJ;

public interface ScheduleVehicleSpendDao extends JpaRepository<ScheduleVehicleSpend, String>{

	List<ScheduleVehicleSpend> findByBusSchedule_IdAndScheduleVehicle_Id(String BusSchedule_Id,String ScheduleVehicle_Id);

	ScheduleVehicleSpend findById(String id);
	
	@Query(value = "select if(isnull(sum(price)),0,sum(price)) from schedule_vehicle_spend where bus_schedule_id = :busScheduleId and schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Double getSumSpentByBusScheduleIdAndScheduleVehicleId(@Param("busScheduleId")String busScheduleId, @Param("scheduleVehicleId")String scheduleVehicleId);

	//这个脚本是对的，返回类型应该不对，
	@Query(value = "select cpm.name, count(1), if(isnull(sum(total)),0,sum(total)) total from passenger_bus_order pbo, config_pay_method cpm where pbo.pay_method = cpm.id and bus_schedule_id = :busScheduleId and schedule_vehicle_id = :scheduleVehicleId group by pay_method " , nativeQuery = true)
	List<TotalIncomeOBJ> getTotalIncomeOBJListByBusScheduleIdAndScheduleVehicleId(@Param("busScheduleId")String busScheduleId, @Param("scheduleVehicleId")String scheduleVehicleId);
}
