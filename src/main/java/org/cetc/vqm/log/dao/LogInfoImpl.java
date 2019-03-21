package org.cetc.vqm.log.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.log.pojo.LogInfo;
import org.cetc.vqm.user.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LogInfoImpl implements LogInfoMapper {

	@Autowired
    private JdbcTemplate jdbcTemplate;

	
	@Override
	public void insertLog(LogInfo loginfo) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("INSERT INTO log(username,time,operation) VALUES (?,?,?)",loginfo.getUsername(),loginfo.getTime(),loginfo.getOperation());
	}


	@Override
	public float ListAllLogCount() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select count(*) from log", float.class);
	}


	@Override
	public List<Map<String, Object>> ListAllLog(int pagenum) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForList("select * from log order by logid limit ? ,10",new Object[] {pagenum});
	}


	@Override
	public float ListAllLogCount(Timestamp start, Timestamp end, String username) {
		// TODO Auto-generated method stub
		String sql = "";
		if(username != "")
		{
			if(start == null && end == null)
			{
				sql = "select count(*) from log where username = "+ username ;
			}
			else if(start != null && end == null)
			{
				sql = "select count(*) from log where username = "+ username + " and time >= '"+ start + "'";
			}
			else if(start == null && end != null)
			{
				sql = "select count(*) from log where username = "+ username + " and time <= '"+ end + "'";
			}
			else if(start != null && end != null)
			{
				sql = "select count(*) from log where username = "+ username + "' and time between '"+ start + "' and '" + end + "'";
			}
		}
		else if (username == "")
		{
			if(start == null && end == null)
			{
				sql = "select count(*) from log";
			}
			else if(start != null && end == null)
			{
				sql = "select count(*) from log where time >= '"+ start + "'";
			}
			else if(start == null && end != null)
			{
				sql = "select count(*) from log where time <= '"+ end + "'";
			}
			else if(start != null && end != null)
			{
				sql = "select count(*) from log where time between '"+ start + "' and '" + end + "'";
			}
		}
			
			
		int countmessage = jdbcTemplate.queryForObject(sql, Integer.class);
		return countmessage;
	}


	@Override
	public List<Map<String, Object>> ListAllLog(Timestamp start, Timestamp end, String username, int num) {
		// TODO Auto-generated method stub
		int a=(num-1)*10;
		
		String sql = "";
		if(username != "")
		{
			if(start == null && end == null)
			{
				sql = "select * from log where username = "+ username +" limit "+ a + ",10";
			}
			else if(start != null && end == null)
			{
				sql = "select * from log where username = "+ username + " and time >= '"+ start + "' limit "+ a + ",10";
			}
			else if(start == null && end != null)
			{
				sql = "select * from log where username = "+ username + " and time <= '"+ end +"' limit "+ a + ",10";
			}
			else if(start != null && end != null)
			{
				sql = "select * from log where username = "+ username + " and time between '"+ start + "' and '" + end +"' limit "+ a + ",10";
			}
		}
		else if (username == "")
		{
			if(start == null && end == null)
			{
				sql = "select * from log limit "+ a + ",10";
			}
			else if(start != null && end == null)
			{
				sql = "select * from log where time >= '"+ start + "' limit "+ a + ",10";
			}
			else if(start == null && end != null)
			{
				sql = "select * from log where time <= '"+ end +"' limit "+ a + ",10";
			}
			else if(start != null && end != null)
			{
				sql = "select * from log where time between '"+ start + "' and '" + end +"' limit "+ a + ",10";
			}
		}
		
		//String sql = "select * from log where username='"+ username + "' and time between'"+ start + "' and '" + end + "' limit "+ a + ",10";		
		List<Map<String, Object>> logs = jdbcTemplate.queryForList(sql);//, new BeanPropertyRowMapper<Photo>(Photo.class));
		return logs;
	}

}
