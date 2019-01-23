package tech.xiangcheng.orangebus.company.service;
import tech.xiangcheng.orangebus.company.domain.SysOffice;
public interface SysOfficeService {
	/**
	 * 
	 * @param sysOffice
	 * @return 保存失败时返回null
	 */
	SysOffice save(SysOffice sysOffice);
}
