package tech.xiangcheng.orangebus.config.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.config.domain.ConfigRefund;

public interface ConfigRefundDao extends JpaRepository<ConfigRefund, String> {
//	List<ConfigRefund> findAll();
}
