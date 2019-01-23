package tech.xiangcheng.orangebus.address.service;

import java.util.HashMap;
import java.util.List;

import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;

public interface SysAreaService {
	List<String> getCitysNameByCityNameFirstLetter(String letter);
	SysArea save(SysArea sysArea);
	HashMap<String, String> getAllCitys();
	
	
	
	List<String> getCitys(String busScheduleId);
	/**
	 * 获得某班次的服务（历经城市）
	 * @param busScheduleId
	 * @return
	 */
	JsonObj getService(String busScheduleId);
}
