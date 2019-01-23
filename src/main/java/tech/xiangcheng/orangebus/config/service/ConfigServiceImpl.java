package tech.xiangcheng.orangebus.config.service;



import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.config.dao.ConfigCheckStatusDao;
import tech.xiangcheng.orangebus.config.dao.ConfigPayMethodDao;
import tech.xiangcheng.orangebus.config.dao.ConfigPayStatusDao;
import tech.xiangcheng.orangebus.config.dao.ConfigPriceDao;
import tech.xiangcheng.orangebus.config.dao.ConfigSourceDao;
import tech.xiangcheng.orangebus.config.dao.ConfigStationOnOffDao;
import tech.xiangcheng.orangebus.config.domain.ConfigCheckStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigPayMethod;
import tech.xiangcheng.orangebus.config.domain.ConfigPayStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigSource;
import tech.xiangcheng.orangebus.config.domain.ConfigStationOnOff;
import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	ConfigCheckStatusDao configCheckStatusDao;
	@Autowired
	ConfigStationOnOffDao configStationOnOffDao;
	@Autowired
	ConfigPayMethodDao configPayMethodDao;
	@Autowired
	ConfigPayStatusDao configPayStatusDao;
	@Autowired 
	ConfigSourceDao configSourceDao;
	@Autowired
	ConfigPriceDao configPriceDao;
	
	
	@Override
	public JsonObj getAllSource() {
		Stream <ConfigSource> css = configSourceDao.findByDelFlag(Constant.NOT_DELTED);
		Function<ConfigSource, String> mapper = c -> {
			JsonObj j = JsonObj.instance();
			j.putProperty("id", c.getId());
			j.putProperty("name", c.getName());
			return j.toJson();
		};
		List<String> res = css.map(mapper).collect(Collectors.toList());
		JsonObj jo = JsonObj.instance();
		jo.putProperty("sources", res);
		return jo;
	}

	@Override
	public ConfigSource getSourceByName(String name) {
		return configSourceDao.findByNameAndDelFlag(name, Constant.NOT_DELTED);
	}

	@Override
	public ConfigSource getSourceById(String id) {
		return configSourceDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
	}
	
	@Override
	public ConfigPayStatus findById_configPayStatus(String id) {
		configPayStatusDao.findById(id);
		return null;
	}

	@Override
	public ConfigPayMethod findById_configPayMethod(String id) {
		configPayMethodDao.findById(id);
		return null;
	}

	@Override
	public ConfigCheckStatus findById_configCheckStatus(String id) {
		configCheckStatusDao.findById(id);
		return null;
	}

	@Override
	public ConfigStationOnOff findById_configStationOnOff(String id) {
		configStationOnOffDao.findById(id);
		return null;
	}

	@Override
	public ConfigPrice findConfigPriceById(String id){
		return configPriceDao.findById(id);
	}
	
}
