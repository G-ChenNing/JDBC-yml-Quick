package tech.xiangcheng.orangebus.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.WxaccountCheck;


public interface WxaccountCheckDao  extends JpaRepository<WxaccountCheck, String> {
	WxaccountCheck findByOpenid(String openid);
	WxaccountCheck findByCheckAccount_Id(String id);
}
