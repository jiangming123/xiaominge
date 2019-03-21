package org.cetc.vqm.log.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.log.service.LogService;
import org.cetc.vqm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
	
	@Autowired
	private LogService logService;
	@Autowired
	private UserService userService;
	
	//日志列表
	//@GetMapping(value = "/log/getAllLogs")
	@PostMapping(value = "/log/getAllLogs")
	public Map<String, Object> getAllLogs(@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		float count = logService.ListAllLogCount();
		
		int countpage = (int) Math.ceil(count / 10);
		
		List<Map<String, Object>> logs;
		
		if (pagenum < 1 || countpage < 1) {
			
			int pagenum1 = 0;
			logs = logService.ListAllLog(pagenum1);
			rs.put("Data", logs);
		} else if (pagenum <= countpage) {
			logs = logService.ListAllLog((pagenum - 1) * 10);
			rs.put("Data", logs);
		} else {
			int pagenum1 = (countpage - 1) * 10;
			logs = logService.ListAllLog(pagenum1);
			rs.put("Data", logs);
		}
		
		List<String> time=new ArrayList<String>();
		for(int i=0;i<logs.size();i++) {
			String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logs.get(i).get("time"));
			time.add(datetime);
		}
		rs.put("time", time);
		rs.put("count", count);
		rs.put("countpage", countpage);
		rs.put("flag", "success");
		rs.put("msg", "展示所有日志成功");
	
		return rs;
	}
	
	@PostMapping(value = "/log/queryLogs")
	public Map<String, Object> queryLogs(@RequestParam("starttime") String starttime,
			@RequestParam("endtime") String endtime,@RequestParam("username") String username,
			@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		Timestamp start = new Timestamp(System.currentTimeMillis());
		
		Timestamp end = new Timestamp(System.currentTimeMillis());
		
		if(starttime=="")
			start = null;
		else start = Timestamp.valueOf(starttime);
		
		if(endtime=="")
			end = null;
		else end  = Timestamp.valueOf(endtime);
		
		//System.out.println(endtime);
		
		
		try {
			
			float count = logService.ListAllLogCount(start,end,username);
			int countpage=(int)Math.ceil(count/10);
			rs.put("count",count);
			rs.put("countnum", countpage);
			
			List<Map<String, Object>> logs = new ArrayList<Map<String, Object>>();
			
			if(count <=0){
				rs.put("flag", "success");
				rs.put("msg", "展示所有日志成功");
				rs.put("Data", logs);
				return rs;
			}
			else if(pagenum < 0) {
				int num=1;
				logs = logService.ListAllLog(start,end,username,num);
				rs.put("Data", logs);
			}else if(pagenum < countpage) {
				int num = pagenum;
				logs = logService.ListAllLog(start,end,username,num);
				rs.put("Data", logs);
			}else {
				int num = (int) countpage;
				logs = logService.ListAllLog(start,end,username,num);
				rs.put("Data", logs);
			}
			
			List<String> time=new ArrayList<String>();
			for(int i=0;i<logs.size();i++) {
				String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(logs.get(i).get("time"));
				time.add(datetime);
			}
			rs.put("time", time);
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rs.put("flag", "success");
		rs.put("msg", "展示所有日志成功");
	
		return rs;
	}
	
	
	@PostMapping(value = "/log/getAllUsers")
	public Map<String, Object> getAllUsers()
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<Map<String, Object>> users = userService.ListAllUser();
		
		
		rs.put("Data", users);
		rs.put("flag", "success");
		rs.put("msg", "展示所有用户成功");
	
		return rs;
	}

}
