package tech.xiangcheng.orangebus.company.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.company.domain.Vehicle;

public interface VehicleDao extends JpaRepository<Vehicle, String>{
	List<Vehicle> findByDelFlagOrderByName(String delFlag);
	Vehicle findByIdAndDelFlag(String id, String delFlag);
	
	@Query("select v from Vehicle v, CheckAccountStationCoach casc where "
			+ "casc.checkAccount.id=:id and casc.vehicle.id=v.id and v.delFlag=:delFlag and casc.delFlag=:delFlag "
			+ " order by v.name")
	List<Vehicle> findByCheckAccountIdAndDelFlag(@Param("id") String id, @Param("delFlag") String delFlag);
}
