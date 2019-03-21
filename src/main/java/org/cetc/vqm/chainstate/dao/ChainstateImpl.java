package org.cetc.vqm.chainstate.dao;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.chain.pojo.Chain;
import org.cetc.vqm.chainstate.pojo.Chainstate;
import org.cetc.vqm.server.ChainDiagnoseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChainstateImpl implements ChainstateMapper{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public int getConferenceIDbyServerIP(String serverip) {
		
		String sql = "SELECT conferenceid from conference where serverip = ?";
		return jdbcTemplate.queryForObject(sql,new Object[] {serverip}, int.class);
	}
	
	public Chain getchain(int conferenceid, int chainnumber) {
		String sql = "SELECT chainid, type FROM chain where conferenceid = ? and chainnumber = ?";
				
		@SuppressWarnings("unchecked")
		List<Chain> chainList = jdbcTemplate.query(sql,new Object[] {conferenceid,chainnumber},new BeanPropertyRowMapper(Chain.class));
        
		//List<Chain> chainList = jdbcTemplate.query("select * from chain WHERE zoneid = ? and conferenceid = ? and chainname = ?",new Object[] {zoneid,conferenceid,chainname},new BeanPropertyRowMapper(Chain.class));
        if(chainList != null && chainList.size() > 0){
        	Chain chain = chainList.get(0);
            return chain;
        }else {
        	System.out.println("no chain");
            return null;
        }
		
		//String sql = "SELECT chainid from chain  where conferenceid = ? and chainnumber = ?";
		//return jdbcTemplate.queryForObject(sql,new Object[] {conferenceid,chainnumber}, int.class);
	}
	
	public void add(final Chainstate dbrecord) {
		
		jdbcTemplate.update("UPDATE chainstate SET display = ? WHERE chainid = ? and detecttype = ?",0,dbrecord.getChainid(),dbrecord.getDetecttype());
		
		final String sql = "insert into "
				+ " chainstate(chainid,detecttype,chaintype,chainstate,distant_video_json,network_state_json,local_video_json,path,display)"
				+ " values (?,?,?,?,?,?,?,?,?)";
		final Object[] args = new Object[] { dbrecord.getChainid(), dbrecord.getDetecttype(), dbrecord.getChaintype(),
				dbrecord.getChainstate(),dbrecord.getDistant_video_json(),dbrecord.getNetwork_state_json(),dbrecord.getLocal_video_json(),dbrecord.getPath(),1};
		jdbcTemplate.update(sql, args);
	}
	
	@Override
	public float ListChainstatesCount(int conferenceid,int detecttype) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select count(*) from chainstate a LEFT JOIN CHAIN b ON a.chainid = b.chainid "
				+ "where a.display = ? and b.conferenceid = ? and detecttype = ?", new Object[] {1,conferenceid,detecttype}, float.class);
	}

	@Override
	public List<Map<String, Object>> ListChainstates(int pagenum,int conferenceid,int detecttype) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainstateid,a.chainid,a.detecttype,a.chaintype,a.chainstate,a.distant_video_json,"
				+ "a.network_state_json,a.local_video_json,a.path,a.time,b.chainname,c.conferenceid,c.conferencename,d.zoneid,d.zonename "
				+"FROM chainstate a LEFT JOIN chain b ON a.chainid = b.chainid LEFT JOIN conference c on b.chainconferenceid = c.conferenceid"
				+ " LEFT JOIN warzone d on c.zoneid = d.zoneid where a.chaintype = ? and a.display = ? and b.isOn = ? and b.conferenceid = ? and detecttype = ?"
				+ " order by a.chainstate DESC limit ? ,20";
		return jdbcTemplate.queryForList(sql,new Object[] {"本地",1,1,conferenceid,detecttype,pagenum});
	}
	
	@Override
	public List<Map<String, Object>> ListChainstates2(int pagenum,int conferenceid,int detecttype) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainstateid,a.chainid,a.detecttype,a.chaintype,a.chainstate,a.distant_video_json,"
				+ "a.network_state_json,a.local_video_json,a.path,a.time,b.chainname,c.conferenceid,c.conferencename,d.zoneid,d.zonename "
				+"FROM chainstate a LEFT JOIN chain b ON a.chainid = b.chainid LEFT JOIN conference c on b.chainconferenceid = c.conferenceid"
				+ " LEFT JOIN warzone d on c.zoneid = d.zoneid where a.chaintype = ? and a.display = ? and b.isOn = ? and b.conferenceid = ? and detecttype = ?"
				+ " order by a.chainid limit ? ,20";
		return jdbcTemplate.queryForList(sql,new Object[] {"远端",1,1,conferenceid,detecttype,pagenum});
	}

	@Override
	public float ListChainstatesByZonenameCount(int conferenceid, int detecttype, String zonename) {
		// TODO Auto-generated method stub
		
		return jdbcTemplate.queryForObject("select count(*) from chainstate a LEFT JOIN CHAIN b ON a.chainid = b.chainid "
				+ "LEFT JOIN conference c on b.chainconferenceid = c.conferenceid LEFT JOIN warzone d on c.zoneid = d.zoneid "
				+ " where d.zonename = ? and a.display = ? and b.conferenceid = ? and a.detecttype = ?", new Object[] {zonename,1,conferenceid,detecttype}, float.class);
	}

	@Override
	public List<Map<String, Object>> ListChainstatesByZonename(int pagenum, int conferenceid, int detecttype,
			String zonename) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainstateid,a.chainid,a.detecttype,a.chaintype,a.chainstate,a.distant_video_json,"
				+ "a.network_state_json,a.local_video_json,a.time,b.chainname,c.conferenceid,c.conferencename,d.zoneid,d.zonename "
				+"FROM chainstate a LEFT JOIN chain b ON a.chainid = b.chainid LEFT JOIN conference c on b.chainconferenceid = c.conferenceid"
				+ " LEFT JOIN warzone d on c.zoneid = d.zoneid where d.zonename = ? and a.chaintype = ? and a.display = ? and b.isOn = ? and b.conferenceid = ? and a.detecttype = ?"
				+ " order by a.chainstate DESC limit ? ,20";
		return jdbcTemplate.queryForList(sql,new Object[] {zonename,"本地",1,1,conferenceid,detecttype,pagenum});
	}

	@Override
	public List<Map<String, Object>> ListChainstatesByZonename2(int pagenum, int conferenceid, int detecttype,
			String zonename) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.chainstateid,a.chainid,a.detecttype,a.chaintype,a.chainstate,a.distant_video_json,"
				+ "a.network_state_json,a.local_video_json,a.time,b.chainname,c.conferenceid,c.conferencename,d.zoneid,d.zonename "
				+"FROM chainstate a LEFT JOIN chain b ON a.chainid = b.chainid LEFT JOIN conference c on b.chainconferenceid = c.conferenceid"
				+ " LEFT JOIN warzone d on c.zoneid = d.zoneid where d.zonename = ? and a.chaintype = ? and a.display = ? and b.isOn = ? and b.conferenceid = ? and a.detecttype = ?"
				+ " order by a.chainstate DESC limit ? ,20";
		return jdbcTemplate.queryForList(sql,new Object[] {zonename,"远端",1,1,conferenceid,detecttype,pagenum});
	}



	public void add(int chainid, String chaintype, ChainDiagnoseMessage chaindiagnosemessage) {
		// TODO Auto-generated method stub
		
		final String sql = "insert into "
				+ " fault(chainid,chaintype,blur,overdark,overbright,colorcast,lowcontrast,mosaic,blackscreen,frozen)"
				+ " values (?,?,?,?,?,?,?,?,?,?)";
		final Object[] args = new Object[] { chainid, chaintype,chaindiagnosemessage.getLocalvideojson().getBlur(),
				chaindiagnosemessage.getLocalvideojson().getOverdark(),chaindiagnosemessage.getLocalvideojson().getOverbright(),
				chaindiagnosemessage.getLocalvideojson().getColorcast(),chaindiagnosemessage.getLocalvideojson().getLowcontrast(),
				chaindiagnosemessage.getLocalvideojson().getMosaic(),chaindiagnosemessage.getLocalvideojson().getBlackscreen(),
				chaindiagnosemessage.getLocalvideojson().getFrozen()};
		jdbcTemplate.update(sql, args);
	}
	

}
