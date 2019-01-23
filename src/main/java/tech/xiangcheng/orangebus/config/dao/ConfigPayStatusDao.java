package tech.xiangcheng.orangebus.config.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigPayStatus;

public interface ConfigPayStatusDao extends JpaRepository<ConfigPayStatus, String> {
	ConfigPayStatus findById(String id);
}
