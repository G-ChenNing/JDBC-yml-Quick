package tech.xiangcheng.orangebus.company.domain;

import java.util.ArrayList;
/**
 * 包装busSchdule返回给前端
 * @author yang
 *
 */
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.account.domain.SvCheck;
import tech.xiangcheng.orangebus.coupon.domain.ConfigExtraPrice;
import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;
import tech.xiangcheng.orangebus.leaf.util.time.DateFormatUtil;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.parent.domain.Constant;
public class TicketJson {
	String id; //班次id
	String departTime;//发车时间
	String code;//编号
	Double price=null;//青铜价格，即原价
	Double thisMemberPrice=null;//会员价
	Double bigPrice=0.0;//优惠价格上限，未登录情况
	Double smallPrice=0.0;//优惠价格下限，未登录情况
	int kilometre;//公里数
	String companyName;// 所属公司名称
	String coachNumberName;
	Integer totalRemainSeats;
	//沿途经停站信息
	List<StationCoachNumberJson> viaInfo;
	//班次对应车辆信息
	List<ScheduleVehicleJson> scheduleVehicleInfo =new ArrayList<ScheduleVehicleJson>();
	//乘务员对应车辆信息
	List<ScheduleVehicleJson> checkScheduleVehicleInfo =new ArrayList<ScheduleVehicleJson>();
	/**
	 * 
	 * @param busSchedule
	 * @param needViaInfo 配置是否需要途径站信息,若为false则viaInfo内容为空
	 */
	public TicketJson(BusSchedule busSchedule, boolean needViaInfo) {
		this(busSchedule);
		if (null == busSchedule) return;
		CoachNumber coachNumber = busSchedule.getCoachNumber();
		if (null == coachNumber) return;
		viaInfo = new ArrayList<>();
		if (needViaInfo && null != coachNumber.getStationCoachNumbers()) {
			coachNumber.getStationCoachNumbers().forEach(stationCoachNumber -> {
				if(!("1").equals(stationCoachNumber.getDelFlag())){//逻辑关闭该  车次配置站点
					viaInfo.add(new StationCoachNumberJson(stationCoachNumber));
				}
			});
		}
	}
	public TicketJson setRemainSeats(Integer totalRemainSeats) {
		this.totalRemainSeats = totalRemainSeats;
		return this;
	}
	public TicketJson(BusSchedule busSchedule, boolean needViaInfo, ConfigPrice cp) {//该cp是从前端来的信息分装在这个对象中
		this(busSchedule,needViaInfo);
		CoachNumber coachNumber = busSchedule.getCoachNumber();
		List<ConfigPrice> configPriceList= coachNumber.getConfigPrices();//获取该线路所有价格
		ConfigPrice configPrice = new ConfigPrice();
		if(cp.getMemberLevel()!=null && !cp.getMemberLevel().getId().isEmpty()){//如果cp的用户等级不为空，即用户已登录
			for(int i=0; i<configPriceList.size();i++){
				ConfigPrice cpi= configPriceList.get(i);
				if(cpi.getOriginalCity().getCode().equals(cp.getOriginalCity().getCode())
						&& cpi.getTerminalCity().getCode().equals(cp.getTerminalCity().getCode())){
					//如果出发终点站和cp的一致
					if(cpi.getMemberLevel().getId().equals(ConfigEnum.MEMBERZERO.getValue())){
						price=cpi.getPrice();//青铜价格，即原价
						price=addExtraPrice(price,cpi,busSchedule);//额外加价
					}
					if(cpi.getMemberLevel().getId().equals(cp.getMemberLevel().getId())){
						//如果该线路中的某一个价格的会员等级=cp的会员等级（前端来的是用户的等级）
						//则这个价格就是前端需要给用户的价格
						configPrice=cpi;
					}
				}
			}
			thisMemberPrice=configPrice.getPrice();//则这个价格就是前端需要给用户的价格
			thisMemberPrice=addExtraPrice(thisMemberPrice,configPrice,busSchedule);//额外加价
			
			
		}else{//如果cp的用户等级为空，即用户未登录
			
			for(int i=0; i<configPriceList.size();i++){
				ConfigPrice cpi= configPriceList.get(i);
				if(cpi.getOriginalCity().getCode().equals(cp.getOriginalCity().getCode())
						&& cpi.getTerminalCity().getCode().equals(cp.getTerminalCity().getCode())){//如果出发终点站和cp的一致
					if(cpi.getMemberLevel().getId().equals(ConfigEnum.MEMBERZERO.getValue())){
						price=cpi.getPrice();//青铜价格，即原价
						price=addExtraPrice(price,cpi,busSchedule);//额外加价
					}else{
						//这个else意思是，优惠价格上限，不能使原始青铜价
						if(cpi.getPrice()>bigPrice){
							bigPrice=cpi.getPrice();
							bigPrice=addExtraPrice(bigPrice,cpi,busSchedule);//额外加价
						}
					}
					//把管理员的0.01取消
					if((cpi.getPrice()<smallPrice && !cpi.getMemberLevel().getId().equals(ConfigEnum.MEMBERADMIN.getValue())) || smallPrice==0.0){
						smallPrice=cpi.getPrice();
						smallPrice=addExtraPrice(smallPrice,cpi,busSchedule);//额外加价
					}
				}
			}
//			if(bigPrice==0.0 || smallPrice==0.0){
//				thisMemberPrice=null;
//			}else{
//				if(bigPrice==smallPrice){
//					thisMemberPrice=bigPrice+"";
//				}else{
//					thisMemberPrice=smallPrice+"-"+bigPrice;//显示会员价格的区间
//				}
//			}
		}
	}
	//该方法在orderhandler中有重复，如修改，请注意
	private double addExtraPrice(double thePrice,ConfigPrice configPrice, BusSchedule busSchedule){
		//配置某个时间段的班次增加价格，如：国庆、春节提价等
		List<ConfigExtraPrice> cepList=configPrice.getConfigExtraPriceList();
		for(int i=0; i<cepList.size();i++){
			if(busSchedule.getDepartureTime().getTime()>=cepList.get(i).getStartDate().getTime() &&
					busSchedule.getDepartureTime().getTime()<cepList.get(i).getEndDate().getTime()){
				thePrice+=cepList.get(i).getExtraPrice();
			}
		}
		return thePrice;
	}
	
