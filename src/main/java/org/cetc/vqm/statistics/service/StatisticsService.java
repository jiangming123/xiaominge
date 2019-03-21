package org.cetc.vqm.statistics.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.statistics.dao.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StatisticsService {
	
	@Autowired
	StatisticsMapper statisticsMapper;
	

	public List<Map<String, Object>> ListStatistics(Timestamp start, Timestamp end) {
		// TODO Auto-generated method stub
		return statisticsMapper.ListStatistics(start,end);
	}


	public List<Map<String, Object>> ListFaultStatistics(Timestamp start, Timestamp end) {
		// TODO Auto-generated method stub
		return statisticsMapper.ListFaultStatistics(start,end);
	}


	public List<Map<String, Object>> NewListFaultStatistics(Timestamp start, Timestamp end, int chainid) {
		// TODO Auto-generated method stub
		return statisticsMapper.NewListFaultStatistics(start,end,chainid);
	}


	


}
