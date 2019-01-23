package tech.xiangcheng.orangebus.config.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.config.dao.ConfigSpendNameDao;
import tech.xiangcheng.orangebus.config.domain.ConfigSpendName;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class ConfigSpendNameServiceImpl implements ConfigSpendNameService {
	@Autowired
	ConfigSpendNameDao configSpendNameDao;
	@Override
	public List<ConfigSpendName> getNameList() {
		List<ConfigSpendName> configSpendNames = configSpendNameDao.findByDelFlagOrderBySort(Constant.NOT_DELTED);
		return configSpendNames;
	}

}
