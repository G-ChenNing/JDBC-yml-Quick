package tech.xiangcheng.orangebus.account;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tech.xiangcheng.orangebus.account.service.CouponSnService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CouponsnServiceTest {

	@Autowired
	CouponSnService couponSnService;
	
	@Test
//	@Ignore("数据库中有测试数据方可")
	public void testGetCouponSnNum() {
		long count = couponSnService.getCouponSnNumByUserId("94907d30-f009-4806-bcb4-3960d20dfe26");
		System.out.println(count);
//		assertTrue(count > 0);
	}

}
