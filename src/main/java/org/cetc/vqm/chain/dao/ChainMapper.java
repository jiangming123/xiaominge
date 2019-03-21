package org.cetc.vqm.chain.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.chain.pojo.Chain;

public interface ChainMapper {

	public void addChains(Chain chain, int chainnum);

	public float getChainCount();

	public List<Map<String, Object>> ListAllChains(int pagenum);

	public Chain getChain(int zoneid, int conferenceid, String chainname);

	public void addChain(Chain chain);

	public float getChainCount(int zoneid, int conferenceid);

	public List<Map<String, Object>> ListAllChains(int pagenum, int zoneid, int conferenceid);

	public void editChain(int chainid, int chainconferenceid, String chainname, String type, Date time);

	public Chain getChain(Integer chainid);

	public void deleteChain(Integer chainid);

	public int getMaxChainNumber(int zoneid, int conferenceid);

	public List<Map<String, Object>> ListActiveConferenceChains(int conferenceid);

	public List<Map<String, Object>> ListInActiveConferenceChains(int conferenceid);

	public void addListActiveConferenceChains(int conferenceid, String chainids);

	public void deleteListActiveConferenceChains(int conferenceid, int chainid);

}
