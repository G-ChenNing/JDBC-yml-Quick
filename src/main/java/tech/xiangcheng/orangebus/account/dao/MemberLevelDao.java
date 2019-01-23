package tech.xiangcheng.orangebus.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.xiangcheng.orangebus.account.domain.MemberLevel;

public interface MemberLevelDao extends JpaRepository<MemberLevel, String> {
	MemberLevel findById(String id);
}
