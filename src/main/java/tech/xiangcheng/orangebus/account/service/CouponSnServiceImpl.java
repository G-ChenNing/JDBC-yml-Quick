package tech.xiangcheng.orangebus.account.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.CouponGroupDao;
import tech.xiangcheng.orangebus.account.dao.CouponSnDao;
import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.coupon.domain.CouponGroup;
import tech.xiangcheng.orangebus.coupon.domain.CouponSn;
import tech.xiangcheng.orangebus.leaf.util.json.JsonUtil;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.leaf.util.time.TimeUtil;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class CouponSnServiceImpl implements CouponSnService {

	@Autowired
	CouponSnDao couponSnDao;

	@Autowired
	CouponGroupDao couponGroupDao;
	
	@Override
	public List<CouponSn> findCouponSnByUserId(String userid,String type) throws ParseException {
		
//		return couponSnDao.findByPersonalAccount_IdAndDelFlag(userid, Constant.NOT_DELTED);
		if ("unused".equals(type)) {
			return couponSnDao.findValidCouponByUserId(userid, Constant.getNowDateYMDDate(),Constant.NOT_DELTED);
		}
		if("used".equals(type)){
			return couponSnDao.findUsedCouponByUserId(userid, Constant.NOT_DELTED);
		}
		if("expired".equals(type)){
			return couponSnDao.findExpiredCouponByUserId(userid, Constant.getNowDateYMDDate(),Constant.NOT_DELTED);
		}
		return null;
	}

	@Override
	public CouponSn findById(String id) {
		return couponSnDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
	}
	@Override
	public Long getCountCouponSnByUserId(String userid) throws ParseException{
		  Date dt=new Date();
		     SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		     String date=  matter1.format(dt);
		     //Date today = matter1.parse(date);  
		     String dates="str_to_date('"+date+"', '%Y-%m-%d')";
		//couponSnDao.getCountCouponSnByUserId(userid, today);
		return couponSnDao.getCountCouponSnByUserId(userid, dates);
	}

	@Override
	public boolean useCouponOnce(CouponSn couponSn) {
		int minusTimes =couponSn.getRemainTimes()-1;
		if(minusTimes>=0){
			couponSn.setRemainTimes(minusTimes);
			couponSnDao.save(couponSn);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public long getCouponSnNumByUserId(String userid) {
		if (null == userid || userid.isEmpty()) return 0;
		
		 Date dt=new Date();
	     SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
	     String date=  matter1.format(dt);
	     //Date today = matter1.parse(date);  
//	     String dates="str_to_date('"+date+"', '%Y-%m-%d')";
	     String dates = date;
		return couponSnDao.getCountCouponSnByUserId(userid, dates);
	}

	@Override
	public List<CouponGroup> getSignInCouponGroupList() throws ParseException{
		List<CouponGroup> couponGroupList=couponGroupDao.findSignInCoupon(Constant.SIGNINSEND,Constant.getNowDateYMDDate(),Constant.NOT_DELTED);
		return couponGroupList;
	}

	@Override
	public CouponSn addCouponSn(PersonalAccount account, CouponGroup couponGroup) {
		if (null == account || null == couponGroup) {
			return null;
		}
		CouponSn cs = new CouponSn();
		cs.setId(RandomUtil.getId());
		cs.setCouponGroup(couponGroup);
		cs.setDelFlag(Constant.NOT_DELTED);
		cs.setCode("cp" + TimeUtil.getTimeStamp());
		cs.setCreateDate(new Date());
		cs.setPersonalAccount(account);
		cs.setRemainTimes(couponGroup.getUseTimes());
		return couponSnDao.save(cs);
	}
	
	@Override
	public void setBuyerSignInCouponSnZero(String personalAccountId){
		couponSnDao.setBuyerSignInCouponSnZero(personalAccountId);
	}

	//wcn20171019
	@Override
	public void refundCouponOnce(CouponSn couponSn) {
		int Times =couponSn.getRemainTimes()+1;
		couponSn.setRemainTimes(Times);
		couponSnDao.save(couponSn);
	}

}
