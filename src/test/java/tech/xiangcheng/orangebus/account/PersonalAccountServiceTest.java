package tech.xiangcheng.orangebus.account;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tech.xiangcheng.orangebus.account.domain.PersonalAccount;
import tech.xiangcheng.orangebus.account.service.PersonalAccountService;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@SpringApplicationConfiguration(classes = CommonApplication.class)
public class PersonalAccountServiceTest {

	@Autowired
	PersonalAccountService personalAccountService;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Transactional
	public void testSave() {
		assertNull(personalAccountService.save(null));
		String phone = "123" + RandomUtil.getNonceStr();
		PersonalAccount personalAccount = new PersonalAccount(null,null);
		assertNull(personalAccountService.save(personalAccount));
		personalAccount.setPhone(phone);
		assertNotNull(personalAccountService.save(personalAccount));
	}

	@Transactional
	@Test
	public void testGetAccountByPhone() {
		String phone = "123";
		PersonalAccount personalAccount = new PersonalAccount(phone,null);
		personalAccount = personalAccountService.save(personalAccount);
		assertNotNull(personalAccountService.getAccountByPhone(phone));
	}
	
	@Transactional
	@Test
	public void testGetAccountById() {
		String phone = "123" + RandomUtil.getNonceStr();
		PersonalAccount personalAccount = new PersonalAccount(phone,null);
		personalAccount = personalAccountService.save(personalAccount);
		String id = personalAccount.getId();
		assertNotNull(personalAccountService.getAccountById(id));	
	}
	
	@Transactional
	@Test
	public void testUpgrade() {
		personalAccountService.iFmemberOneUpgrade("e2d1e1fd-3ab3-43f1-af34-071b0c2c1d74");
	}

}
