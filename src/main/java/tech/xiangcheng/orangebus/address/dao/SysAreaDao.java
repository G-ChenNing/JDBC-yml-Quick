package tech.xiangcheng.orangebus.address.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.address.domain.SysArea;

public interface SysAreaDao extends JpaRepository<SysArea, String> {
	List<SysArea> findNameByNameStartingWith(String letter);
	List<SysArea> findNameByCodeStartingWith(String letter);
	List<SysArea> findByTypeAndDelFlag(String type, String delFlag);
	
	@Query(value = "SELECT distinct s.name from SYS_AREA as s inner join STATION as sta on s.id = sta.AREA_ID  where sta.id in ("
			+ "SELECT STATION_ID from STATION_COACH_NUMBER where on_off in :onOffs and coach_number_id = "
			+ "(SELECT coach_number_id from BUS_SCHEDULE where id =:id limit 1)) order by s.name;", nativeQuery = true)
	Stream<String>  getCitysByBusScheduleId(@Param(value = "id") String id, @Param(value = "onOffs") String[] onOffs);
}
