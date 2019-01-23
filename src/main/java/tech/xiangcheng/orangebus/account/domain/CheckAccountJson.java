/**
 * 
 */
package tech.xiangcheng.orangebus.account.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.company.domain.CheckAccountStationCoach;
import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.company.domain.Vehicle;
import tech.xiangcheng.orangebus.company.domain.VehicleJson;
import tech.xiangcheng.orangebus.leaf.util.json.JsonUtil;

/**
 * @author yang
 *
 */
public class CheckAccountJson {
	String id;
	String name;
	String phone;
	//for check account
	String company;
//	List<String> coachNumberIdStrings;//获取该check account配对的 coach number， 配对表:check_account_station_coach, 对象：CheckAccountStationCoach
//	Map<String,CoachNumberJson> coachNumberInfoMap;
	List<String> vehicleIdStrings;//获取该check account配对的 coach number， 配对表:check_account_station_coach, 对象：CheckAccountStationCoach
	Map<String,VehicleJson> vehicleInfoMap;
//	public CheckAccountJson(CheckAccount checkAccount, boolean needCoachNumberInfo) {
//		if (null == checkAccount) return;
//		id = checkAccount.getId();
//		name = checkAccount.getName();
//		phone = checkAccount.getPhone();
//		company = null == checkAccount.getSysOffice() ? null :checkAccount.getSysOffice().getName();
//		coachNumberInfoMap = new HashMap<>();
//		coachNumberIdStrings = new ArrayList<>();
//		for (CheckAccountStationCoach checkAccountStationCoach : checkAccount.getCheckAccountStationCoach()) {
//			CoachNumber coac = checkAccountStationCoach.getCoachNumber();
//			if (null == coac) continue;
//			coachNumberIdStrings.add(coac.getId());
//			if (!needCoachNumberInfo) continue;
//			coachNumberInfoMap.put(coac.getId(), new CoachNumberJson(coac));
//		}
//	}
	
	public CheckAccountJson(CheckAccount checkAccount, boolean needCoachNumberInfo) {
		if (null == checkAccount) return;
		id = checkAccount.getId();
		name = checkAccount.getName();
		phone = checkAccount.getPhone();
		company = null == checkAccount.getSysOffice() ? null :checkAccount.getSysOffice().getName();
		vehicleIdStrings = new ArrayList<>();
		vehicleInfoMap = new HashMap<>();
		for (CheckAccountStationCoach checkAccountStationCoach : checkAccount.getCheckAccountStationCoach()) {
			Vehicle vehicle = checkAccountStationCoach.getVehicle();
			if (null == vehicle) continue;
			vehicleIdStrings.add(vehicle.getId());
			vehicleInfoMap.put(vehicle.getId(), new VehicleJson(vehicle));
		}
	}
	
	public class CoachNumberJson {
		String code;
		String name;
		String startStation;
		String endStation;
		String price;
		
		public CoachNumberJson(CoachNumber coachNumber) {
			if (null == coachNumber) return;
			code = coachNumber.getCode();
			name = coachNumber.getName();
//			startStation = null == coachNumber.getOriginalStation() ? null : coachNumber.getOriginalStation().getName();
//			endStation = null == coachNumber.getTerminalStation() ? null : coachNumber.getTerminalStation().getName();
			price = null == coachNumber.getPrice() ? null : coachNumber.getPrice().toString();
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStartStation() {
			return startStation;
		}
		public void setStartStation(String startStation) {
			this.startStation = startStation;
		}
		public String getEndStation() {
			return endStation;
		}
		public void setEndStation(String endStation) {
			this.endStation = endStation;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
	}
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getCompany() {
		return company;
	}



	public void setCompany(String company) {
		this.company = company;
	}



	public List<String> getVehicleIdStrings() {
		return vehicleIdStrings;
	}



	public void setVehicleIdStrings(List<String> vehicleIdStrings) {
		this.vehicleIdStrings = vehicleIdStrings;
	}



	public Map<String, VehicleJson> getVehicleInfoMap() {
		return vehicleInfoMap;
	}



	public void setVehicleInfoMap(Map<String, VehicleJson> vehicleInfoMap) {
		this.vehicleInfoMap = vehicleInfoMap;
	}




	/**
	 * 查看格式输出
	 */
//	public static void main(String[] args) throws JsonProcessingException {
//		CheckAccount checkAccount = new CheckAccount();
//		for (int i = 0; i < 3; ++i) {
//			CoachNumber coa = new CoachNumber();
//			coa.setId("" + i);
//			CheckAccountStationCoach checkAccountStationCoach = new CheckAccountStationCoach();
//			checkAccountStationCoach.setCoachNumber(coa);
//			if (null == checkAccount.getCheckAccountStationCoach())
//				checkAccount.setCheckAccountStationCoach(new ArrayList<>());
//			checkAccount.getCheckAccountStationCoach().add(checkAccountStationCoach);
//		}
//		CheckAccountJson checkAccountJson = new CheckAccountJson(checkAccount, true);
//		System.out.println(JsonUtil.getJsonStr(checkAccountJson));
//	}
}
