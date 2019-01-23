package tech.xiangcheng.orangebus.config.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigSpendName;

public interface ConfigSpendNameDao extends JpaRepository<ConfigSpendName, String>{
	List<ConfigSpendName>  findByDelFlagOrderBySort(String delFlag);
}
