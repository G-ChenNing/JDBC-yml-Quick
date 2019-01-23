package tech.xiangcheng.orangebus.company.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;

public interface ScheduleVehicleDao extends JpaRepository<ScheduleVehicle, String>{

	ScheduleVehicle findById(String id);
	List<ScheduleVehicle> findByDelFlagAndBusScheduleIdOrderByAddPriceAsc(String delFlag, String busScheduleId);
}
