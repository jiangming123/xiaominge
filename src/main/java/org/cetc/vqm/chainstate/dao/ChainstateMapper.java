package org.cetc.vqm.chainstate.dao;

import java.util.List;
import java.util.Map;

public interface ChainstateMapper {

	float ListChainstatesCount(int conferenceid,int detecttype);

	List<Map<String, Object>> ListChainstates(int pagenum,int conferenceid,int detecttype);
	
	List<Map<String, Object>> ListChainstates2(int pagenum,int conferenceid,int detecttype);

	float ListChainstatesByZonenameCount(int conferenceid, int detecttype, String zonename);

	List<Map<String, Object>> ListChainstatesByZonename(int pagenum1, int conferenceid, int detecttype,
			String zonename);

	List<Map<String, Object>> ListChainstatesByZonename2(int pagenum, int conferenceid, int detecttype,
			String zonename);

	
}
