package tech.xiangcheng.orangebus.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tech.xiangcheng.orangebus.account.domain.CheckAccount;
import tech.xiangcheng.orangebus.account.domain.WxaccountCheck;
import tech.xiangcheng.orangebus.account.service.CheckAccountService;
import tech.xiangcheng.orangebus.account.service.WxaccountCheckService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WxaccountCheckTest {
	@Autowired
	WxaccountCheckService wxcheckAccountService;
	@Autowired
	CheckAccountService checkAccountService;
	@Test
	public void  testSaveOrUpdate() {
		CheckAccount account = checkAccountService.getCheckAccountByName("cb001");
		WxaccountCheck check = new WxaccountCheck("hiiii");
		check.setCheckAccount(account);
		wxcheckAccountService.saveOrUpdate(check);
	}
}
