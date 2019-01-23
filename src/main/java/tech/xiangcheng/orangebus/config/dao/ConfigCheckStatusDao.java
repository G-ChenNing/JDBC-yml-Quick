package tech.xiangcheng.orangebus.config.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigCheckStatus;

public interface ConfigCheckStatusDao extends JpaRepository<ConfigCheckStatus, String> {
	ConfigCheckStatus findById(String id);
}
