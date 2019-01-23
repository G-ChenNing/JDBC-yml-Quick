package tech.xiangcheng.orangebus.account.service;

import java.util.List;

import tech.xiangcheng.orangebus.company.domain.Vehicle;

public interface VehicleService {
	List<Vehicle> getVehicleList(String delFlag);

	Vehicle getVehicleById(String id);
	List<Vehicle> getVehicleListByCheckAccount(String id,String delFlag);
}
