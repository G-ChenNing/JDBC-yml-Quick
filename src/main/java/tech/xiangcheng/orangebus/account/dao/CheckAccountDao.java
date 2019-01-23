package tech.xiangcheng.orangebus.account.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.CheckAccount;

public interface CheckAccountDao extends JpaRepository<CheckAccount, String> {
	CheckAccount findFirst1ByNameAndPassword(String name, String password);
	CheckAccount findFirst1ByName(String name);
	CheckAccount findById(String id);
	List<CheckAccount> findBySysOffice_Name(String name);
}
