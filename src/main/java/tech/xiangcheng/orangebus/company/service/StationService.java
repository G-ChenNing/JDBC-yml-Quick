package tech.xiangcheng.orangebus.company.service;

import tech.xiangcheng.orangebus.company.domain.Station;

public interface StationService {
	/**
	 * 
	 * @param station
	 * @return 保存失败返回null
	 */
	Station save(Station station);
}
