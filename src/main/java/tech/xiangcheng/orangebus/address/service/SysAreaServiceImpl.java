package tech.xiangcheng.orangebus.address.service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.address.dao.SysAreaDao;
import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class SysAreaServiceImpl implements SysAreaService {

	@Autowired
	SysAreaDao sysAreaDao;
	@Override
	public List<String> getCitysNameByCityNameFirstLetter(String letter) {
		//TODO 直接通过自定义sql只返回name
		letter = letter.toUpperCase();
		return sysAreaDao.findNameByCodeStartingWith(letter).stream().map(sysArea
				-> sysArea.getName()).collect(Collectors.toList());
	}
	@Override
	public SysArea save(SysArea sysArea) {
		sysArea.setId(RandomUtil.getId());
		sysArea.setDelFlag(Constant.NOT_DELTED);
		return sysAreaDao.save(sysArea);
	}
	@Override
	public HashMap<String, String> getAllCitys(){
		HashMap<String, String> allCitys =new HashMap<String, String>();
		
		String cityType=ConfigEnum.CITY.getValue();
		String notDel=Constant.NOT_DELTED;
		List<SysArea> citys=sysAreaDao.findByTypeAndDelFlag(cityType,notDel);
		citys.forEach(city->allCitys.put(city.getCode(),city.getName()));
		return allCitys;
	}
	@Override
	public List<String> getCitys(String busScheduleId) {
		String[] onOffs={ConfigEnum.GETOFF.getValue(),ConfigEnum.TERMINAL.getValue()};
		return this.sysAreaDao.getCitysByBusScheduleId(busScheduleId,onOffs).collect(Collectors.toList());
	}
	@Override
	public JsonObj getService(String busScheduleId) {
		List<String> citys = this.getCitys(busScheduleId);
		JsonObj jo = JsonObj.instance();
		jo.putProperty("services", citys);
		return jo;
	}

	
	
}
