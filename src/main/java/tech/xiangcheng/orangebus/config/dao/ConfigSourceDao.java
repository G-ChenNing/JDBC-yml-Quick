package tech.xiangcheng.orangebus.config.dao;


import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigSource;

public interface ConfigSourceDao extends JpaRepository<ConfigSource, String> {

	Stream<ConfigSource> findByDelFlag(String delFlag);
	ConfigSource findByNameAndDelFlag(String name, String delFlag);
	ConfigSource findByIdAndDelFlag(String id, String delFlag);
}
