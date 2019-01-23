package tech.xiangcheng.orangebus.company.service;


import java.util.List;

import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleSpend;
import tech.xiangcheng.orangebus.company.domain.LudanJson;

public interface ScheduleVehicleSpendService {


	ScheduleVehicleSpend save(ScheduleVehicleSpend svs);
	
	int deleteSVS(String id);

	LudanJson getLudanJsonByBusScheduleIdAndScheduleVehicleId(String busScheduleId, String scheduleVehicleId);

}
