package tech.xiangcheng.orangebus.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.SvCheckDao;
import tech.xiangcheng.orangebus.account.domain.SvCheck;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;

@Service
public class SvCheckServiceImpl implements SvCheckService{
	@Autowired
	SvCheckDao svCheckDao;
	@Override
	public List<SvCheck> getSvCheckByScheduleVehicleId(String id) {
		List<SvCheck> list = svCheckDao.findByScheduleVehicle_id(id);
		return list;
	}
	@Override
	public PassengerBusOrder setCheckPhone(PassengerBusOrder order) {
		List<SvCheck> list = svCheckDao.findFirst1ByScheduleVehicle_idAndRole(order.getScheduleVehicle().getId(),1);
		list.forEach(item->{order.setCheckPhoneNum(item.getCheckAccount().getPhone());});
		return order;
	}
	@Override
	public List<SvCheck> getSvCheckByCheckAccountId(String id) {
		List<SvCheck> list = svCheckDao.findByCheckAccount_id(id);
		return list;
	}
}
