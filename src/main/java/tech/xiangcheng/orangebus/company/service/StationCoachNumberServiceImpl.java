package tech.xiangcheng.orangebus.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.company.dao.CoachNumberDao;
import tech.xiangcheng.orangebus.company.dao.StationCoachNumberDao;
import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.company.domain.StationCoachNumber;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
@Service
public class StationCoachNumberServiceImpl implements StationCoachNumberService {

	@Autowired
	StationCoachNumberDao stationCoachNumberDao;

	@Override
	public StationCoachNumber getStationCoachNumberById(String stationCoachNumberId) {
		// TODO Auto-generated method stub
		return stationCoachNumberDao.findById(stationCoachNumberId);
	}

	
}
