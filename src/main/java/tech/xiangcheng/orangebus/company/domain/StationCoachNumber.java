package tech.xiangcheng.orangebus.company.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import tech.xiangcheng.orangebus.config.domain.ConfigStationOnOff;
import tech.xiangcheng.orangebus.parent.domain.DataEntity;

/**
 * @author 
 *
 */
@Entity
public class StationCoachNumber extends DataEntity {

	public StationCoachNumber() {
	}

	@Id
	protected String id;
	
	protected Integer rank;//站点到达排序123
	protected Integer duration;//从上一个站到此站大概需要多久，单位分钟
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "on_off")
	@JsonBackReference
	protected ConfigStationOnOff onOff;//上车站点还是下车站点，1是上车站，2是下车站
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COACH_NUMBER_ID")
	@JsonBackReference 
	protected CoachNumber coachNumber;
	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "STATION_ID")
	@JsonBackReference 
	protected Station station;




	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public Integer getDuration() {
		return duration;
	}


	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	public CoachNumber getCoachNumber() {
		return coachNumber;
	}


	public void setCoachNumber(CoachNumber coachNumber) {
		this.coachNumber = coachNumber;
	}


	public Station getStation() {
		return station;
	}


	public void setStation(Station station) {
		this.station = station;
	}


	public ConfigStationOnOff getOnOff() {
		return onOff;
	}


	public void setOnOff(ConfigStationOnOff onOff) {
		this.onOff = onOff;
	}
	
}