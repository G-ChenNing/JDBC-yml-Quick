package tech.xiangcheng.orangebus.config.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigStationOnOff;

public interface ConfigStationOnOffDao extends JpaRepository<ConfigStationOnOff, String> {
	ConfigStationOnOff findById(String id);
}
