package tech.xiangcheng.orangebus.company.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import tech.xiangcheng.orangebus.company.dao.ScheduleVehicleDao;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicleJson;
import tech.xiangcheng.orangebus.generalcrud.GeneralService;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.order.domain.PassengerBusOrder;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.order.service.PassengerBusOrderService;
import tech.xiangcheng.orangebus.order.service.PreSaleService;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class ScheduleVehicleServiceImpl implements ScheduleVehicleService {

	@Autowired
	ScheduleVehicleDao scheduleVehicleDao;
	@Autowired 
	BusScheduleService busScheduleService;
	@Autowired
	PassengerBusOrderService passengerBusOrderService;
	@Autowired
	PreSaleService preSaleService;
	@Autowired
	GeneralService generalService;
	
	@Override
	public ScheduleVehicle getScheduleVehicleById(String scheduleVehicleId) {
		return scheduleVehicleDao.findById(scheduleVehicleId);
	}

	@Override
	public List<ScheduleVehicle> getScheduleVehicleByBusScheduleId(String busScheduleId) {
		if (null == busScheduleId || busScheduleId.isEmpty() ||  
				null == busScheduleService.getBusScheduleById(busScheduleId))
			return null;
		return scheduleVehicleDao.findByDelFlagAndBusScheduleIdOrderByAddPriceAsc(Constant.NOT_DELTED, busScheduleId);
	}
	
//	@Override
//	public List<ScheduleVehicleJson> getScheduleVehicleJsonByBusScheduleId(String busScheduleId) throws Exception {
//		if (null == busScheduleId || busScheduleId.isEmpty() ||  
//				null == busScheduleService.getBusScheduleById(busScheduleId))
//			return null;
//		 List<ScheduleVehicleJson> scheduleVehicleJsonList= new ArrayList<ScheduleVehicleJson>();
//		List<ScheduleVehicle> scheduleVehicleList= scheduleVehicleDao.findByDelFlagAndBusScheduleIdOrderByAddPriceAsc(Constant.NOT_DELTED, busScheduleId);
//		List<String> ids = scheduleVehicleList.stream().map(sv -> sv.getId()).collect(Collectors.toList());
//		Map<String, Integer> remainSeatsMap = getRemainSeats(ids,"getScheduleVehicleRemainSeats");
//		
//		scheduleVehicleList.forEach(scheduleVehicle->{
//			scheduleVehicleJsonList.add(new ScheduleVehicleJson(scheduleVehicle).setRemainSeats(remainSeatsMap.get(scheduleVehicle.getId())));
//		});
//		
//		return scheduleVehicleJsonList;
//	}
	

	@Override
	public Map<String, Integer> getRemainSeats(List<String> ids, String index) throws IOException, SQLException {
//index 只可取geBusScheduletRemainSeats 或  getScheduleVehicleRemainSeats 详情请查看busSchedule.yaml
		List<String> idds = new  ArrayList<>();
		idds.addAll(ids);
		JsonObj args = JsonObj.instance().putProperty("ids", ids);
		args.putProperty("validSeconds", PassengerBusOrderService.NOT_PAID_ORDER_VAILD_SECONDS);
		String select = generalService.select(index, args);
		JsonObj jo = new JsonObj(select);
		Object obj = jo.getProperty("remainSeats");
		if (obj instanceof String){
			String res = (String) obj;
			Map<String, Integer> remainTicketsMap = new HashMap<>();
			jo = new JsonObj(res);
			remainTicketsMap.put((String)jo.getProperty("id"), (Integer)jo.getProperty("remainSeats"));
			return remainTicketsMap;
		}else{
			List<String> res = (List<String>)obj;
			Map<String, Integer> remainTicketsMap = new HashMap<>();
			for (String r : res) {
				jo = new JsonObj(r);
				remainTicketsMap.put((String)jo.getProperty("id"), (Integer)jo.getProperty("remainSeats"));
			}
			return remainTicketsMap;
		}
	}
	
	//未测试，可能有bug，最初是为了乘务员端的班次车辆的已验、未验票数和余票数20171209
	@Override
	public Map<String, Map> geSVStatisticsByBSid(List<String> ids ) throws IOException, SQLException {
		//详情请查看check项目的busSchedule.yaml
		String index="geSVStatistics";
		List<String> idds = new  ArrayList<>();
		idds.addAll(ids);
		JsonObj args = JsonObj.instance().putProperty("ids", ids);
		args.putProperty("validSeconds", PassengerBusOrderService.NOT_PAID_ORDER_VAILD_SECONDS);
		String select = generalService.select(index, args);
		JsonObj jo = new JsonObj(select);
		Object obj = jo.getProperty("remainSeats");
		

			List<String> res = (List<String>)obj;
			Map<String, Map> remainTicketsMap = new HashMap<>();
			for (String r : res) {
				jo = new JsonObj(r);
				Map<String,Integer> statMap = new HashMap<>();
				statMap.put("remainSeats", (Integer)jo.getProperty("remainSeats"));
				statMap.put("checkNum", (Integer)jo.getProperty("checkNum"));
				statMap.put("unCheckNum", (Integer)jo.getProperty("unCheckNum"));
				remainTicketsMap.put((String)jo.getProperty("id"), statMap);
			}
			return remainTicketsMap;
		
	}
	
	
	@Override
	public JsonObj getStatisticMsg(String scheduleVehicleId) {
		JsonObj orderStatisticMsg = passengerBusOrderService.getOrderStatisticMessagee(scheduleVehicleId);
		//预售，已预约，及已预约+已上车
		JsonObj preSaleStatisticMsg = preSaleService.getStatisticMsg(scheduleVehicleId);
		return orderStatisticMsg.union(preSaleStatisticMsg);
	}

	@Override
	public ScheduleVehicle save(ScheduleVehicle scheduleVehicle) {
		scheduleVehicle.setId(RandomUtil.getId());
		scheduleVehicle.setDelFlag(Constant.NOT_DELTED);
		return scheduleVehicleDao.save(scheduleVehicle);
	}

	@Override
	public ScheduleVehicle update(ScheduleVehicle scheduleVehicle) {
		if (null == scheduleVehicle.getId() || null == scheduleVehicleDao.findOne(scheduleVehicle.getId()))
			return null;
		return scheduleVehicleDao.save(scheduleVehicle);
	}

	
	@Override
	public ScheduleVehicle deleteById(ScheduleVehicle scheduleVehicle) {
		if (null == scheduleVehicle.getId() || null == scheduleVehicleDao.findOne(scheduleVehicle.getId()))
			return null;
		scheduleVehicle.setDelFlag(Constant.DELTED);
		return scheduleVehicleDao.save(scheduleVehicle);
	}
}
