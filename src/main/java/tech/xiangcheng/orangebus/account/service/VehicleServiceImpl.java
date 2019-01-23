package tech.xiangcheng.orangebus.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.company.dao.VehicleDao;
import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.Vehicle;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class VehicleServiceImpl implements VehicleService {
	@Autowired
	VehicleDao vehicleDao;

	@Override
	public List<Vehicle> getVehicleList(String delFlag) {
		List<Vehicle> list = vehicleDao.findByDelFlagOrderByName(delFlag);
		return list;
	}
	
	@Override
	public Vehicle getVehicleById(String id) {
		if (null == id)
			return null;
		return vehicleDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
	}

	@Override
	public List<Vehicle> getVehicleListByCheckAccount(String id, String delFlag) {
		return vehicleDao.findByCheckAccountIdAndDelFlag(id, delFlag);
	}
	
}
