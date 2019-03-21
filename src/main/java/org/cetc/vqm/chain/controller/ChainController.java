package org.cetc.vqm.chain.controller;

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
import org.cetc.vqm.log.pojo.LogInfo;
import org.cetc.vqm.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChainController {
	
	@Autowired // (required = false) Spring容器不再抛出异而是认为这两个属性为null。
	private ChainService chainService;
	
	@Autowired
	private LogService logService;
	
	//通道列表
		@PostMapping(value = "/chain/getAllChains")
		public Map<String, Object> getAllChains(@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			float count = chainService.ListAllChainCount();
			
			int countpage = (int) Math.ceil(count / 10);
			
			List<Map<String, Object>> chains;
			
			if (pagenum < 1 || countpage < 1) {
				
				int pagenum1 = 0;
				chains = chainService.ListAllChains(pagenum1);
				rs.put("Data", chains);
			} else if (pagenum <= countpage) {
				chains = chainService.ListAllChains((pagenum - 1) * 10);
				rs.put("Data", chains);
			} else {
				int pagenum1 = (countpage - 1) * 10;
				chains = chainService.ListAllChains(pagenum1);
				rs.put("Data", chains);
			}
			List<String> time=new ArrayList<String>();
			for(int i=0;i<chains.size();i++) {
				String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chains.get(i).get("time"));
				time.add(datetime);
			}
			
			rs.put("time", time);
			rs.put("count", count);
			rs.put("countpage", countpage);
			rs.put("flag", "success");
			rs.put("msg", "展示所有通道成功");
		
			return rs;
		}
		
		//根据战区/会场选择通道列表
		@PostMapping(value = "/chain/getAllChainsByZoneIDAndConferenceid")
		public Map<String, Object> getAllConferences(@RequestParam("zoneid") String zoneid,@RequestParam("conferenceid")
		String conferenceid, @RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			int id = Integer.valueOf(zoneid);
			int id1 = Integer.valueOf(conferenceid);
			
			float count = chainService.ListAllChainCount(id,id1);
			
			int countpage = (int) Math.ceil(count / 10);
			
			List<Map<String, Object>> chains;
			
			if (pagenum < 1 || countpage < 1) {
				
				int pagenum1 = 0;
				chains = chainService.ListAllChains(pagenum1,id,id1);
				rs.put("Data", chains);
			} else if (pagenum <= countpage) {
				chains = chainService.ListAllChains((pagenum - 1) * 10,id,id1);
				rs.put("Data", chains);
			} else {
				int pagenum1 = (countpage - 1) * 10;
				chains = chainService.ListAllChains(pagenum1,id,id1);
				rs.put("Data", chains);
			}
			
			List<String> time=new ArrayList<String>();
			for(int i=0;i<chains.size();i++) {
				String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chains.get(i).get("time"));
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
	     * 添加通道
	     */
		@PostMapping("/chain/addChain")
		public Map<String, Object> addChain(@RequestParam("zoneid") String zoneid,@RequestParam("zonename")
		String zonename ,@RequestParam("conferenceid")	String conferenceid ,@RequestParam("conferencename")
		String conferencename ,@RequestParam("chainname")	String chainname, 
		@RequestParam("type")	String type,HttpSession session) {

			Map<String, Object> rs = new HashMap<String, Object>();
			
			int id = Integer.valueOf(zoneid);
			int id1 = Integer.valueOf(conferenceid);
			
			Chain chain1 = chainService.getChain(id,id1,chainname);
			if (chain1 != null) {
				rs.put("flag", "error");
				rs.put("msg", "通道已存在，无法重复添加");
				return rs;
			}
			
			float count = chainService.ListAllChainCount(id,id1);
			
			String username = (String) session.getAttribute("loginUser");
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
			String nowdate = dateFormat.format( now );
			
			int maxchainnumber = chainService.getMaxChainNumber(id,id1);
			
			Chain chain = new Chain();
			chain.setChainnumber((int)count+1);
			chain.setConferenceid(id1);
			chain.setChainname(chainname);
			chain.setType(type);
			chain.setTime(Timestamp.valueOf(nowdate));
			chainService.addChain(chain);
			
			LogInfo loginfo = new LogInfo();
			loginfo.setUsername(username);
			loginfo.setTime(Timestamp.valueOf(nowdate));
			String operation = "给战区 " + zonename + " 会场 " + conferencename + " 添加通道 " + chainname;
			loginfo.setOperation(operation);
			logService.insertLog(loginfo);

			rs.put("flag", "success");
			rs.put("msg", "添加成功");
			
			return rs;
		}
		
		/**
	     * 更新通道信息
	     */
	    @PostMapping("/chain/editChain")
	    public Map<String, Object> editChain(@RequestParam("chainid") String chainid,
	    		@RequestParam("zonename") String zonename,
	    		@RequestParam("conferencename") String conferencename,
	    		@RequestParam("chainname")String chainname,
	    		@RequestParam("chainconferenceid")String chainconferenceid,
	    		@RequestParam("type")String type,HttpSession session){

	    	Map<String, Object> rs = new HashMap<String, Object>();	
	    	
	    	int id = Integer.valueOf(chainid);	
	    	int id2 = Integer.valueOf(chainconferenceid);
	    	
	    	String username = (String) session.getAttribute("loginUser");
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
			String nowdate = dateFormat.format( now );
	    	
	    	chainService.editChain(id,id2,chainname,type,Timestamp.valueOf(nowdate));
			
			
			
			LogInfo loginfo = new LogInfo();
			loginfo.setUsername(username);
			loginfo.setTime(Timestamp.valueOf(nowdate));
			String operation = "修改通道名称为: " + chainname;
			//String operation = " 修改战区 " + zonename + " 会场 " + conferencename + " 通道名称为: " + chainname;
			loginfo.setOperation(operation);
			logService.insertLog(loginfo);
			
			rs.put("flag", "success");
			rs.put("msg", "修改成功");
	    	
	    	return rs;
	    }
	    
	    /**
	     * 删除通道信息
	     */
	    @SuppressWarnings("null")
		@PostMapping("/chain/deleteChain")
	    Map<String, Object> deleteConference(
	    		@RequestParam("zoneid") String zoneid,
	    		@RequestParam("chainid")  String chainid, HttpSession session){
	    	
	    	Map<String, Object> rs = new HashMap<String, Object>();
	    	
	    	int id = Integer.valueOf(chainid);
	    	
	    	Chain chain = chainService.getChain(id);
	    	
	    	if (chain == null) {
				rs.put("flag", "error");
				rs.put("msg", "会场不存在，无法删除");
				return rs;
			}
	    	
	    	chainService.deleteChain(id);
	        
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
			String nowdate = dateFormat.format( now );
			
			String username = (String) session.getAttribute("loginUser");
			
			LogInfo loginfo = new LogInfo();
			loginfo.setUsername(username);
			loginfo.setTime(Timestamp.valueOf(nowdate));
			String operation = " 删除战区id: " + zoneid
						+ " 会场id: " + chain.getConferenceid() + " 通道 " + chain.getChainname();
			loginfo.setOperation(operation);
			logService.insertLog(loginfo);
			
			rs.put("flag", "success");
			rs.put("msg", "删除成功");
			
			return rs;
	    }
	    
	    //通道列表
		@PostMapping(value = "/chain/getActiveConferenceChains")
		public Map<String, Object> getActiveconferenceChains(@RequestParam("conferenceid")	String conferenceid)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			List<Map<String, Object>> chains;
			
			int id = Integer.valueOf(conferenceid);
			
			chains = chainService.ListActiveConferenceChains(id);
			
			rs.put("Data", chains);
			
			List<String> time=new ArrayList<String>();
			for(int i=0;i<chains.size();i++) {
				String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chains.get(i).get("time"));
				time.add(datetime);
			}
			
			rs.put("flag", "success");
			rs.put("msg", "展示所有当前会场在线通道成功");
		
			return rs;
		}
		
		//通道列表
		@PostMapping(value = "/chain/getInActiveConferenceChains")
		public Map<String, Object> getInActiveconferenceChains(@RequestParam("conferenceid")	String conferenceid)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			List<Map<String, Object>> chains;
			
			int id = Integer.valueOf(conferenceid);
			
			chains = chainService.ListInActiveConferenceChains(id);
			
			rs.put("Data", chains);
			
			List<String> time=new ArrayList<String>();
			for(int i=0;i<chains.size();i++) {
				String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chains.get(i).get("time"));
				time.add(datetime);
			}
			
			rs.put("flag", "success");
			rs.put("msg", "展示所有当前会场不在线通道成功");
		
			return rs;
		}
		
		//通道列表
		@PostMapping(value = "/chain/addActiveConferenceChains")
		public Map<String, Object> addActiveconferenceChains(@RequestParam("conferenceid")	String conferenceid
				,@RequestParam("chainids")	String chainids)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			int id = Integer.valueOf(conferenceid);
			
			chainService.addListActiveConferenceChains(id,chainids);
			
			rs.put("flag", "success");
			rs.put("msg", "添加当前会场在线通道成功");
		
			return rs;
		}
		
		//通道列表
		@PostMapping(value = "/chain/deleteActiveConferenceChains")
		public Map<String, Object> deleteActiveconferenceChains(@RequestParam("conferenceid")	String conferenceid
				,@RequestParam("chainid")	String chainid)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			int id = Integer.valueOf(conferenceid);
			
			int id1 = Integer.valueOf(chainid);
			
			chainService.deleteListActiveConferenceChains(id,id1);
			
			rs.put("flag", "success");
			rs.put("msg", "删除当前会场在线通道成功");
		
			return rs;
		}

}
