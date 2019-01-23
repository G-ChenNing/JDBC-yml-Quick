package tech.xiangcheng.orangebus.company;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tech.xiangcheng.orangebus.address.domain.SysArea;
import tech.xiangcheng.orangebus.address.service.SysAreaService;
import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.company.domain.CoachNumber;
import tech.xiangcheng.orangebus.company.domain.Station;
import tech.xiangcheng.orangebus.company.domain.SysOffice;
import tech.xiangcheng.orangebus.company.service.BusScheduleService;
import tech.xiangcheng.orangebus.company.service.CoachNumberService;
import tech.xiangcheng.orangebus.company.service.StationService;
import tech.xiangcheng.orangebus.company.service.SysOfficeService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BusScheduleServiceTest {
	@Autowired
	BusScheduleService busScheduleService;
	@Autowired
	CoachNumberService coachNumberService;
	@Autowired
	StationService stationService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	SysOfficeService sysOfficeService;
	@Test
	@Transactional
	public void testGetBusScheduleById() {
		assertNotNull(busScheduleService.getBusScheduleById("a66e0199631f421aaaabbd6c3fc6eeb7"));
		assertNull(busScheduleService.getBusScheduleById(null));
		BusSchedule busSchedule = new BusSchedule();
		CoachNumber coachNumber = new CoachNumber();
		Station station = new Station();
		SysArea sysArea = new SysArea();
		sysArea = sysAreaService.save(sysArea);
		assertNotNull(sysArea);
		station.setSysArea(sysArea);
		station = stationService.save(station);
		assertNotNull(station);
//		coachNumber.setOriginalStation(station);
//		coachNumber.setTerminalStation(station);
		SysOffice sysOffice = new SysOffice();
		sysOffice.setSysArea(sysArea);
		sysOffice.setGrade("1");
		sysOffice.setType("1");
		sysOffice = sysOfficeService.save(sysOffice);
		assertNotNull(sysOffice);
		coachNumber.setSysOffice(sysOffice);
		coachNumber = coachNumberService.save(coachNumber);
		assertNotNull(coachNumber);
		busSchedule.setCoachNumber(coachNumber);
		busSchedule.setDelFlag("1");
		BusSchedule savedBusSchedule = busScheduleService.save(busSchedule);
		assertNotNull(savedBusSchedule);
		assertNull(busScheduleService.getBusScheduleById(savedBusSchedule.getId()));
		savedBusSchedule.setDelFlag("0");
		busScheduleService.update(savedBusSchedule);
		assertNotNull(busScheduleService.getBusScheduleById(savedBusSchedule.getId()));
	}

	/**
	 * 建立在数据库有相关数据的基础上
	 * @throws ParseException
	 */
	@Test
	//@Ignore("建立在数据库有相关数据的基础上")
	public void testFindBusSchedule() throws ParseException {
		String originalStationCode = "430200", terminalStationCode = "430100", departureDate = "2017-7-1";
		List<BusSchedule> bss = busScheduleService.findByBusScheduleByOriginalStationAreaCodeAndTerminalStationAreaCodeAndDepatureDate(originalStationCode, terminalStationCode, departureDate);
		assertEquals(2, bss.size());
	}
	
	@Test
	@Ignore("建立在数据库有相关数据的基础上")
	public void testFindByCheckAccountAndDepartDate() throws ParseException {
		String id = "4c7e12ca160340eba657ec2f867c7458";
		String date = "2017-7-1";
		List<BusSchedule> bs = busScheduleService.getBusScheduleByCheckAccountIdAndDepartDate(id, date);
		assertEquals(2, bs.size());
	}
}
