package tech.xiangcheng.orangebus.account.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.coupon.domain.CouponGroup;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;

public interface CouponGroupDao extends JpaRepository<CouponGroup, String> {

	@Query("select cg from CouponGroup cg where cg.useTimes>0 and cg.signInSend=:signInSend and cg.endTime>=:today and cg.delFlag=:delFlag")
	List<CouponGroup> findSignInCoupon(@Param("signInSend") int signInSend, @Param("today") Date today,@Param("delFlag")String delFlag);
	
}
