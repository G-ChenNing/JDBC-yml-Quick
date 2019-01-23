package tech.xiangcheng.orangebus.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.PersonalAccount;

public interface PersonalAccountDao extends JpaRepository<PersonalAccount, String> {
	PersonalAccount findById(String id);
	PersonalAccount findByCodeAndDelFlag(String code, String flag);
	PersonalAccount findByPhone(String phone);
	PersonalAccount findByPhoneAndDelFlag(String phone, String delFlag);
	PersonalAccount findByIdAndDelFlag(String id, String delFlag);
	PersonalAccount findByNameAndDelFlag(String name, String delFlag);
}
