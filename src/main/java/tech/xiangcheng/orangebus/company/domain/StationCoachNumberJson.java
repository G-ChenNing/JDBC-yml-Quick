/**
 * 
 */
package tech.xiangcheng.orangebus.company.domain;


/**
 *  包装stationCoachnumber返回给前端
 * @author yang
 *
 */
public class StationCoachNumberJson {
	String id="";
	int rank = 0;
	String name = "";
	Integer duration = 0;//停留时间,单位：分钟
	String onOff = "";// 0 出发站；1 上车站；2 下车站； 3 终点站；
	String city="";
	String cityCode="";
	String address = "";
	String stationId = "";
	public StationCoachNumberJson(StationCoachNumber stationCoachNumber) {
		if (null == stationCoachNumber) return;
		id= stationCoachNumber.getId() ==null ? null: stationCoachNumber.getId();
		rank = stationCoachNumber.getRank() == null ? 0 : stationCoachNumber.getRank();
		duration = stationCoachNumber.getDuration() == null ? 0 : stationCoachNumber.getDuration();
		onOff = stationCoachNumber.getOnOff() == null ? "" : stationCoachNumber.getOnOff().getId();
		Station station = stationCoachNumber.getStation();
		if (null == station) return;
		name = station.getName() == null ? "" : station.getName();
		stationId = station.getId() == null ? "" : station.getId();
		city = null == station.getSysArea() ? "" : station.getSysArea().getName();
		cityCode = null == station.getSysArea() ? "" : station.getSysArea().getCode();
		address= null == station.getDetail() ? "" :station.getDetail();
	}
	
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public int getRank() {
		return rank;
	}



	public void setRank(int rank) {
		this.rank = rank;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getOnOff() {
		return onOff;
	}
	public void setOnOff(String onOff) {
		this.onOff = onOff;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}



	public String getCityCode() {
		return cityCode;
	}



	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	
}
