package tech.xiangcheng.orangebus.account.service;

import tech.xiangcheng.orangebus.account.domain.PersonalRole;

public interface PersonalRoleService {
	
	PersonalRole findByPersonalAccount_idAndDelFlag(String id);
	PersonalRole findByPersonalAccount_phoneAndDelFlag(String phone);
}
