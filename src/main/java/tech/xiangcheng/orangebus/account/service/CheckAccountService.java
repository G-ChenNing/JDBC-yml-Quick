package tech.xiangcheng.orangebus.account.service;

import java.util.List;

import tech.xiangcheng.orangebus.account.domain.CheckAccount;

public interface CheckAccountService {
	/**
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	CheckAccount getCheckAccountByNameAndPassword(String name, String password);
	/**
	 * 
	 * @param name
	 * @return
	 */
	CheckAccount getCheckAccountByName(String name);
	
	CheckAccount getCheckAccountByid(String id);
	/**
	 * 
	 * @param name
	 * @param password
	 * @return 0成功登陆，-1用户名或密码错误，-2用户名不存在
	 */
	int login(String name, String password);
	/**
	 * 
	 * @param name
	 * @return 
	 */
	List<CheckAccount> getCheckAccoutsBySysOfficeName(String name);
}
