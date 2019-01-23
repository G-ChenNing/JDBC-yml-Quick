package tech.xiangcheng.orangebus.account.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;

public interface CouponSnDao extends JpaRepository<CouponSn, String> {
	List<CouponSn> findByPersonalAccount_IdAndDelFlag(String id, String delFlag);

	CouponSn findByIdAndDelFlag(String id, String notDelted);
	
	
	@Query(value = "select count(*) from coupon_sn sn, coupon_group cg "
			+ "where sn.personal_account_id =:userid and sn.remain_times>0 and "
			+ "sn.coupon_group_id = cg.id and cg.start_time<=:today and cg.end_time>=:today",  nativeQuery = true)
	long getCountCouponSnByUserId(@Param("userid") String userid,@Param("today") String today);
	
	@Query("select cps from CouponSn cps where cps.personalAccount.id=:userid and cps.remainTimes>0 and cps.couponGroup.endTime>=:today and cps.delFlag=:delFlag")
	List<CouponSn> findValidCouponByUserId(@Param("userid") String userid, @Param("today") Date today,@Param("delFlag")String delFlag);
	
	@Query("select cps from CouponSn cps where cps.personalAccount.id=:userid and cps.remainTimes=0 and cps.delFlag=:delFlag")
	List<CouponSn> findUsedCouponByUserId(@Param("userid") String userid, @Param("delFlag")String delFlag);
	
	@Query("select cps from CouponSn cps where cps.personalAccount.id=:userid and cps.remainTimes>0 and cps.couponGroup.endTime<:today and cps.delFlag=:delFlag")
	List<CouponSn> findExpiredCouponByUserId(@Param("userid") String userid, @Param("today") Date today,@Param("delFlag")String delFlag);
	
	@Transactional
	@Modifying
	@Query(value = " update coupon_sn cs, personal_account pa,coupon_group cg SET cs.remarks = cs.remain_times, cs.remain_times = 0 "
			+ " where cs.personal_account_id= pa.id and cs.coupon_group_id = cg.id and cg.sign_in_send = 1 and pa.id= :personalAccountId and cs.remain_times != 0 ", nativeQuery = true)
	void setBuyerSignInCouponSnZero(@Param("personalAccountId")String personalAccountId);
	
}
