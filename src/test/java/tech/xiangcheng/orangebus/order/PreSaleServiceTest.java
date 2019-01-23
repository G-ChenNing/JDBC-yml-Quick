package tech.xiangcheng.orangebus.order;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
import tech.xiangcheng.orangebus.order.service.PreSaleService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PreSaleServiceTest {

	@Autowired
	PreSaleService preSaleService;
	@Test
	public void testGetStatisticMsg() {
		JsonObj jo = preSaleService.getStatisticMsg("65fa044ffd404240afbae9b13c9605ca");
		assertEquals(jo.getProperty("preSaleNum"), new Long(0));
	}

}
