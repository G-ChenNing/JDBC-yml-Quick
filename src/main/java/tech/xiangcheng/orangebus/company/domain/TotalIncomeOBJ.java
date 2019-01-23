package tech.xiangcheng.orangebus.company.domain;

public class TotalIncomeOBJ {

	
	private String incomeType;
	private int amount;
	private double price;
	public TotalIncomeOBJ(String incomeType, int amount, double price) {
		super();
		this.incomeType = incomeType;
		this.amount = amount;
		this.price = price;
	}
	
	public String getIncomeType() {
		return incomeType;
	}
	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
