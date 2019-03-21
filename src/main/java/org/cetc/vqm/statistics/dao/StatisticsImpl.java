package org.cetc.vqm.statistics.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsImpl implements StatisticsMapper{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> ListStatistics(Timestamp start, Timestamp end) {
		// TODO Auto-generated method stub
		String sql = "SELECT chainnumber, chainname, GROUP_CONCAT(result.total) AS totals, GROUP_CONCAT(result.date) AS dates FROM (SELECT COUNT(*)"
				+ " AS total, b.chainnumber AS chainnumber, b.chainname AS chainname, DATE_FORMAT(a.time, '%Y-%m-%d') AS DATE  FROM chainstate a " + 
				"LEFT JOIN CHAIN b ON a.chainid = b.chainid WHERE a.time >= ? and a.time <= ? and a.chainstate < 4 GROUP BY b.chainnumber, "
				+ "DATE_FORMAT(a.time, '%Y-%m-%d')) result GROUP BY chainnumber order by dates ASC";
		
		//String sql = "select count(*) as total, b.chainnumber, b.chainname, DATE_FORMAT(a.time, '%Y-%m-%d') as date  FROM chainstate a "
		//		+ "LEFT JOIN CHAIN b ON a.chainid = b.chainid WHERE a.time >= ? and a.time <= ? and a.chainstate < 4 "
		//		+ "GROUP BY b.chainnumber, DATE_FORMAT(a.time, '%Y-%m-%d')";
		
		return jdbcTemplate.queryForList(sql,new Object[] {start, end});
	}

	@Override
	public List<Map<String, Object>> ListFaultStatistics(Timestamp start, Timestamp end) {
		// TODO Auto-generated method stub
		String sql = "select a.chainid,a.chaintype,b.chainnumber, b.chainname,count(*) as counts from fault a left join chain b on "
				+ "a.chainid = b.chainid where a.time >= ? and a.time <= ? group by a.chainid";
		
		return jdbcTemplate.queryForList(sql,new Object[] {start, end});
	}


	@Override
	public List<Map<String, Object>> NewListFaultStatistics(Timestamp start, Timestamp end, int chainid) {
		// TODO Auto-generated method stub

		String sql = "SELECT " + 
				  "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND blur < '4') AS blur, " + 
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND overdark < '4' ) AS overdark, " +
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND overbright < '4' ) AS overbright, " +
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND colorcast < '4' ) AS colorcast, " +
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND lowcontrast < '4' ) AS lowcontrast, " +
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND mosaic >= 1 and mosaic < '4' ) AS mosaic, " +
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND blackscreen >= 1 and blackscreen < '4' ) AS blackscreen, " +
			      "(SELECT COUNT(*) FROM fault WHERE chainid = ? and time >= ? and time <= ? AND frozen >= 1 and frozen < '4' ) AS frozen";
		
		/*String sql = "select a.chainid, b.chainumber, b.chainname, " +
				"       case when blur < 4 then 0 when blur >= 4 then 1 end as blur," + 
				"		CASE WHEN overdark < 4 THEN 0 WHEN overdark >= 4 THEN 1 END AS overdark," + 
				"		CASE WHEN overbright < 4 THEN 0 WHEN overbright >= 4 THEN 1 END AS overbright," + 
				"		CASE WHEN colorcast < 4 THEN 0 WHEN colorcast >= 4 THEN 1 END AS colorcast," + 
				"		CASE WHEN lowcontrast < 4 THEN 0 WHEN lowcontrast >= 4 THEN 1 END AS lowcontrast," + 
				"		CASE WHEN mosaic < 4 THEN 0 WHEN mosaic >= 4 THEN 1 END AS mosaic," + 
				"		CASE WHEN blackscreen < 4 THEN 0 WHEN blackscreen >= 4 THEN 1 END AS blackscreen," + 
				"		CASE WHEN frozen < 4 THEN 0 WHEN frozen >= 4 THEN 1 END AS frozen " +
				"       from fault a left join chain b on a.chain = b.chain where " +
				"       a.chainid = ?" + 
				"		union select  '合计', (select count(*) as cou from fault where blur < '4' )," + 
				"				      (select count(*) as cou from fault where overdark < '4' )," + 
				"				      (SELECT COUNT(*) AS cou FROM fault WHERE overbright < '4' )," + 
				"				      (SELECT COUNT(*) AS cou FROM fault WHERE colorcast < '4' )," + 
				"				      (SELECT COUNT(*) AS cou FROM fault WHERE lowcontrast < '4' )," + 
				"				      (SELECT COUNT(*) AS cou FROM fault WHERE mosaic < '4' )," + 
				"				      (SELECT COUNT(*) AS cou FROM fault WHERE blackscreen < '4' )," + 
				"				      (SELECT COUNT(*) AS cou FROM fault WHERE frozen < '4' ) from fault";*/
		
		return jdbcTemplate.queryForList(sql,new Object[] {chainid,start, end,chainid,start, end,
				chainid,start, end,chainid,start, end,chainid,start, end,chainid,start, end,chainid,start, end,chainid,start, end});
	}

	

}
