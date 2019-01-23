package tech.xiangcheng.orangebus.company.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.company.domain.Station;

public interface StationDao extends JpaRepository<Station, String> {

}
