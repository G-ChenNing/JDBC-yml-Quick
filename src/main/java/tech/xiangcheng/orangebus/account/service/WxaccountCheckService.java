package tech.xiangcheng.orangebus.account.service;

import tech.xiangcheng.orangebus.account.domain.WxaccountCheck;

public interface WxaccountCheckService {
	/**
	 * 通过openid判断用户是否存在
	 * @param openid
	 * @return
	 */
	WxaccountCheck getWxaccountCheckByOpenid(String openid);
	
	WxaccountCheck getWxaccountByCheckAccountId(String id);
	/**
	 * 
	 * @param check
	 * @return
	 */
	WxaccountCheck saveOrUpdate(WxaccountCheck check);

	int deleteWxaccountCheckByCheckAccountId(String id);

}
