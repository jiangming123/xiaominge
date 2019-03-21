package org.cetc.vqm.statistics.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface StatisticsMapper {
	
	List<Map<String, Object>> ListStatistics(Timestamp start, Timestamp end);

	List<Map<String, Object>> ListFaultStatistics(Timestamp start, Timestamp end);

	List<Map<String, Object>> NewListFaultStatistics(Timestamp start, Timestamp end, int chainid);



}
