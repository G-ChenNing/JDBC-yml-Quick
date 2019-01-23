package tech.xiangcheng.orangebus.company;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tech.xiangcheng.orangebus.company.domain.ScheduleVehicle;
import tech.xiangcheng.orangebus.company.service.ScheduleVehicleService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ScheduleVehicleServiceTest {
	@Autowired
	ScheduleVehicleService scheduleVehicleService;

	@Test
	public void testGetScheduleVehicleByBusScheduleId() {
		assertNull(scheduleVehicleService.getScheduleVehicleByBusScheduleId(null));
		List<ScheduleVehicle> scheduleVehicles = scheduleVehicleService.getScheduleVehicleByBusScheduleId("a66e0199631f421aaaabbd6c3fc6eeb7");
		assertNotNull(scheduleVehicles);
	}

}
