package tech.xiangcheng.orangebus.config.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.coupon.domain.ConfigPrice;

public interface ConfigPriceDao extends JpaRepository<ConfigPrice, String> {
	ConfigPrice findById(String id);
}
