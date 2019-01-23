package tech.xiangcheng.orangebus.generalcrud;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.fasterxml.jackson.core.JsonProcessingException;

import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GeneralServiceTest {

	@Autowired
	GeneralService gs;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Ignore("id is primary key")
	@Test
	public void testCreate() throws SQLException {
		gs.create("testCreate", JsonObj.instance().putProperty("id", "123").putProperty("name", "test3"));
	}
	
	@Test
	public void testUpdate() throws SQLException {
		gs.update("testUpdate", JsonObj.instance().putProperty("id", "123").putProperty("name", "updateTest3"));
	}

	@Test
	public void testSelect() throws JsonProcessingException, SQLException {
		System.out.println(gs.select("testSelect", JsonObj.instance().putProperty("id", "123")));
	}
	@Test
	public void testDelete() throws SQLException {
		gs.delete("testDelete", JsonObj.instance().putProperty("id", "123"));
	}
}
