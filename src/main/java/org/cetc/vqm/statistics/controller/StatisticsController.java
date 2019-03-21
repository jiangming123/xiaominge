package org.cetc.vqm.statistics.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {
	
	@Autowired
	StatisticsService staticticsService;

	@PostMapping(value = "/statistics/timequery")
	public Map<String, Object> timequery(@RequestParam("starttime") String starttime,
			@RequestParam("endtime") String endtime)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		starttime += " 00:00:00";
		endtime += " 23:59:59";
		
		Timestamp start = new Timestamp(System.currentTimeMillis());
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		
		start = Timestamp.valueOf(starttime);
		
		end  = Timestamp.valueOf(endtime);
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		try {
			
			datas = staticticsService.ListStatistics(start,end);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		rs.put("datas", datas);
		rs.put("flag", "success");
		rs.put("msg", "展示所有查询结果成功");
	
		return rs;
	}
	
	@PostMapping(value = "/statistics/faultquery")
	public Map<String, Object> faultquery(@RequestParam("starttime") String starttime,
			@RequestParam("endtime") String endtime)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		starttime += " 00:00:00";
		endtime += " 23:59:59";
		
		Timestamp start = new Timestamp(System.currentTimeMillis());
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		
		start = Timestamp.valueOf(starttime);
		
		end  = Timestamp.valueOf(endtime);
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> newdatas = new ArrayList<Map<String, Object>>();
		try {
			
			datas = staticticsService.ListFaultStatistics(start,end);
			
			for(Map<String, Object> map : datas){
				
				int chainid = Integer.parseInt(map.get("chainid").toString());
				
				newdatas = staticticsService.NewListFaultStatistics(start,end,chainid);
				
				map.put("overall", newdatas);
				
				/*for(Map<String, Object> newmap : newdatas){
				if(newmap.get("chainid").equals("合计")) {
					map.put("hj", newmap);
					}
				}*/
			
			}
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rs.put("datas", datas);
		rs.put("flag", "success");
		rs.put("msg", "展示所有查询结果成功");
	
		return rs;
	}
}
