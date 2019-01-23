package tech.xiangcheng.orangebus.company.service;

import tech.xiangcheng.orangebus.company.domain.Station;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.company.dao.StationDao;;
@Service
public class StationServiceImpl implements StationService {
	@Autowired
	StationDao stationDao;
	@Override
	public Station save(Station station) {
		if (null == station || null == station.getSysArea()) return null;
		station.setId(RandomUtil.getId());
		return stationDao.save(station);
	}

}
