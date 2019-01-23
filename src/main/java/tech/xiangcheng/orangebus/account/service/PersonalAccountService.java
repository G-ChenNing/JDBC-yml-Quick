package tech.xiangcheng.orangebus.account.service;

import tech.xiangcheng.orangebus.account.domain.MemberLevel;
import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.account.domain.PersonalAccountJson;

public interface PersonalAccountService {
	/**
	 * 根据code查找有效账号（delFlag为0）
	 * @param code
	 * @return
	 */
	PersonalAccount findValidAccountByCode(String code);
	/**
	 * 若返回null则代表保存失败
	 * 当传入phone为null或已存在时则会保存失败，保证账号的手机号唯一
	 * @param personalAccount
	 * @return
	 */
	PersonalAccount save(PersonalAccount personalAccount);
	/**
	 * 
	 * @param personalAccount id有效
	 * @return
	 */
	PersonalAccount update(PersonalAccount personalAccount);
	/**
	 * 根据phone查找账号
	 * @param phone
	 * @return
	 */
	PersonalAccount getAccountByPhone(String phone);
	/**
	 * 根据id查找账号
	 * @param id
	 * @return
	 */
	PersonalAccount getAccountById(String id);
	/**
	 * 根据id返回accountjson
	 * @param id
	 * @return
	 */
	PersonalAccountJson getAccountJsonById(String id);
	/**
	 * 根据id更新用户等级
	 * @param id newAccountLevel
	 * @return
	 */
	PersonalAccount updateAccountLevel(String id,String newAccountLevel);
	String iFmemberOneUpgrade(String accountId);
}
