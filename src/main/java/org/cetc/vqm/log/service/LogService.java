package org.cetc.vqm.log.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.log.dao.LogInfoMapper;
import org.cetc.vqm.log.pojo.LogInfo;
import org.cetc.vqm.user.dao.UserInfoMapper;
import org.cetc.vqm.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	
	@Autowired
    LogInfoMapper logInfoMapper;
	


	public void insertLog(LogInfo loginfo) {
		// TODO Auto-generated method stub
		logInfoMapper.insertLog(loginfo);
	}



	public float ListAllLogCount() {
		// TODO Auto-generated method stub
		return logInfoMapper.ListAllLogCount();
	}


	public List<Map<String, Object>> ListAllLog(int pagenum) {
		// TODO Auto-generated method stub
		return logInfoMapper.ListAllLog(pagenum);
	}

	public float ListAllLogCount(Timestamp start, Timestamp end, String username) {
		// TODO Auto-generated method stub
		return logInfoMapper.ListAllLogCount(start,end,username);
	}

	public List<Map<String, Object>> ListAllLog(Timestamp start, Timestamp end, String username, int num) {
		// TODO Auto-generated method stub
		return logInfoMapper.ListAllLog(start,end,username,num);
	}


	


}