	public TicketJson(BusSchedule busSchedule, String checkAccountid, 
			boolean needViaInfo, boolean needAllBusScheduleVehicleInfo,boolean needCheckVehicleInfo) {
		
		this(busSchedule, needViaInfo);
		// System.out.println("checkAccountid:"+checkAccountid);
		if (needAllBusScheduleVehicleInfo) {
			busSchedule.getScheduleVehicle().forEach(scheduleVehicle -> {
				scheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle));
			});
		}
		if (needCheckVehicleInfo) {
			busSchedule.getScheduleVehicle().forEach(scheduleVehicle -> {
				scheduleVehicle.getVehicle().getCheckAccountStationCoach().forEach(checkAccountStationCoach -> {
					// System.out.println("checkAccountStationCoach.getCheckAccount().getId():"+checkAccountStationCoach.getCheckAccount().getId());
					if (checkAccountStationCoach.getCheckAccount().getId().equals(checkAccountid)
							&& Constant.NOT_DELTED.equals(scheduleVehicle.getDelFlag())) {
						checkScheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle));
					}

				});

			});
		}
	}
	
	public TicketJson(BusSchedule busSchedule, String checkAccountid, 
			boolean needViaInfo, boolean needAllBusScheduleVehicleInfo,boolean needCheckVehicleInfo,List<ScheduleVehicle> svs,List<SvCheck> svcs) {
		
		this(busSchedule, needViaInfo);
		// System.out.println("checkAccountid:"+checkAccountid);
		if (needAllBusScheduleVehicleInfo) {
			busSchedule.getScheduleVehicle().forEach(scheduleVehicle -> {
				scheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle));
			});
		}
		if (needCheckVehicleInfo) {
			svs.forEach(scheduleVehicle -> {
				svcs.forEach(svc -> {
					// System.out.println("checkAccountStationCoach.getCheckAccount().getId():"+checkAccountStationCoach.getCheckAccount().getId());
					if (busSchedule.getId().equals(svc.getBusSchedule().getId()) && svc.getCheckAccount().getId().equals(checkAccountid)
							&&  scheduleVehicle.getId().equals(svc.getScheduleVehicle().getId())  && Constant.NOT_DELTED.equals(scheduleVehicle.getDelFlag())) {
						checkScheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle));
					}

				});

			});
		}

	}
	
	/**
	 * @param scheduleVehicles
	 * @param needVehicleInfo  是否需要车辆信息 
	 */
	public TicketJson(List<ScheduleVehicle> scheduleVehicles, boolean needVehicleInfo) {
		this(scheduleVehicles==null? null : scheduleVehicles.get(0).getBusSchedule(), needVehicleInfo);
		scheduleVehicles.forEach(scheduleVehicle->{
			scheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle));
		});
//		for(int i = 0 ; i < scheduleVehicles.size() ; i++) {
//			ScheduleVehicleJson scheduleVehicleJson=new ScheduleVehicleJson(scheduleVehicles.get(i));
//			scheduleVehicleInfo.add(scheduleVehicleJson);
//		}
	}
	
	/**
	 * @param scheduleVehicles
	 * @param needVehicleInfo  是否需要车辆信息 加上配置价格
	 */
	public TicketJson(List<ScheduleVehicle> scheduleVehicles, Map<String, Integer> remainSeatsMap, boolean needVehicleInfo,ConfigPrice cp) {
		this(scheduleVehicles==null? null : scheduleVehicles.get(0).getBusSchedule(), needVehicleInfo, cp);
		scheduleVehicles.forEach(scheduleVehicle->{
			scheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle).setRemainSeats(remainSeatsMap.get(scheduleVehicle.getId())));
		});
	}
