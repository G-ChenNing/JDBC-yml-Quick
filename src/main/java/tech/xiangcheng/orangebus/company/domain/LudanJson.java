/**
 * 
 */
package tech.xiangcheng.orangebus.company.domain;

import java.util.ArrayList;
import java.util.List;

//该班次、该车辆的路单
public class LudanJson {

	String busScheduleId="";
	String scheduleVehicleId="";
	//路单页面需要以下4个信息
	Double totalIncome=0.0;//总收入
	Double totalSpend=0.0;//总支出
	List<ScheduleVehicleSpend> spendList = new ArrayList<ScheduleVehicleSpend>();//支出列表
	List<TotalIncomeOBJ> incomeList= new ArrayList<TotalIncomeOBJ>();//收入列表

	
	public LudanJson(String busScheduleId, String scheduleVehicleId, Double totalIncome, Double totalSpend,
			List<ScheduleVehicleSpend> spendList, List<TotalIncomeOBJ> incomeList) {
		super();
		this.busScheduleId = busScheduleId;
		this.scheduleVehicleId = scheduleVehicleId;
		this.totalIncome = totalIncome;
		this.totalSpend = totalSpend;
		this.spendList = spendList;
		this.incomeList = incomeList;
	}


	public String getBusScheduleId() {
		return busScheduleId;
	}


	public void setBusScheduleId(String busScheduleId) {
		this.busScheduleId = busScheduleId;
	}


	public String getScheduleVehicleId() {
		return scheduleVehicleId;
	}


	public void setScheduleVehicleId(String scheduleVehicleId) {
		this.scheduleVehicleId = scheduleVehicleId;
	}


	public Double getTotalIncome() {
		return totalIncome;
	}


	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}


	public Double getTotalSpend() {
		return totalSpend;
	}


	public void setTotalSpend(Double totalSpend) {
		this.totalSpend = totalSpend;
	}


	public List<ScheduleVehicleSpend> getSpendList() {
		return spendList;
	}


	public void setSpendList(List<ScheduleVehicleSpend> spendList) {
		this.spendList = spendList;
	}


	public List<TotalIncomeOBJ> getIncomeList() {
		return incomeList;
	}


	public void setIncomeList(List<TotalIncomeOBJ> incomeList) {
		this.incomeList = incomeList;
	}




	
}
