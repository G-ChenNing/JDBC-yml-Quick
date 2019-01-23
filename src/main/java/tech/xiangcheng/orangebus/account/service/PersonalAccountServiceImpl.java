package tech.xiangcheng.orangebus.account.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.PersonalAccountDao;
import tech.xiangcheng.orangebus.account.domain.MemberLevel;
import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.account.domain.PersonalAccountJson;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.order.service.PassengerBusOrderService;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class PersonalAccountServiceImpl implements PersonalAccountService {

	@Autowired
	PersonalAccountDao personalAccountDao;
	@Autowired
	PassengerBusOrderService passengerBusOrderService;
	@Autowired
	CouponSnService couponSnService;
	@Override
	public PersonalAccount findValidAccountByCode(String code) {
		return personalAccountDao.findByCodeAndDelFlag(code, Constant.NOT_DELTED);
	}
	@Override
	public PersonalAccount save(PersonalAccount personalAccount) {
		if (null == personalAccount || null == personalAccount.getPhone() || null != personalAccountDao.findByPhone(personalAccount.getPhone())) 
			return null;
		personalAccount.setId(RandomUtil.getId());
		personalAccount.setDelFlag(Constant.NOT_DELTED);
		personalAccount.setPoints(0.0);
//		System.out.println(personalAccount);
		return personalAccountDao.save(personalAccount);
	}
	@Override
	public PersonalAccount update(PersonalAccount personalAccount) {
		if (null == personalAccount || null == personalAccount.getId() || null == personalAccount.getPhone() || 
				null == getAccountById(personalAccount.getId()) || null == getAccountByPhone(personalAccount.getPhone()))
			return null;
		return personalAccountDao.save(personalAccount);
	}
	@Override
	public PersonalAccount getAccountByPhone(String phone) {
		return personalAccountDao.findByPhoneAndDelFlag(phone, Constant.NOT_DELTED);
	}
	@Override
	public PersonalAccount getAccountById(String id) {
		return personalAccountDao.findByIdAndDelFlag(id, Constant.NOT_DELTED);
	}
	
	@Override
	@Deprecated
	public String iFmemberOneUpgrade(String accountId){
		PersonalAccount account = this.getAccountById(accountId);
		boolean ifUpgade = false;
		if(account==null || account.getMemberLevel()==null ){
			return "-1";
		}
		if(account.getMemberLevel().getId().equals(ConfigEnum.MEMBERONE.getValue())){
			Double spentMoney = passengerBusOrderService.getSumSpentByUserId(accountId);
			if(spentMoney>=account.getMemberLevel().getUpgradeCredit()){
//				account.getMemberLevel().setId(ConfigEnum.MEMBERTWO.getValue());
				MemberLevel memberLevel=new MemberLevel();
				memberLevel.setId(ConfigEnum.MEMBERTWO.getValue());
				account.setMemberLevel(memberLevel);
				try{
					account=personalAccountDao.save(account);
					ifUpgade=true;
				}catch(Exception e){
					ifUpgade=false;
				}
				
			}
		}
		if(ifUpgade==true){
			return ConfigEnum.MEMBERTWO.getValue();
		}else{
			return "-1";
		}
	}
	
	@Override
	public PersonalAccountJson getAccountJsonById(String id) {
		PersonalAccount account = this.getAccountById(id);
//		Double spentMoney = passengerBusOrderService.getSumSpentByUserId(id);
		Long couponSnNum = null;
		try {
			couponSnNum = couponSnService.getCountCouponSnByUserId(id);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		PersonalAccountJson accountJson = new PersonalAccountJson(account);
		accountJson.setCouponSnNum(couponSnNum);
//		accountJson.setSpentMoney(spentMoney);
		return accountJson;
	}
	//wangchenning
	@Override
	public PersonalAccount updateAccountLevel(String id,String newAccountLevel) {
		PersonalAccount personalAccount = this.getAccountById(id);
		personalAccount.getMemberLevel().setId(newAccountLevel);
		return personalAccountDao.save(personalAccount);
	}

}
