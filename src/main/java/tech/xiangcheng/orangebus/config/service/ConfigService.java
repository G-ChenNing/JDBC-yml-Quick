package tech.xiangcheng.orangebus.config.service;


import tech.xiangcheng.orangebus.config.domain.ConfigCheckStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigPayMethod;
import tech.xiangcheng.orangebus.config.domain.ConfigPayStatus;
import tech.xiangcheng.orangebus.config.domain.ConfigSource;
import tech.xiangcheng.orangebus.config.domain.ConfigStationOnOff;
import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;
import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;

public interface ConfigService {
	ConfigPayStatus findById_configPayStatus(String id);
	ConfigPayMethod findById_configPayMethod(String id);
	ConfigCheckStatus findById_configCheckStatus(String id);
	ConfigStationOnOff findById_configStationOnOff(String id);
	JsonObj getAllSource();
	ConfigSource getSourceByName(String name);
	ConfigSource getSourceById(String id);
	ConfigPrice findConfigPriceById(String id);
}
