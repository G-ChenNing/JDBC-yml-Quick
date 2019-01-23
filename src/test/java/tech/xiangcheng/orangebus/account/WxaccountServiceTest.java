package tech.xiangcheng.orangebus.account;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.account.domain.Wxaccount;
import tech.xiangcheng.orangebus.account.service.PersonalAccountService;
import tech.xiangcheng.orangebus.account.service.WxaccountService;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WxaccountServiceTest {

	@Autowired
	WxaccountService wxaccountService;
	@Autowired 
	PersonalAccountService personalAccountService;
	@Test
	@Transactional
	public void testFindByOpenid() {
		String openid = "123456";
		Wxaccount wxaccount = new Wxaccount(openid);
		wxaccount.setId("666");
		wxaccountService.save(wxaccount);
		assertNull(wxaccountService.getWxaccountByOpenid("123ewretry"));
		assertNotNull(wxaccountService.getWxaccountByOpenid(openid));
	}
	
	
	@Test
	@Transactional
	public void testRemoveBindToPersonalAccountByPersonalAccountId() {
		String openid = "123" + RandomUtil.getNonceStr();
		String phone = "456" + RandomUtil.getNonceStr();
		PersonalAccount personalAccount = new PersonalAccount(phone,null);
		personalAccount.setPhone(phone);
		personalAccount = personalAccountService.save(personalAccount);
		String id = personalAccount.getId();
		assertEquals(2, wxaccountService.removeBindToPersonalAccountByPersonalAccountId(id));
		Wxaccount wxaccount = new Wxaccount(openid);
		wxaccount.setOpenid(openid);
		wxaccount.setPersonalAccount(personalAccount);
		wxaccountService.save(wxaccount);
		assertEquals(1, wxaccountService.removeBindToPersonalAccountByPersonalAccountId(id));
		assertNull(wxaccountService.getWxaccountByOpenid(openid).getPersonalAccount());
	}
	
}
