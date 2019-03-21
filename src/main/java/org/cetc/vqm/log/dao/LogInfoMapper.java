package org.cetc.vqm.log.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.log.pojo.LogInfo;

public interface LogInfoMapper {
	

	void insertLog(LogInfo loginfo);

	float ListAllLogCount();

	List<Map<String, Object>> ListAllLog(int pagenum);

	float ListAllLogCount(Timestamp start, Timestamp end, String username);

	List<Map<String, Object>> ListAllLog(Timestamp start, Timestamp end, String username, int num);

	

}
