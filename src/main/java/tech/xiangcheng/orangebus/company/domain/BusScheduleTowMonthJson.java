package tech.xiangcheng.orangebus.company.domain;

/*
 * 封装类，返回两个月内车次的价格范围
 */
public class BusScheduleTowMonthJson {
	private String date;
	private Double minPrice;
	private Double maxPrice;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}
	public Double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
	
}
