package tech.xiangcheng.orangebus.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.company.dao.CoachNumberDao;
import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
@Service
public class CoachNumberServiceImpl implements CoachNumberService {

	@Autowired
	CoachNumberDao coachNumberDao;
	@Override
	public CoachNumber save(CoachNumber coachNumber) {
		if (null == coachNumber || 
//				null == coachNumber.getOriginalStation() || 
//				null == coachNumber.getTerminalStation() ||
				null == coachNumber.getSysOffice()) 
			return null;
		coachNumber.setId(RandomUtil.getId());
		return coachNumberDao.save(coachNumber);
	}

}