//	public TicketJson(List<ScheduleVehicle> scheduleVehicles, boolean needVehicleInfo,ConfigPrice cp) {
//		this(scheduleVehicles==null? null : scheduleVehicles.get(0).getBusSchedule(), needVehicleInfo, cp);
//		scheduleVehicles.forEach(scheduleVehicle->{
//			scheduleVehicleInfo.add(new ScheduleVehicleJson(scheduleVehicle));
//		});
//	}
	
	/**
	 * @param scheduleVehicles
	 * @param needVehicleInfo  是否需要车辆信息 加上配置价格
	 */
//	public TicketJson(List<ScheduleVehicleJson> scheduleVehicleJsonList, boolean needVehicleInfo,ConfigPrice cp) {
//		this(scheduleVehicleJsonList==null? null : scheduleVehicleJsonList.get(0).getBusSchedule(), needVehicleInfo, cp);
//		scheduleVehicleInfo.addAll(scheduleVehicleJsonList);
//	}
	
	private TicketJson(BusSchedule busSchedule) {
		if (null == busSchedule) return;
		id = busSchedule.getId() == null ? "" : busSchedule.getId();
		code = busSchedule.getCode() == null ? "" : busSchedule.getCode();
		departTime = DateFormatUtil.getyyyy_MM_dd_hh_mmDateStr(busSchedule.getDepartureTime());
		CoachNumber coachNumber = busSchedule.getCoachNumber();
		if (null == coachNumber) return;
		coachNumberName=coachNumber.getName();
//		price = coachNumber.getPrice() == null ? 0.0 : coachNumber.getPrice();
		kilometre = coachNumber.getKilometre();
		companyName = coachNumber.getSysOffice() == null ? "" : coachNumber.getSysOffice().getName();
	}
	
 


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<StationCoachNumberJson> getViaInfo() {
		return viaInfo;
	}
	public void setViaInfo(List<StationCoachNumberJson> viaInfo) {
		this.viaInfo = viaInfo;
	}


	public List<ScheduleVehicleJson> getScheduleVehicleInfo() {
		return scheduleVehicleInfo;
	}
	public void setScheduleVehicleInfo(List<ScheduleVehicleJson> scheduleVehicleInfo) {
		this.scheduleVehicleInfo = scheduleVehicleInfo;
	}
	public String getDepartTime() {
		return departTime;
	}
	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getKilometre() {
		return kilometre;
	}
	public void setKilometre(int kilometre) {
		this.kilometre = kilometre;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	//查看格式	
	public static void main(String[] args) throws JsonProcessingException {
		
		CoachNumber coachNumber = new CoachNumber();
		List<StationCoachNumber> stationCoachNumbers = new ArrayList<StationCoachNumber>();
		for (int i = 0; i < 3; ++i) {
			StationCoachNumber stationCoachNumber =  new StationCoachNumber();
			stationCoachNumber.setRank(i);
			stationCoachNumbers.add(stationCoachNumber);
		}
		coachNumber.setStationCoachNumbers(stationCoachNumbers);
		BusSchedule busSchedule = new BusSchedule();
		busSchedule.setCoachNumber(coachNumber);
		TicketJson ticketJson = new TicketJson(busSchedule, false);
//		System.out.println(JsonUtil.getJsonStr(ticketJson));
		ticketJson = new TicketJson(busSchedule, true);
//		System.out.println(JsonUtil.getJsonStr(ticketJson));
		List<ScheduleVehicle> scheduleVehicles = new ArrayList<ScheduleVehicle>();
		ticketJson = new TicketJson(scheduleVehicles, false);
//		System.out.println(JsonUtil.getJsonStr(ticketJson));
		scheduleVehicles.get(0).setBusSchedule(busSchedule);
		ticketJson = new TicketJson(scheduleVehicles, true);
//		System.out.println(JsonUtil.getJsonStr(ticketJson));
	}

	public String getCoachNumberName() {
		return coachNumberName;
	}

	public void setCoachNumberName(String coachNumberName) {
		this.coachNumberName = coachNumberName;
	}

	public List<ScheduleVehicleJson> getCheckScheduleVehicleInfo() {
		return checkScheduleVehicleInfo;
	}

	public void setCheckScheduleVehicleInfo(List<ScheduleVehicleJson> checkScheduleVehicleInfo) {
		this.checkScheduleVehicleInfo = checkScheduleVehicleInfo;
	}

	public Double getThisMemberPrice() {
		return thisMemberPrice;
	}

	public void setThisMemberPrice(Double thisMemberPrice) {
		this.thisMemberPrice = thisMemberPrice;
	}

	public Double getBigPrice() {
		return bigPrice;
	}

	public void setBigPrice(Double bigPrice) {
		this.bigPrice = bigPrice;
	}

	public Double getSmallPrice() {
		return smallPrice;
	}

	public void setSmallPrice(Double smallPrice) {
		this.smallPrice = smallPrice;
	}
	public Integer getTotalRemainSeats() {
		return totalRemainSeats;
	}
	public void setTotalRemainSeats(Integer totalRemainSeats) {
		this.totalRemainSeats = totalRemainSeats;
	}

	
	
	
	
}
