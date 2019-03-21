package org.cetc.vqm.conference.service;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.conference.dao.ConferenceMapper;
import org.cetc.vqm.conference.pojo.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConferenceService {
	
	@Autowired
    ConferenceMapper conferenceMapper;

	public Conference getConference(int zoneid, String name) {
		// TODO Auto-generated method stub
		return conferenceMapper.getConference(zoneid,name);
	}
	
	public Conference getConference(String serverip) {
		// TODO Auto-generated method stub
		return conferenceMapper.getConference(serverip);
	}

	public int addConference(Conference conference) {
		// TODO Auto-generated method stub
		return conferenceMapper.addConference(conference);
	}

	public void editConference(int conferenceid, String name, String serverip) {
		// TODO Auto-generated method stub
		conferenceMapper.editConference(conferenceid,name,serverip);
	}

	public void deleteConference(Integer conferenceid) {
		// TODO Auto-generated method stub
		conferenceMapper.deleteConference(conferenceid);
	}

	public Conference getConference(Integer id) {
		// TODO Auto-generated method stub
		return conferenceMapper.getConference(id);
	}

	public float ListAllConferenceCount() {
		// TODO Auto-generated method stub
		return conferenceMapper.getConferenceCount();
	}

	public List<Map<String, Object>> ListAllConferences(int pagenum) {
		// TODO Auto-generated method stub
		return conferenceMapper.ListAllConferences(pagenum);
	}

	public float ListAllConferenceCount(int zoneid) {
		// TODO Auto-generated method stub
		return conferenceMapper.getConferenceCount(zoneid);
	}

	public List<Map<String, Object>> ListAllConference(int pagenum, int zoneid) {
		// TODO Auto-generated method stub
		return conferenceMapper.ListAllConference(pagenum,zoneid);
	}

	public List<Map<String, Object>> getConferenceInfoByServerip(String ip) {
		// TODO Auto-generated method stub
		return conferenceMapper.getConferenceInfoByServerip(ip);
	}

	public List<Map<String, Object>> ListAllConference(int zoneid) {
		// TODO Auto-generated method stub
		return conferenceMapper.ListAllConference(zoneid);
	}



}
