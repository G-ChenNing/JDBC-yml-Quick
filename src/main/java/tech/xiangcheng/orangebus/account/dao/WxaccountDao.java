package tech.xiangcheng.orangebus.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.Wxaccount;

public interface WxaccountDao extends JpaRepository<Wxaccount, String> {
	Wxaccount findByOpenid(String openid);
	Wxaccount findByPersonalAccount_Id(String id);
	Wxaccount findById(String id);
	Wxaccount findByPersonalAccount_phone(String phone);
}
