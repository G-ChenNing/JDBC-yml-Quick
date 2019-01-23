package tech.xiangcheng.orangebus.company.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;

public interface BusScheduleJdbcDao {

	public List<BusSchedule> getBusScheduleSelectByUser(String delFlag, String originalCode, String terminalCode,String fromDate,String toDate);
}
