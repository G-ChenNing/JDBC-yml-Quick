package tech.xiangcheng.orangebus.account.service;


import java.util.List;

import tech.xiangcheng.orangebus.account.domain.Wxaccount;

public interface WxaccountService {
	/**
	 * @param wxaccount 不设置id
	 * @return 若wxaccount为null或其openid为null则返回null
	 */
	Wxaccount save(Wxaccount wxaccount);

	/**
	 * 通过openid判断用户是否存在
	 * @param openid
	 * @return
	 */
	Wxaccount getWxaccountByOpenid(String openid);
	/**
	 * 根据账号id查找其绑定微信账号
	 * @param personalAccountId
	 * @return
	 */
	Wxaccount getWxaccountByPersonalAccountId(String personalAccountId);

	/**
	 * 返回所有wxaccount
	 * @return
	 */
	List<Wxaccount> getAllWxaccounts();
	/**
	 * 解除与personalAccount的绑定
	 * @param id
	 * @return 2 代表当前账号没有绑定微信，1代表解绑成功
	 */
	int removeBindToPersonalAccountByPersonalAccountId(String id);

	int deleteWxaccountByPersonalAccountId(String id);

	int deleteWxaccountByPersonalAccountPhone(String phone);
}
