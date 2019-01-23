package tech.xiangcheng.orangebus.address;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.address.service.SysAreaService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SysAreaServiceTest {

	@Autowired
	SysAreaService sysAreaService;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Transactional
	public void testGetCitysNameByCityNameFirstLetter() {
		int num = 10;
		for (int i = 0; i < num; ++i) {
			SysArea sysArea = new SysArea();
			String string = "随便";
			String code = "xity";
			sysArea.setName(string + i);
			sysArea.setCode(code);
			sysAreaService.save(sysArea);
		}
		SysArea sysArea = new SysArea();
		String string = "ity";
		sysArea.setName(string);
		sysAreaService.save(sysArea);
		assertTrue(sysAreaService.getCitysNameByCityNameFirstLetter("x").size() >= 10);
	}

	
	@Test
	public void testGetCitys() {
		List<String> citys = this.sysAreaService.getCitys("00322d81edf040218f11267368ac217d");
		for (String city : citys) {
			System.out.println(city);
		}
		assertTrue(!citys.isEmpty());
		
	}
}
