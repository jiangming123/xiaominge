package org.cetc.vqm.conference.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.conference.pojo.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ConferenceImpl implements ConferenceMapper{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public Conference getConference(int zoneid, String conferencename) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT a.conferenceid,a.zoneid,b.zonename,a.conferencename,a.serverip,a.time FROM conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid"
				+ " WHERE a.zoneid = ? and a.conferencename = ?";
		@SuppressWarnings("unchecked")
		List<Conference> conferenceList = jdbcTemplate.query(sql,new Object[] {zoneid,conferencename},new BeanPropertyRowMapper(Conference.class));
        
		//List<Conference> conferenceList = jdbcTemplate.query("select * from conference WHERE zoneid = ? and conferencename = ?",new Object[] {zoneid,conferencename},new BeanPropertyRowMapper(Conference.class));
        if(conferenceList != null && conferenceList.size() > 0){
        	Conference conference = conferenceList.get(0);
            return conference;
        }else {
        	System.out.println("no conference");
            return null;
        }

	}
	
	@Override
	public Conference getConference(String serverip) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT a.conferenceid,a.zoneid,b.zonename,a.conferencename,a.serverip,a.time FROM conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid"
				+ " WHERE a.serverip = ?";
		@SuppressWarnings("unchecked")
		List<Conference> conferenceList = jdbcTemplate.query(sql,new Object[] {serverip},new BeanPropertyRowMapper(Conference.class));
        
		//List<Conference> conferenceList = jdbcTemplate.query("select * from conference WHERE zoneid = ? and conferencename = ?",new Object[] {zoneid,conferencename},new BeanPropertyRowMapper(Conference.class));
        if(conferenceList != null && conferenceList.size() > 0){
        	Conference conference = conferenceList.get(0);
            return conference;
        }else {
        	System.out.println("no conference");
            return null;
        }

	}

	@Override
	public int addConference(Conference conference) {

		// TODO Auto-generated method stub
		//jdbcTemplate.update("INSERT INTO conference(zoneid,zonename,name,serverip,time) VALUES (?,?,?,?,?)",conference.getZoneid(),conference.getZonename(),conference.getName(),conference.getServerip(),conference.getTime());
		
		final String sql = "INSERT INTO conference(zoneid,conferencename,serverip,time) VALUES (?,?,?,?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();  
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
                                 //设置返回的主键字段名
				PreparedStatement ps = con.prepareStatement(sql,new String[]{ "id" }); 
				ps.setInt(1, conference.getZoneid());
				ps.setString(2, conference.getConferencename());
				ps.setString(3, conference.getServerip());
				ps.setTimestamp(4, new Timestamp(conference.getTime().getTime()));
				return ps;
			}
		}, keyHolder);
		
		
	return keyHolder.getKey().intValue();
}

	@Override
	public void editConference(int conferenceid, String conferencename, String serverip) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("UPDATE conference SET serverip = ?, conferencename = ? WHERE conferenceid = ?",
				serverip,conferencename,conferenceid);
	}

	@Override
	public void deleteConference(Integer conferenceid) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("DELETE FROM chain WHERE conferenceid = ?",conferenceid);
		jdbcTemplate.update("DELETE FROM conference WHERE conferenceid = ?",conferenceid);
	}

	@Override
	public Conference getConference(Integer conferenceid) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.conferenceid,a.zoneid,b.zonename,a.conferencename,a.serverip,a.time FROM conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid"
				+ " WHERE a.conferenceid = ?";
		@SuppressWarnings("unchecked")
		List<Conference> conferenceList = jdbcTemplate.query(sql,new Object[] {conferenceid},new BeanPropertyRowMapper(Conference.class));
        
		//List<Conference> conferenceList = jdbcTemplate.query("select * from conference WHERE id = ?",new Object[] {id},new BeanPropertyRowMapper(Conference.class));
        if(conferenceList != null && conferenceList.size() > 0){
        	Conference conference = conferenceList.get(0);
            return conference;
        }else {
        	System.out.println("no conference");
            return null;
        }
	}

	@Override
	public float getConferenceCount() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select count(*) from conference", float.class);
	}

	@Override
	public List<Map<String, Object>> ListAllConferences(int pagenum) {
		// TODO Auto-generated method stub

		String sql = "SELECT a.conferenceid,a.zoneid,b.zonename,a.conferencename,a.serverip,a.time FROM conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid"
				+ " order by a.zoneid, a.conferenceid limit ? ,10";
		return jdbcTemplate.queryForList(sql,new Object[] {pagenum});
		//return jdbcTemplate.queryForList("select * from conference order by zoneid, id limit ? ,10",new Object[] {pagenum});
	}

	@Override
	public float getConferenceCount(int zoneid) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select count(*) from conference WHERE zoneid = ?",new Object[] {zoneid}, float.class);
	}

	@Override
	public List<Map<String, Object>> ListAllConference(int pagenum, int zoneid) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.conferenceid,a.zoneid,b.zonename,a.conferencename,a.serverip,a.time FROM conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid"
				+ " where a.zoneid = ? order by a.conferenceid limit ? ,10";
		return jdbcTemplate.queryForList(sql,new Object[] {zoneid,pagenum});
		
		//return jdbcTemplate.queryForList("select * from conference WHERE zoneid = ? order by id limit ? ,10",new Object[] {zoneid,pagenum});
	}

	@Override
	public List<Map<String, Object>> getConferenceInfoByServerip(String serverip) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.conferenceid, a.conferencename, b.zonename from conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid where serverip = ?";
		return jdbcTemplate.queryForList (sql,new Object[] {serverip});
        
	}

	@Override
	public List<Map<String, Object>> ListAllConference(int zoneid) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.conferenceid,a.zoneid,b.zonename,a.conferencename,a.serverip,a.time FROM conference a LEFT JOIN warzone b ON a.zoneid = b.zoneid"
				+ " where a.zoneid = ? order by a.conferenceid";
		return jdbcTemplate.queryForList(sql,new Object[] {zoneid});
	}


}
