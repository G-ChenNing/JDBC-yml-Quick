package tech.xiangcheng.orangebus.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.PersonalAccountDao;
import tech.xiangcheng.orangebus.account.dao.WxaccountDao;
import tech.xiangcheng.orangebus.account.domain.Wxaccount;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
@Service
public class WxaccountServiceImpl implements WxaccountService {
	@Autowired
	WxaccountDao wxaccountDao;
	@Autowired
	PersonalAccountDao personalAccountDao;
	@Override
	public Wxaccount save(Wxaccount wxaccount) {
		if (null == wxaccount || null == wxaccount.getOpenid())
			return null;
		Wxaccount wxaccountByOpenid = getWxaccountByOpenid(wxaccount.getOpenid());
		if (null != wxaccountByOpenid) {
			wxaccount.setId(wxaccountByOpenid.getId());
		} else {
			wxaccount.setId(RandomUtil.getId());
		}
		return wxaccountDao.save(wxaccount);
	}
	@Override
	public Wxaccount getWxaccountByOpenid(String openid) {
		return wxaccountDao.findByOpenid(openid);
	}
	
	@Override
	public List<Wxaccount> getAllWxaccounts() {
		return wxaccountDao.findAll();
	}
	@Override
	public int removeBindToPersonalAccountByPersonalAccountId(String id) {
		Wxaccount wxaccount = wxaccountDao.findByPersonalAccount_Id(id);
		if ( null == wxaccount)
			return 2;
		else {
			wxaccount.setPersonalAccount(null);
			wxaccountDao.save(wxaccount);
			return 1;
		}
	}
	@Override
	public int deleteWxaccountByPersonalAccountId(String id){
		Wxaccount wxaccount = wxaccountDao.findByPersonalAccount_Id(id);
		if ( null == wxaccount)
			return 2;
		else {
			wxaccountDao.delete(wxaccount);
			return 1;
		}
	}
	
	@Override
	public int deleteWxaccountByPersonalAccountPhone(String phone){
		Wxaccount wxaccount = wxaccountDao.findByPersonalAccount_phone(phone);
		if ( null == wxaccount)
			return 2;
		else {
			wxaccountDao.delete(wxaccount);
			return 1;
		}
	}
	
	
	@Override
	public Wxaccount getWxaccountByPersonalAccountId(String personalAccountId) {
		if (null == personalAccountId) return null;
		return wxaccountDao.findByPersonalAccount_Id(personalAccountId);
	}

}
