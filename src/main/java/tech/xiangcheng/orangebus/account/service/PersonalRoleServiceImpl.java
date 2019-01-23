package tech.xiangcheng.orangebus.account.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.account.dao.PersonalRoleDao;
import tech.xiangcheng.orangebus.account.domain.PersonalRole;
import tech.xiangcheng.orangebus.parent.domain.Constant;
@Service
public class PersonalRoleServiceImpl implements PersonalRoleService {

	@Autowired
	PersonalRoleDao personalRoleDao;

	@Override
	public PersonalRole findByPersonalAccount_idAndDelFlag(String id) {
		// TODO Auto-generated method stub
		return personalRoleDao.findByPersonalAccount_idAndDelFlag(id, Constant.NOT_DELTED);
	}

	@Override
	public PersonalRole findByPersonalAccount_phoneAndDelFlag(String phone) {
		// TODO Auto-generated method stub
		return personalRoleDao.findByPersonalAccount_phoneAndDelFlag(phone, Constant.NOT_DELTED);
	}

}
