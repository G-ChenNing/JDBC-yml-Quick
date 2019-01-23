package tech.xiangcheng.orangebus.account.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.PersonalRole;

public interface PersonalRoleDao extends JpaRepository<PersonalRole, String> {
	PersonalRole findByPersonalAccount_idAndDelFlag(String id, String notDelted);
	PersonalRole findByPersonalAccount_phoneAndDelFlag(String phone, String notDelted);
}
