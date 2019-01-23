package tech.xiangcheng.orangebus.order.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.order.domain.PreSale;

public interface PreSaleDao extends JpaRepository<PreSale, String> {
	List<PreSale> findByBusSchedule_IdAndScheduleVehicle_Id(String bus_schedule_id,String schedule_vehicle_id);
	PreSale findByIdAndDelFlag(String id, String delFlag);
	
	@Modifying
	@Query(value = "update pre_sale set status =:status where id =:id", nativeQuery = true)
	void changeStatus(@Param("status") int status, @Param("id") String id);
	
	@Query(value = "select if(isnull(sum(amount)),0,sum(amount)) from pre_sale where bus_schedule_id = :busScheduleId" , nativeQuery = true)
	Integer getBusschedulePresaleAmount(@Param("busScheduleId")String busScheduleId);

	@Query(value = "select if(isnull(sum(amount)),0,sum(amount)) from pre_sale where schedule_vehicle_id = :scheduleVehicleId" , nativeQuery = true)
	Integer getScheduleVehiclePresaleAmount(@Param("scheduleVehicleId")String scheduleVehicleId);

}
