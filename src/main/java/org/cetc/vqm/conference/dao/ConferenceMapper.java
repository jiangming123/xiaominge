package org.cetc.vqm.conference.dao;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.conference.pojo.Conference;

public interface ConferenceMapper {

	Conference getConference(int zoneid, String name);
	
	Conference getConference(String serverip);

	int addConference(Conference conference);

	void editConference(int conferenceid, String name, String serverip);

	void deleteConference(Integer id);

	Conference getConference(Integer id);

	float getConferenceCount();

	List<Map<String, Object>> ListAllConferences(int pagenum);

	float getConferenceCount(int zoneid);

	List<Map<String, Object>> ListAllConference(int pagenum, int zoneid);

	List<Map<String, Object>> getConferenceInfoByServerip(String serverip);

	List<Map<String, Object>> ListAllConference(int zoneid);

}
