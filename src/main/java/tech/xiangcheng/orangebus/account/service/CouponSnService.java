package tech.xiangcheng.orangebus.account.service;

import java.text.ParseException;
import java.util.List;

import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.coupon.domain.CouponGroup;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;

public interface CouponSnService {
	
	/**
	 * 根据userid查找该用户的代金券（delFlag为0）
	 * @param userid
	 * @return
	 * @throws ParseException 
	 */
	List<CouponSn> findCouponSnByUserId(String userid,String type) throws ParseException;
	CouponSn findById(String id);
	
	void refundCouponOnce (CouponSn couponSn);
	boolean useCouponOnce (CouponSn couponSn);
	/**
	 * 发放代金券
	 * @param account
	 * @param couponGroup
	 * @return
	 */
	CouponSn addCouponSn(PersonalAccount account, CouponGroup couponGroup);
	/**
	 * 
	 * @param userid
	 * @return userid为null或为空时返回值0
	 */
	long getCouponSnNumByUserId(String userid);
	Long getCountCouponSnByUserId(String userid) throws ParseException;
	List<CouponGroup> getSignInCouponGroupList() throws ParseException;
	void setBuyerSignInCouponSnZero(String personalAccountId);
}
