package tech.xiangcheng.orangebus.account.service;

import java.util.List;

import tech.xiangcheng.orangebus.account.domain.SvCheck;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;

public interface SvCheckService {

	List<SvCheck> getSvCheckByScheduleVehicleId(String id);
	PassengerBusOrder setCheckPhone(PassengerBusOrder order);
	List<SvCheck> getSvCheckByCheckAccountId(String id);
}
