package tech.xiangcheng.orangebus.company.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.company.domain.StationCoachNumber;

public interface StationCoachNumberDao extends JpaRepository<StationCoachNumber, String>{

	StationCoachNumber findById(String id);
}
