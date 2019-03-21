package org.cetc.vqm.chainstate.service;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.chainstate.dao.ChainstateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChainstateService {
	
	@Autowired
	ChainstateMapper chainstateMapper;

	public float ListChainstatesCount(int conferenceid,int detecttype) {
		// TODO Auto-generated method stub
		return chainstateMapper.ListChainstatesCount(conferenceid,detecttype);
	}

	public List<Map<String, Object>> ListChainstates(int pagenum,int conferenceid,int detecttype) {
		// TODO Auto-generated method stub
		return chainstateMapper.ListChainstates(pagenum,conferenceid,detecttype);
	}
	
	public List<Map<String, Object>> ListChainstates2(int pagenum,int conferenceid,int detecttype) {
		// TODO Auto-generated method stub
		return chainstateMapper.ListChainstates2(pagenum,conferenceid,detecttype);
	}

	public float ListChainstatesByZonenameCount(int conferenceid, int detecttype, String zonename) {
		// TODO Auto-generated method stub
		return chainstateMapper.ListChainstatesByZonenameCount(conferenceid,detecttype,zonename);
	}

	public List<Map<String, Object>> ListChainstatesByZonename(int pagenum1, int conferenceid, int detecttype, String zonename) {
		// TODO Auto-generated method stub
		return chainstateMapper.ListChainstatesByZonename(pagenum1,conferenceid,detecttype,zonename);
	}
	public List<Map<String, Object>> ListChainstatesByZonename2(int pagenum,int conferenceid,int detecttype, String zonename) {
		// TODO Auto-generated method stub
		return chainstateMapper.ListChainstatesByZonename2(pagenum,conferenceid,detecttype,zonename);
	}

	
	
}
