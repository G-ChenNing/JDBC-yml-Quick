package tech.xiangcheng.orangebus.company.service;

import tech.xiangcheng.orangebus.company.domain.CoachNumber;

public interface CoachNumberService {
	/**
	 * 
	 * @param coachNumber
	 * @return 保存失败返回null
	 */
	CoachNumber save(CoachNumber coachNumber);
}
