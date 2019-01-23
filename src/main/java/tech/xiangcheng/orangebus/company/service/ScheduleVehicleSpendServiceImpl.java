package tech.xiangcheng.orangebus.company.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.company.dao.ScheduleVehicleSpendDao;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleSpend;
import tech.xiangcheng.orangebus.company.domain.TotalIncomeOBJ;
import tech.xiangcheng.orangebus.company.domain.LudanJson;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.order.service.PassengerBusOrderService;
import tech.xiangcheng.orangebus.parent.domain.Constant;

@Service
public class ScheduleVehicleSpendServiceImpl implements ScheduleVehicleSpendService{
	@Autowired
	ScheduleVehicleSpendDao scheduleVehicleSpendDao;
	@Autowired
	PassengerBusOrderService passengerBusOrderService;
	@Override
	public LudanJson getLudanJsonByBusScheduleIdAndScheduleVehicleId(String busScheduleId,String scheduleVehicleId) {
		//收入总和
		Double totalIncome=passengerBusOrderService.getSumSpentByBusScheduleIdAndScheduleVehicleId(busScheduleId, scheduleVehicleId);
		//支出总和
		Double totalSpend=scheduleVehicleSpendDao.getSumSpentByBusScheduleIdAndScheduleVehicleId(busScheduleId, scheduleVehicleId);
		totalSpend= null == totalSpend ? 0 : totalSpend.doubleValue();
		//收入列表，需改
		List<TotalIncomeOBJ> totalIncomeOBJList = scheduleVehicleSpendDao.getTotalIncomeOBJListByBusScheduleIdAndScheduleVehicleId(busScheduleId, scheduleVehicleId);
		//支出列表
		List<ScheduleVehicleSpend> spendList =scheduleVehicleSpendDao.findByBusSchedule_IdAndScheduleVehicle_Id(busScheduleId, scheduleVehicleId);
		
		LudanJson ludanJson=new LudanJson(busScheduleId, scheduleVehicleId,totalIncome,totalSpend,spendList,totalIncomeOBJList);
		return ludanJson;
	}

	@Override
	public ScheduleVehicleSpend save(ScheduleVehicleSpend svs) {
		if (null == svs || null == svs.getName()) 
			return null;
		svs.setId(RandomUtil.getId());
		svs.setDelFlag(Constant.NOT_DELTED);
		svs.setCreateDate(new Date());
		return scheduleVehicleSpendDao.save(svs);
	}

	@Override
	public int deleteSVS(String id) {
		ScheduleVehicleSpend scheduleVehicleSpend = scheduleVehicleSpendDao.findById(id);
		if ( null == scheduleVehicleSpend)
			return 2;
		else {
			scheduleVehicleSpendDao.delete(scheduleVehicleSpend);
			return 1;
		}
	}



}
