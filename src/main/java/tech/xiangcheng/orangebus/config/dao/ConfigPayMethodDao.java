package tech.xiangcheng.orangebus.config.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigPayMethod;

public interface ConfigPayMethodDao extends JpaRepository<ConfigPayMethod, String> {
	ConfigPayMethod findById(String id);
}
