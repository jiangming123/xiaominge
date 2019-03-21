package org.cetc.vqm.chain.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.agent.control.VideoSourceNetWorker;
import org.cetc.vqm.chain.pojo.Chain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChainImpl implements ChainMapper{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public void addChains(Chain chain, int chainnum) {
		// TODO Auto-generated method stub
		final List<String> chainnames = new ArrayList<String>();
		String chainname = chain.getChainname();
		int conferenceid = chain.getConferenceid();
		Date time = chain.getTime();
		for(int j = 0; j < chainnum; j++)
		{
			String name = chainname + String.valueOf(j+1);
			chainnames.add(name);
		}

			String sql = "INSERT IGNORE INTO chain(chainnumber,chainname,type,conferenceid,chainconferenceid,time,isOn) VALUES (?,?,?,?,?)";
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@SuppressWarnings("null")
			public int getBatchSize() {
				return chainnames.size(); //这个方法设定更新记录数，通常List里面存放的都是我们要更新的，所以返回list.size()；
			}
		
		@SuppressWarnings("null")
		public void setValues(PreparedStatement ps, int i)  throws SQLException {
			ps.setInt(1,i);
			ps.setString(2, chainnames.get(i));
			//ps.setString(3, "本地" + String.valueOf(i));
			if(i <= 10)	ps.setString(3, "本地" + String.valueOf(i));
			else ps.setString(3, "远端" + String.valueOf(i-10));
			ps.setInt(4, conferenceid);
			ps.setInt(5, conferenceid);
			ps.setTimestamp(6, new Timestamp(time.getTime()));
			ps.setInt(7, 0);
		}
		});
	}

	@Override
	public float getChainCount() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select count(*) from chain", float.class);
	}

	@Override
	public List<Map<String, Object>> ListAllChains(int pagenum) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.chainconferenceid,c.conferencename,a.time FROM CHAIN a "
				+ "LEFT JOIN conference c ON a.chainconferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid "
				+ " order by b.zoneid, c.conferenceid, a.chainid limit ? ,10";
		return jdbcTemplate.queryForList(sql,new Object[] {pagenum});
		
		//return jdbcTemplate.queryForList("select * from chain order by zoneid, conferenceid, chainid limit ? ,10",new Object[] {pagenum});
	}

	@Override
	public Chain getChain(int zoneid, int conferenceid, String chainname) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.conferenceid,c.conferencename,a.time FROM CHAIN a "
				+ "LEFT JOIN conference c ON a.conferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid "
				+ " WHERE b.zoneid = ? and c.conferenceid = ? and a.chainname = ?";
		@SuppressWarnings("unchecked")
		List<Chain> chainList = jdbcTemplate.query(sql,new Object[] {zoneid,conferenceid,chainname},new BeanPropertyRowMapper(Chain.class));
        
		//List<Chain> chainList = jdbcTemplate.query("select * from chain WHERE zoneid = ? and conferenceid = ? and chainname = ?",new Object[] {zoneid,conferenceid,chainname},new BeanPropertyRowMapper(Chain.class));
        if(chainList != null && chainList.size() > 0){
        	Chain chain = chainList.get(0);
            return chain;
        }else {
        	System.out.println("no chain");
            return null;
        }
	}

	@Override
	public void addChain(Chain chain) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("INSERT INTO chain(chainname,type,conferenceid,time) VALUES (?,?,?,?)",
				chain.getChainname(),chain.getType(),chain.getConferenceid(),chain.getTime());
	}

	@Override
	public float getChainCount(int zoneid, int conferenceid) {
		// TODO Auto-generated method stub
			
		if(conferenceid < 0)
			return jdbcTemplate.queryForObject("select count(*) from chain a LEFT JOIN conference c ON a.conferenceid = c.conferenceid LEFT "
					+ "JOIN warzone b ON c.zoneid = b.zoneid where b.zoneid = ?", new Object[] {zoneid},float.class);
		else return jdbcTemplate.queryForObject("select count(*) from chain a LEFT JOIN conference c ON a.conferenceid = c.conferenceid LEFT "
				+ " JOIN warzone b ON c.zoneid = b.zoneid where b.zoneid = ? and c.conferenceid = ?", new Object[] {zoneid, conferenceid},float.class); 
	}


	@Override
	public List<Map<String, Object>> ListAllChains(int pagenum, int zoneid, int conferenceid) {
		// TODO Auto-generated method stub
		
		String sql = "";
		
		if(conferenceid < 0)
		{
			sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.conferenceid,c.conferencename,a.time FROM CHAIN a " 
					+ "LEFT JOIN conference c ON a.chainconferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid"
					+ " where b.zoneid = ? order by b.zoneid, c.conferenceid, a.chainid limit ? ,10";
			
			return jdbcTemplate.queryForList(sql,new Object[] {zoneid, pagenum});
			//return jdbcTemplate.queryForList("select * from chain order where zoneid = ? order by zoneid, conferenceid, chainid limit ? ,10",new Object[] {zoneid, pagenum});
		}
		else
		{
			sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.conferenceid,c.conferencename,a.time FROM CHAIN a " 
					+ "LEFT JOIN conference c ON a.chainconferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid"
					+ " where b.zoneid = ? and c.conferenceid = ? order by b.zoneid, c.conferenceid, a.chainid limit ? ,10";
			return jdbcTemplate.queryForList(sql,new Object[] {zoneid, conferenceid, pagenum});
			
			//return jdbcTemplate.queryForList("select * from chain order where zoneid = ? and conferenceid = ? order by zoneid, conferenceid, chainid limit ? ,10",new Object[] {zoneid, conferenceid, pagenum});
	
		}
	}

	@Override
	public void editChain(int chainid, int chainconferenceid, String chainname, String type , Date time) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("UPDATE chain SET chainconferenceid = ?, chainname = ?, type = ?, time = ? WHERE chainid = ?",
				chainconferenceid,chainname,type,time,chainid);
	}

	@Override
	public Chain getChain(Integer chainid) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.conferenceid,c.conferencename,a.time FROM CHAIN a "
		+ "LEFT JOIN conference c ON a.conferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid "
				+ " WHERE a.chainid = ?";
		@SuppressWarnings("unchecked")
		List<Chain> chainList = jdbcTemplate.query(sql,new Object[] {chainid},new BeanPropertyRowMapper(Chain.class));
        
		//List<Chain> chainList = jdbcTemplate.query("select * from chain WHERE zoneid = ? and conferenceid = ? and chainname = ?",new Object[] {zoneid,conferenceid,chainname},new BeanPropertyRowMapper(Chain.class));
        if(chainList != null && chainList.size() > 0){
        	Chain chain = chainList.get(0);
            return chain;
        }else {
        	System.out.println("no chain");
            return null;
        }
	}

	@Override
	public void deleteChain(Integer chainid) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("DELETE FROM chain WHERE chainid = ?",chainid);
	}

	@Override
	public int getMaxChainNumber(int zoneid, int conferenceid) {
		// TODO Auto-generated method stub

		return jdbcTemplate.queryForObject("select MAX(chainnumber) from chain a LEFT JOIN conference c ON a.conferenceid = c.conferenceid LEFT "
			+ "JOIN warzone b ON c.zoneid = b.zoneid where b.zoneid = ?", new Object[] {zoneid,conferenceid},int.class);

	}

	@Override
	public List<Map<String, Object>> ListActiveConferenceChains(int conferenceid) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.conferenceid,c.conferencename,a.time FROM chain a "
				+ "LEFT JOIN conference c ON a.chainconferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid "
				+ "  where a.conferenceid = ? and a.isOn = ? order by a.chainnumber";
		return jdbcTemplate.queryForList(sql,new Object[] {conferenceid,1});
		
	}
	
	@Override
	public List<Map<String, Object>> ListInActiveConferenceChains(int conferenceid) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT a.chainid,a.chainnumber,a.chainname,a.type,b.zoneid,b.zonename,a.conferenceid,c.conferencename,a.time FROM chain a "
				+ "LEFT JOIN conference c ON a.chainconferenceid = c.conferenceid LEFT JOIN warzone b ON c.zoneid = b.zoneid "
				+ "  where a.conferenceid = ? and a.isOn = ? order by a.chainnumber";
		return jdbcTemplate.queryForList(sql,new Object[] {conferenceid,0});
		
	}

	@Override
	public void addListActiveConferenceChains(int conferenceid, String chainids) {
		// TODO Auto-generated method stub		
		
		String kk = chainids;    //假设从文本框获取的值是字符串kk
        String[] b = kk.split(",");    //将字符串中的","除去后存入数组里
        String endstr = "";
        for (int i = 0; i < b.length; i++)  //根据数组的元素个数判断循环次数
        {
            kk = "'" + b[i] + "'";  //在每个元素前后加上我们想要的格式，效果例如：
            // " 'tt' "
            if (i < b.length - 1)  //根据数组元素的个数来判断应该加多少个逗号
            {
                kk += ",";
            }
            endstr += kk;
        }
		jdbcTemplate.update("UPDATE chain SET isOn = ? WHERE conferenceid = ? and chainid in(" + endstr + ")",
				1,conferenceid);
	}

	@Override
	public void deleteListActiveConferenceChains(int conferenceid, int chainid) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("UPDATE chain SET isOn = ? WHERE conferenceid = ? and chainid = ?",
				0,conferenceid,chainid);
		
	}
		
}
	

