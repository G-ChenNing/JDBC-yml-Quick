package tech.xiangcheng.orangebus.account.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.SvCheck;

public interface SvCheckDao  extends JpaRepository<SvCheck, String> {
	List<SvCheck> findFirst1ByScheduleVehicle_idAndRole(String id,Integer role);
	List<SvCheck> findByScheduleVehicle_id(String id);
	List<SvCheck> findByCheckAccount_id(String id);
}
