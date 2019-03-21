package org.cetc.vqm.chain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.chain.dao.ChainMapper;
import org.cetc.vqm.chain.pojo.Chain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChainService {

	@Autowired
    ChainMapper chainMapper;
	
	public void addChains(Chain chain, int chainnum) {
		// TODO Auto-generated method stub
		chainMapper.addChains(chain,chainnum);
	}

	public float ListAllChainCount() {
		// TODO Auto-generated method stub
		return chainMapper.getChainCount();
	}

	public List<Map<String, Object>> ListAllChains(int pagenum) {
		// TODO Auto-generated method stub
		return chainMapper.ListAllChains(pagenum);
	}

	public Chain getChain(int zoneid, int conferenceid, String chainname) {
		// TODO Auto-generated method stub
		return chainMapper.getChain(zoneid,conferenceid,chainname);
	}

	public void addChain(Chain chain) {
		// TODO Auto-generated method stub
		chainMapper.addChain(chain);
	}

	public float ListAllChainCount(int zoneid, int conferenceid) {
		// TODO Auto-generated method stub
		return chainMapper.getChainCount(zoneid, conferenceid);
	}

	public List<Map<String, Object>> ListAllChains(int pagenum, int zoneid, int conferenceid) {
		// TODO Auto-generated method stub
		return chainMapper.ListAllChains(pagenum,zoneid,conferenceid);
	}

	public void editChain(int chainid, int chainconferenceid, String chainname,String type,Date time) {
		// TODO Auto-generated method stub
		chainMapper.editChain(chainid, chainconferenceid, chainname,type,time);
	}

	public Chain getChain(Integer chainid) {
		// TODO Auto-generated method stub
		return chainMapper.getChain(chainid);
	}

	public void deleteChain(Integer chainid) {
		// TODO Auto-generated method stub
		chainMapper.deleteChain(chainid);
	}
	
	public int getMaxChainNumber(int zoneid, int conferenceid) {
		// TODO Auto-generated method stub
		return chainMapper.getMaxChainNumber(zoneid, conferenceid);
	}

	public List<Map<String, Object>> ListActiveConferenceChains(int conferenceid) {
		// TODO Auto-generated method stub
		return chainMapper.ListActiveConferenceChains(conferenceid);
	}

	public List<Map<String, Object>> ListInActiveConferenceChains(int conferenceid) {
		// TODO Auto-generated method stub
		return chainMapper.ListInActiveConferenceChains(conferenceid);
	}

	public void addListActiveConferenceChains(int conferenceid, String chainids) {
		// TODO Auto-generated method stub
		chainMapper.addListActiveConferenceChains(conferenceid, chainids);
	}

	public void deleteListActiveConferenceChains(int conferenceid, int chainid) {
		// TODO Auto-generated method stub
		chainMapper.deleteListActiveConferenceChains(conferenceid, chainid);
	}

}
