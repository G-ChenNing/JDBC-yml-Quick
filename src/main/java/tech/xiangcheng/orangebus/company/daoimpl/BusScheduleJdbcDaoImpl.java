package tech.xiangcheng.orangebus.company.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import tech.xiangcheng.orangebus.DBHelper;
import tech.xiangcheng.orangebus.company.dao.BusScheduleDao;
import tech.xiangcheng.orangebus.company.dao.BusScheduleJdbcDao;
import tech.xiangcheng.orangebus.company.domain.BusSchedule;
import tech.xiangcheng.orangebus.order.domain.enumType.ConfigEnum;
import tech.xiangcheng.orangebus.parent.domain.Constant;

public class BusScheduleJdbcDaoImpl implements BusScheduleJdbcDao {
	
	@Autowired
	BusScheduleDao busScheduleDao;
	
    static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;
	@Override
	public List<BusSchedule> getBusScheduleSelectByUser(String delFlag, String originalCode, String terminalCode,
			String fromDate, String toDate) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
