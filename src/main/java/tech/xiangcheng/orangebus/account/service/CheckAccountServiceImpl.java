package tech.xiangcheng.orangebus.account.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.CheckAccountDao;
import tech.xiangcheng.orangebus.account.domain.CheckAccount;
@Service
public class CheckAccountServiceImpl implements CheckAccountService {

	@Autowired
	CheckAccountDao checkAccountDao;
	@Override
	public CheckAccount getCheckAccountByNameAndPassword(String name, String password) {
		if (null == name || name.isEmpty() || null == password || password.isEmpty())
			return null;
		return checkAccountDao.findFirst1ByNameAndPassword(name, password);
	}
	@Override
	public CheckAccount getCheckAccountByName(String name) {
		if (null == name || name.isEmpty()) 
			return null;
		return checkAccountDao.findFirst1ByName(name);
	}
	@Override
	public int login(String name, String password) {
		if (null == name || name.isEmpty() || null == password || password.isEmpty()) 
			return -2;//用户名不存在
		if (null == checkAccountDao.findFirst1ByNameAndPassword(name, password)) {
			if (null == checkAccountDao.findFirst1ByName(name))
				return -2;//用户名不存在
			return -1;//用户名或密码错误
		} else {
			return 0;//登陆成功
		}
	}
	@Override
	public List<CheckAccount> getCheckAccoutsBySysOfficeName(String name) {
		List<CheckAccount> accounts = new ArrayList<>();
		if (null != name && !name.isEmpty()) 
			accounts = checkAccountDao.findBySysOffice_Name(name);
		return accounts;
	}
	@Override
	public CheckAccount getCheckAccountByid(String id) {
		return checkAccountDao.findById(id);
	}

}
