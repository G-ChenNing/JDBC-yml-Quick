package tech.xiangcheng.orangebus.order.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.domain.StationCoachNumber;
import tech.xiangcheng.orangebus.company.service.BusScheduleService;
import tech.xiangcheng.orangebus.company.service.ScheduleVehicleService;
import tech.xiangcheng.orangebus.company.service.StationCoachNumberService;
import tech.xiangcheng.orangebus.config.domain.ConfigSource;
import tech.xiangcheng.orangebus.config.service.ConfigService;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.order.dao.PreSaleDao;
import tech.xiangcheng.orangebus.order.domain.PreSale;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.parent.domain.Constant;

@Service
public class PreSaleServiceImpl implements PreSaleService {

	@Autowired
	PreSaleDao preSaleDao;
	Logger logger = LoggerFactory.getLogger(PreSaleServiceImpl.class);
	@Autowired
	BusScheduleService bss;
	@Autowired
	ScheduleVehicleService svs;
	@Autowired
	ConfigService configService;
	@Autowired
	StationCoachNumberService stationCoachNumberService;
	@Autowired
	private EntityManagerFactory emf;
	@Autowired
	PassengerBusOrderService passengerBusOrderService;
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public PreSale save(JsonObj preSaleJson) {
		String stationCoachNumberId = (String) preSaleJson.getProperty("stationCoachNumberId");
		String busScheduleId = (String) preSaleJson.getProperty("busScheduleId");
		String scheduleVehicleId = (String) preSaleJson.getProperty("scheduleVehicleId");
		String source = (String) preSaleJson.getPropertyOrDefault("source", ConfigEnum.OTHERS.getName());
		String create_by = (String) preSaleJson.getProperty("createBy");
		StationCoachNumber stationCoachNumber = stationCoachNumberService
				.getStationCoachNumberById(stationCoachNumberId);
		BusSchedule busScheduleById = bss.getBusScheduleById(busScheduleId);
		ScheduleVehicle scheduleVehicleById = svs.getScheduleVehicleById(scheduleVehicleId);
		ConfigSource configSource = configService.getSourceById(source);
		if (null == busScheduleById || null == scheduleVehicleById || null == configSource
				|| null == stationCoachNumber) {
			throw new IllegalArgumentException("班次、班次车辆id、来源、stationCoachNumberId必须传入有效值");
		}
		// String service = (String)preSaleJson.getPropertyOrDefault("service",
		// "未选择");
		String phone = (String) preSaleJson.getPropertyOrDefault("phone", "未填写");
		Integer amount = (Integer) preSaleJson.getPropertyOrDefault("amount", 0);
		
		Object t  =preSaleJson.getPropertyOrDefault("total", 0.0);
		Double total = 0.0;
		if(t instanceof Integer){//判断类型
			total= (Integer) preSaleJson.getPropertyOrDefault("total", 0.0)+0.0;
		}else{
			 total = (Double) preSaleJson.getPropertyOrDefault("total", 0.0);
		}
		
		
		
		// String name = (String)preSaleJson.getPropertyOrDefault("name",
		// "未填写");
		int status = 0;// 初始状态为0，代表预售成功（成功保存订单）
		PreSale ps = new PreSale();
		ps.setAmount(amount);
		ps.setBusSchedule(busScheduleById);
		ps.setStationCoachNumber(stationCoachNumber);
		ps.setConfigSource(configSource);
		ps.setCreateDate(new Date());
		ps.setDelFlag(Constant.NOT_DELTED);
		ps.setId(RandomUtil.getId());
		// ps.setName(name);
		ps.setPhone(phone);
		ps.setScheduleVehicle(scheduleVehicleById);
		// ps.setService(service);
		ps.setStatus(status);
		ps.setTotal(total);
		ps.setCreateBy(create_by);
		return this.preSaleDao.save(ps);
//		synchronized (passengerBusOrderService) {
//			Long remainTicketsAmount = passengerBusOrderService.remainTicketsAmount(scheduleVehicleById.getId());
//			if (remainTicketsAmount - ps.getAmount() > 0) {
//				return this.preSaleDao.save(ps);
//			} else {
//				logger.info("请求保存预售订单失败，订单包含票数: " + ps.getAmount() + ",实际剩余车票:" + remainTicketsAmount);
//				return null;
//			}
//		}
	}

