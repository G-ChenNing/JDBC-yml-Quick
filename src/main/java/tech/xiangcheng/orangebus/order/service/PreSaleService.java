package tech.xiangcheng.orangebus.order.service;

import java.util.List;

import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.order.domain.PreSale;

public interface PreSaleService {
	PreSale save(JsonObj preSaleJson);
	/**
	 * 更改状态
	 * @param status 0预售成功，1已上车，2已取消
	 * @param id 预售单id
	 */
	void changeStatus(String id, int status);
	
	JsonObj getPreSales(String busScheduleId, String scheduleVehicleId);
	
	JsonObj getStatisticMsg(String scheduleVehicleId);
	Integer getBusschedulePresaleAmount(String busScheduleId);
	Integer getScheduleVehiclePresaleAmount(String scheduleVehicleId);
	List<PreSale> updateList(List<PreSale> presales);
	List<PreSale> findList(String bus_schedule_id, String schedule_vehicle_id);
	PreSale getByPreSaleId(String id, String delFlag);
}
