package tech.xiangcheng.orangebus.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.WxaccountCheckDao;
import tech.xiangcheng.orangebus.account.domain.Wxaccount;
import tech.xiangcheng.orangebus.account.domain.WxaccountCheck;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;

@Service
public class WxaccountCheckServiceImpl implements WxaccountCheckService {
	@Autowired
	WxaccountCheckDao dao;
	@Override
	public WxaccountCheck getWxaccountCheckByOpenid(String openid) {
		if (null == openid || "".equals(openid)) {
			return null;
		}
		return dao.findByOpenid(openid);
	}
	@Override
	public WxaccountCheck saveOrUpdate(WxaccountCheck check) {
		if (null == check || null == check.getOpenid() || null == check.getCheckAccount()) {
			return null;
		}
		WxaccountCheck wxaccountCheckByAccountId= getWxaccountByCheckAccountId(check.getCheckAccount().getId());
		if (null == wxaccountCheckByAccountId) {
			check.setId(RandomUtil.getId());
		} else {
			check.setId(wxaccountCheckByAccountId.getId());
		}
		return dao.save(check);
	}
	@Override
	public WxaccountCheck getWxaccountByCheckAccountId(String id) {
		if (null == id || "".equals(id))
			return null;
		return dao.findByCheckAccount_Id(id);
	}
	
	@Override
	public int deleteWxaccountCheckByCheckAccountId(String id){
		WxaccountCheck wxaccountCheck = dao.findByCheckAccount_Id(id);
		if ( null == wxaccountCheck)
			return 2;
		else {
			dao.delete(wxaccountCheck);
			return 1;
		}
	}

}
