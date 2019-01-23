package tech.xiangcheng.orangebus.company.service;

import tech.xiangcheng.orangebus.company.domain.SysOffice;
import tech.xiangcheng.orangebus.leaf.util.random.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.xiangcheng.orangebus.company.dao.SysOfficeDao;;
@Service
public class SysOfficeServiceImpl implements SysOfficeService {
	@Autowired
	SysOfficeDao sysOfficeDao;
	@Override
	public SysOffice save(SysOffice sysOffice) {
		if (null == sysOffice || null == sysOffice.getSysArea()) return null;
		sysOffice.setId(RandomUtil.getId());
		return sysOfficeDao.save(sysOffice);
	}

}