	@Transactional
	@Override
	public void changeStatus(String id, int status) {
		preSaleDao.changeStatus(status, id);
	}

	@Override
	public JsonObj getPreSales(String busScheduleId, String scheduleVehicleId) {
		EntityManager em = emf.createEntityManager();
		try {
			// status 0预售成功,1已上车,2已取消
			Query q = em.createNativeQuery(
					"select ps.id, ps.amount, ps.total, ps.source_id, ps.phone, ps.status, s.name sn, sa.name san "
							+ " from pre_sale ps, station_coach_number scn, station s, sys_area sa "
							+ " where ps.station_coach_number_id = scn.id and scn.station_id = s.id and s.area_id = sa.id "
							+ " and ps.bus_schedule_id=? and ps.schedule_vehicle_id=? and ps.status!= ? order by ps.status, scn.rank asc, ps.create_date desc");
			q.setParameter(1, busScheduleId);
			q.setParameter(2, scheduleVehicleId);
			q.setParameter(3, Integer.valueOf(ConfigEnum.pre_sale_cancel.getValue()).intValue());
			List<Object[]> sqlRes = q.getResultList();
			JsonObj res = JsonObj.instance();
			res.putProperty("preSales", sqlRes.stream().map(os -> {
				JsonObj jo = JsonObj.instance();
				String sourceName="";
				if(os[3]!=null && !"".equals((String) os[3])){
					String id= (String) os[3];
					sourceName=ConfigEnum.getSourceById(id).getName();
				}
				String statusName="";
				if(os[5]!=null){
					statusName=ConfigEnum.getStatusById((Integer) os[5]).getName();
				}
				jo.putProperty("id", os[0]).putProperty("amount", os[1]).putProperty("total", os[2])
						.putProperty("source", sourceName)
						.putProperty("phone", os[4])
						.putProperty("status", statusName)
						.putProperty("stationName", os[6]).putProperty("stationCityName", os[7]);
				return jo.toJson();
			}).collect(Collectors.toList()));
			return res;
		} finally {
			em.close();
		}
	}

	@Override
	public JsonObj getStatisticMsg(String scheduleVehicleId) {
		EntityManager em = emf.createEntityManager();
		JsonObj jo = JsonObj.instance();
		try {
			Query q1 = em.createQuery(" select sum(amount) from pre_sale where schedule_vehicle_id=? and status=? ");
			q1.setParameter(1, scheduleVehicleId);
			q1.setParameter(2, Integer.valueOf(ConfigEnum.pre_sale_successful.getValue()).intValue());//只要预售成功的，但还没上车，也还没取消的
			List<BigInteger> qres1 = q1.getResultList();
			jo.putProperty("preSaleNum", qres1.get(0)==null ? 0 : qres1.get(0));
			
			Query q2 = em.createQuery(" select sum(amount) from pre_sale where schedule_vehicle_id=? and status !=? ");
			q2.setParameter(1, scheduleVehicleId);
			q2.setParameter(2, Integer.valueOf(ConfigEnum.pre_sale_cancel.getValue()).intValue());//不为取消，总订
			List<BigInteger> qres2 = q2.getResultList();
			jo.putProperty("preSaleOnNum", qres2.get(0)==null ? 0 : qres2.get(0));
//			return jo;
		} finally {
			em.close();
		}
		
		return jo;
	}

	@Override
	public Integer getBusschedulePresaleAmount(String busScheduleId) {
		return preSaleDao.getBusschedulePresaleAmount(busScheduleId);
	}

	@Override
	public Integer getScheduleVehiclePresaleAmount(String scheduleVehicleId) {
		return preSaleDao.getScheduleVehiclePresaleAmount(scheduleVehicleId);
	}

	@Override
	public List<PreSale> updateList(List<PreSale> presales) {
		return preSaleDao.save(presales);
	}
	@Override
	public List<PreSale> findList(String bus_schedule_id,String schedule_vehicle_id) {
		return preSaleDao.findByBusSchedule_IdAndScheduleVehicle_Id(bus_schedule_id, schedule_vehicle_id);
	}
	@Override
	public PreSale getByPreSaleId(String id, String delFlag) {
		return preSaleDao.findByIdAndDelFlag(id, delFlag);
	}
	
}
