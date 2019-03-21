package org.cetc.vqm.conference.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.cetc.vqm.chain.pojo.Chain;
import org.cetc.vqm.chain.service.ChainService;
import org.cetc.vqm.conference.pojo.Conference;
import org.cetc.vqm.conference.service.ConferenceService;
import org.cetc.vqm.log.pojo.LogInfo;
import org.cetc.vqm.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConferenceController {

	@Autowired // (required = false) Spring容器不再抛出异而是认为这两个属性为null。
	private ConferenceService conferenceService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private ChainService chainService;
	
	//会场列表
	@PostMapping(value = "/conference/getAllConferences")
	public Map<String, Object> getAllConferences(@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		float count = conferenceService.ListAllConferenceCount();
		
		int countpage = (int) Math.ceil(count / 10);
		
		List<Map<String, Object>> conferences;
		
		if (pagenum < 1 || countpage < 1) {
			
			int pagenum1 = 0;
			conferences = conferenceService.ListAllConferences(pagenum1);
			rs.put("Data", conferences);
		} else if (pagenum <= countpage) {
			conferences = conferenceService.ListAllConferences((pagenum - 1) * 10);
			rs.put("Data", conferences);
		} else {
			int pagenum1 = (countpage - 1) * 10;
			conferences = conferenceService.ListAllConferences(pagenum1);
			rs.put("Data", conferences);
		}
		List<String> time=new ArrayList<String>();
		for(int i=0;i<conferences.size();i++) {
			String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(conferences.get(i).get("time"));
			time.add(datetime);
		}
		
		rs.put("time", time);
		rs.put("count", count);
		rs.put("countpage", countpage);
		rs.put("flag", "success");
		rs.put("msg", "展示所有会场成功");
	
		return rs;
	}
	
	//根据战区选择会场列表
	@PostMapping(value = "/conference/getAllConferencesByZoneID")
	public Map<String, Object> getAllConferences(@RequestParam("zoneid") String zoneid,
			@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		int id = Integer.valueOf(zoneid);
		
		float count = conferenceService.ListAllConferenceCount(id);
		
		if (pagenum == -1)
		{
			List<Map<String, Object>> conferences;
			conferences = conferenceService.ListAllConference(id);
			rs.put("Data", conferences);
			rs.put("count", count);
			rs.put("flag", "success");
			rs.put("msg", "根据战区选择会场列表展示成功");
		
			return rs;
		}
		
		int countpage = (int) Math.ceil(count / 10);
		
		List<Map<String, Object>> conferences;
		
		if (pagenum < 1 || countpage < 1) {
			
			int pagenum1 = 0;
			conferences = conferenceService.ListAllConference(pagenum1,id);
			rs.put("Data", conferences);
		} else if (pagenum <= countpage) {
			conferences = conferenceService.ListAllConference((pagenum - 1) * 10,id);
			rs.put("Data", conferences);
		} else {
			int pagenum1 = (countpage - 1) * 10;
			conferences = conferenceService.ListAllConference(pagenum1,id);
			rs.put("Data", conferences);
		}
		
		List<String> time=new ArrayList<String>();
		for(int i=0;i<conferences.size();i++) {
			String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(conferences.get(i).get("time"));
			time.add(datetime);
		}
		
		rs.put("time", time);
		rs.put("count", count);
		rs.put("countpage", countpage);
		rs.put("flag", "success");
		rs.put("msg", "根据战区选择会场列表展示成功");
	
		return rs;
	}

	/**
     * 添加会场
     */
	@PostMapping("/conference/addConference")
	public Map<String, Object> addConference(@RequestParam("zoneid") String zoneid,@RequestParam("zonename")
	String zonename ,@RequestParam("conferencename") String conferencename,@RequestParam("serverip") String serverip, HttpSession session) {

		Map<String, Object> rs = new HashMap<String, Object>();
		
		int id = Integer.valueOf(zoneid);
		
		Conference conference1 = conferenceService.getConference(id,conferencename);
		if (conference1 != null) {
			rs.put("flag", "error");
			rs.put("msg", "会场已存在，无法重复添加");
			return rs;
		}
		
		Conference conference2 = conferenceService.getConference(serverip);
		if (conference2 != null) {
			rs.put("flag", "error");
			rs.put("msg", "会场服务器IP已经存在，无法重复添加");
			return rs;
		}
		
		String username = (String) session.getAttribute("loginUser");
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowdate = dateFormat.format( now );
		
		Conference conference = new Conference();
		conference.setZoneid(id);
		conference.setConferencename(conferencename);
		conference.setServerip(serverip);
		conference.setTime(Timestamp.valueOf(nowdate));
		int conferenceid = conferenceService.addConference(conference);
		
		Chain chain = new Chain();
		chain.setConferenceid(conferenceid);
		chain.setChainname("通道");
		chain.setTime(Timestamp.valueOf(nowdate));
		int chainnum = 10;
		
		chainService.addChains(chain,chainnum);
		
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(username);
		loginfo.setTime(Timestamp.valueOf(nowdate));
		String operation = "给战区 " + zonename + " 添加会场 " + conferencename;
		loginfo.setOperation(operation);
		logService.insertLog(loginfo);

		rs.put("flag", "success");
		rs.put("msg", "添加成功");
		
		return rs;
	}
	
	/**
     * 更新会场信息
     */
    @PostMapping("/conference/editConference")
    public Map<String, Object> editConference(@RequestParam("conferenceid") String conferenceid, 
    		@RequestParam("zoneid") String zoneid,@RequestParam("name")String name,
    		@RequestParam("serverip") String serverip, HttpSession session){

    	Map<String, Object> rs = new HashMap<String, Object>();		
    	
    	int id = Integer.valueOf(conferenceid);
    	conferenceService.editConference(id,name,serverip);
		
		String username = (String) session.getAttribute("loginUser");
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowdate = dateFormat.format( now );
		
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(username);
		loginfo.setTime(Timestamp.valueOf(nowdate));
		String operation = " 修改战区 " + zoneid + " 会场 " + name + " 信息";
		loginfo.setOperation(operation);
		logService.insertLog(loginfo);
		
		rs.put("flag", "success");
		rs.put("msg", "修改成功");
    	
    	return rs;
    }
    
    /**
     * 删除会场信息
     */
    @SuppressWarnings("null")
	@PostMapping("/conference/deleteConference")
    Map<String, Object> deleteConference(@RequestParam("conferenceid")  Integer conferenceid, HttpSession session){
    	
    	Map<String, Object> rs = new HashMap<String, Object>();
    	
    	Conference conference = conferenceService.getConference(conferenceid);
    	
    	if (conference == null) {
			rs.put("flag", "error");
			rs.put("msg", "会场不存在，无法删除");
			return rs;
		}
    	
    	conferenceService.deleteConference(conferenceid);
        
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		String nowdate = dateFormat.format( now );
		
		String username = (String) session.getAttribute("loginUser");
		
		LogInfo loginfo = new LogInfo();
		loginfo.setUsername(username);
		loginfo.setTime(Timestamp.valueOf(nowdate));
		String operation = " 删除战区 " + String.valueOf(conference.getZoneid())
					+ " 会场 " + conference.getConferencename() + " 信息";
		loginfo.setOperation(operation);
		logService.insertLog(loginfo);
		
		rs.put("flag", "success");
		rs.put("msg", "删除成功");
		
		return rs;
    }
	
	
}
