package org.cetc.vqm.chainstate.controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.cetc.vqm.agent.control.VideoProcessControl;
import org.cetc.vqm.agent.control.VideoSourceNetWorker;
import org.cetc.vqm.chain.pojo.Chain;
import org.cetc.vqm.conference.pojo.Conference;
import org.cetc.vqm.conference.service.ConferenceService;
import org.cetc.vqm.log.pojo.LogInfo;
import org.cetc.vqm.chainstate.pojo.Distantvideojson;
import org.cetc.vqm.chainstate.pojo.Localvideojson;
import org.cetc.vqm.chainstate.pojo.Networkstatejson;
import org.cetc.vqm.chainstate.service.ChainstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
public class ChainstateController {
	
	@Autowired // (required = false) Spring容器不再抛出异而是认为这两个属性为null。
	private ChainstateService chainstateService;
	
	@Autowired // (required = false) Spring容器不再抛出异而是认为这两个属性为null。
	private ConferenceService conferenceService;
	
	
	//通道状态列表
	@PostMapping(value = "/chainstate/getChainstates")
	public Map<String, Object> getAllChains(@RequestParam("conferenceid") String conferenceid,
			@RequestParam("detect_type") String detect_type
			,@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		int id = Integer.valueOf(conferenceid);
		
		int detecttype = Integer.valueOf(detect_type);
		
		float count = chainstateService.ListChainstatesCount(id,detecttype);
		
		int countpage = (int) Math.ceil(count / 20);
		
		List<Map<String, Object>> chains; // 本地
		List<Map<String, Object>> chains2; // 远端
		
		if (pagenum < 1 || countpage < 1) {
			
			int pagenum1 = 0;
			chains = chainstateService.ListChainstates(pagenum1,id,detecttype);
			chains2 = chainstateService.ListChainstates2(pagenum1,id,detecttype);
		} else if (pagenum <= countpage) {
			chains = chainstateService.ListChainstates((pagenum - 1) * 10,id,detecttype);
			chains2 = chainstateService.ListChainstates2((pagenum - 1) * 10,id,detecttype);
		} else {
			int pagenum1 = (countpage - 1) * 10;
			chains = chainstateService.ListChainstates(pagenum1,id,detecttype);
			chains2 = chainstateService.ListChainstates2(pagenum1,id,detecttype);
		}

		chains.addAll(chains2);
		if (chains.size() < 1)
		{
			rs.put("flag", "failure");
			rs.put("msg", "没有通道状态展示");
			return rs;
		
		}
		
		for (Map<String, Object> m : chains)
	    {
			m.put("distantvideojson", "");
			m.put("networkstatejson", "");
			m.put("localvideojson", "");
			m.put("distantvideostate", "");
			m.put("networkstate", "正常");
			m.put("localvideostate", "");
			m.put("suggestion", "");
			m.put("score", "");
			
			String chaintype = "";
			
			for (String k : m.keySet())
			{
	    	  
	    	  if(k.equals("detecttype"))
	    	  {
	    		  /*if(m.get(k).equals(0))
	    		  {
	    			  m.put("detecttype", "自动试机试线");
	    			  detecttype = 0;
	    		  }
	    		  else if(m.get(k).equals(1))
	    		  {
	    			  m.put("detecttype", "实时检测");
	    			  detecttype = 1;
	    		  }*/
	    		  if(detecttype == 1)
	    			  m.put("detecttype", "自动试机试线");
	    		  else if(detecttype == 2)
	    			  m.put("detecttype", "实时检测");
	    			  
	    	  }
	    	  else if(k.equals("chaintype"))
	    	  {
	    		  chaintype = (String) m.get(k);
	    		  /*if(m.get(k).equals(0))
	    		  {
	    			  m.put("chaintype", "本地");
	    		  }
	    		  else if(m.get(k).equals(1))
	    		  {
	    			  m.put("chaintype", "远端");
	    		  }*/
	    	  }
	    	  /*else if(k.equals("chainstate"))
	    	  {
	    		  float score = (float) m.get(k);
	    		  if(4 <= score && score <= 5)
	    		  {
	    			  m.put("chainstate", "正常");
	    		  }
	    		  else if(3 < score && score <= 4)
	    		  {
	    			  m.put("chainstate", "异常");
	    		  }
	    		  else if(2 < score && score <= 3)
	    		  {
	    			  m.put("chainstate", "故障");
	    		  }
	    		  else if(score < 2)
	    		  {
	    			  m.put("chainstate", "严重故障");
	    		  }
	    	  }*/
	    	  else if(k.equals("distant_video_json")) 
	    	  {
	    		  /*final Distantvideojson r = JSONObject.parseObject(String.valueOf(m.get(k)), Distantvideojson.class);
	    		  m.put("distantvideojson", r);
	    		  int score = r.getMosaic() + r.getBlackscreen() + r.getFrozen();
	    		  if(121 < score && score <= 150)
	    		  {
	    			  m.put("distantvideostate", "正常");
	    		  }
	    		  else if(91 < score && score <= 120)
	    		  {
	    			  m.put("distantvideostate", "异常");
	    		  }
	    		  else if(61 < score && score <= 90)
	    		  {
	    			  m.put("distantvideostate", "故障");
	    		  }
	    		  else if(30 < score && score <= 60)
	    		  {
	    			  m.put("distantvideostate", "严重故障");
	    		  }*/
	    	  }
	    	  else if(k.equals("network_state_json"))
	    	  {
	    		  final Networkstatejson r = JSONObject.parseObject(String.valueOf(m.get(k)), Networkstatejson.class);
	    		  m.put("networkstatejson", r);
	    	  }
	    	  else if(k.equals("local_video_json"))
	    	  {
	    		  final Localvideojson r = JSONObject.parseObject(String.valueOf(m.get(k)), Localvideojson.class);
	    		  m.put("localvideojson", r);
	    		  
	    		  float score = 0;
	    		  
	    		  String suggestion = "";
	    		  
	    		  if(detecttype == 1) 
	    		  {
	    			  score = r.getPsnr();
	    			  m.put("suggestion", "无");
	    		  }
	    		  else if(detecttype == 2)
	    		  {
	    			  score = r.getOverall();
	    			  if(r.getBlur() < 4) suggestion += "调节摄像机焦距，远端通道会受上下变换影响\n";
	    			  if(r.getOverdark() < 4 || r.getOverbright() < 4) suggestion += "调节摄像机光圈，或会场灯光、窗帘等布置\n";
	    			  if(r.getColorcast() < 4) suggestion += "调节摄像机红、蓝通道增益\n";
	    			  if(r.getLowcontrast() < 4) suggestion += "调节摄像机对比度和光圈\n";
	    			  if(chaintype.equals("远端"))
	    				  if(r.getBlackscreen() < 4 || r.getFrozen() < 4 || r.getMosaic() < 4) suggestion += "远程通道信号异常\n";
	    				  
	    				  
	    			  m.put("suggestion", suggestion);
	    		  }
	    			  
	    		  if(4 <= score && score <= 5)
	    		  {
	    			  m.put("localvideostate", "正常");
	    			  m.put("chainstate", "正常");
	    			  m.put("score",score);
	    		  }
	    		  else if(2.5 <= score && score < 4)
	    		  {
	    			  m.put("localvideostate", "异常");
	    			  m.put("chainstate", "异常");
	    			  m.put("score",score);
	    		  }
	    		  else if(1 <= score && score < 2.5)
	    		  {
	    			  m.put("localvideostate", "故障");
	    			  m.put("chainstate", "故障");
	    			  m.put("score",score);
	    		  }
	    	  }

	        //System.out.println(k + " : " + m.get(k));
	      }
	      
	    }
		
		rs.put("Data", chains);
		
		
		List<String> time=new ArrayList<String>();
		for(int i=0;i<chains.size();i++) {
			String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chains.get(i).get("time"));
			time.add(datetime);
		}
		
		rs.put("time", time);
		rs.put("count", count);
		rs.put("countpage", countpage);
		rs.put("flag", "success");
		rs.put("msg", "展示所有通道状态成功");
	
		return rs;
		
		//final WebRegion r = JSONObject.parseObject(gisobject.content, WebRegion.class);
		//final String jsonstr = JSONObject.toJSONString(radarmessage);
	}
	
	//通道状态列表
		@PostMapping(value = "/chainstate/getChainstatesByZonename")
		public Map<String, Object> getAllChainsByZonename(@RequestParam("conferenceid") String conferenceid,
				@RequestParam("detect_type") String detect_type,@RequestParam("zonename") String zonename
				,@RequestParam(value = "pagenum",defaultValue = "1") Integer pagenum)
		{
			Map<String, Object> rs = new HashMap<String, Object>();
			
			if (zonename.isEmpty())
			{
				rs.put("flag", "null");
				rs.put("msg", "通道名为空");
				return rs;
			}
			
			int id = Integer.valueOf(conferenceid);
			
			int detecttype = Integer.valueOf(detect_type);
			
			float count = chainstateService.ListChainstatesByZonenameCount(id,detecttype,zonename);
			
			int countpage = (int) Math.ceil(count / 20);
			
			List<Map<String, Object>> chains; // 本地
			List<Map<String, Object>> chains2; // 远端
			
			if (pagenum < 1 || countpage < 1) {
				
				int pagenum1 = 0;
				chains = chainstateService.ListChainstatesByZonename(pagenum1,id,detecttype,zonename);
				chains2 = chainstateService.ListChainstatesByZonename2(pagenum1,id,detecttype,zonename);
			} else if (pagenum <= countpage) {
				chains = chainstateService.ListChainstatesByZonename((pagenum - 1) * 10,id,detecttype,zonename);
				chains2 = chainstateService.ListChainstatesByZonename2((pagenum - 1) * 10,id,detecttype,zonename);
			} else {
				int pagenum1 = (countpage - 1) * 10;
				chains = chainstateService.ListChainstatesByZonename(pagenum1,id,detecttype,zonename);
				chains2 = chainstateService.ListChainstatesByZonename2(pagenum1,id,detecttype,zonename);
			}

			chains.addAll(chains2);
			if (chains.size() < 1)
			{
				rs.put("flag", "failure");
				rs.put("msg", "没有通道状态数据展示");
				return rs;
			
			}
			
			for (Map<String, Object> m : chains)
		    {
				m.put("distantvideojson", "");
				m.put("networkstatejson", "");
				m.put("localvideojson", "");
				m.put("distantvideostate", "");
				m.put("networkstate", "正常");
				m.put("localvideostate", "");
				m.put("suggestion", "");
				m.put("score", "");
				
				String chaintype = "";
				
				for (String k : m.keySet())
		      {
		    	  
		    	  if(k.equals("detecttype"))
		    	  {
		    		  /*if(m.get(k).equals(0))
		    		  {
		    			  m.put("detecttype", "自动试机试线");
		    			  detecttype = 0;
		    		  }
		    		  else if(m.get(k).equals(1))
		    		  {
		    			  m.put("detecttype", "实时检测");
		    			  detecttype = 1;
		    		  }*/
		    		  if(detecttype == 1)
		    			  m.put("detecttype", "自动试机试线");
		    		  else if(detecttype == 2)
		    			  m.put("detecttype", "实时检测");
		    			  
		    	  }
		    	  else if(k.equals("chaintype"))
		    	  {
		    		  chaintype = (String) m.get(k);
		    		  
		    		  /*if(m.get(k).equals(0))
		    		  {
		    			  m.put("chaintype", "本地");
		    		  }
		    		  else if(m.get(k).equals(1))
		    		  {
		    			  m.put("chaintype", "远端");
		    		  }*/
		    	  }
		    	  /*else if(k.equals("chainstate"))
		    	  {
		    		  float score = (float) m.get(k);
		    		  if(4 <= score && score <= 5)
		    		  {
		    			  m.put("chainstate", "正常");
		    		  }
		    		  else if(3 < score && score <= 4)
		    		  {
		    			  m.put("chainstate", "异常");
		    		  }
		    		  else if(2 < score && score <= 3)
		    		  {
		    			  m.put("chainstate", "故障");
		    		  }
		    		  else if(score < 2)
		    		  {
		    			  m.put("chainstate", "严重故障");
		    		  }
		    	  }*/
		    	  else if(k.equals("distant_video_json")) 
		    	  {
		    		  /*final Distantvideojson r = JSONObject.parseObject(String.valueOf(m.get(k)), Distantvideojson.class);
		    		  m.put("distantvideojson", r);
		    		  int score = r.getMosaic() + r.getBlackscreen() + r.getFrozen();
		    		  if(121 < score && score <= 150)
		    		  {
		    			  m.put("distantvideostate", "正常");
		    		  }
		    		  else if(91 < score && score <= 120)
		    		  {
		    			  m.put("distantvideostate", "异常");
		    		  }
		    		  else if(61 < score && score <= 90)
		    		  {
		    			  m.put("distantvideostate", "故障");
		    		  }
		    		  else if(30 < score && score <= 60)
		    		  {
		    			  m.put("distantvideostate", "严重故障");
		    		  }*/
		    	  }
		    	  else if(k.equals("network_state_json"))
		    	  {
		    		  final Networkstatejson r = JSONObject.parseObject(String.valueOf(m.get(k)), Networkstatejson.class);
		    		  m.put("networkstatejson", r);
		    	  }
		    	  else if(k.equals("local_video_json"))
		    	  {
		    		  final Localvideojson r = JSONObject.parseObject(String.valueOf(m.get(k)), Localvideojson.class);
		    		  m.put("localvideojson", r);
		    		  
		    		  float score = 0;
		    		  
		    		  String suggestion = "";
		    		  
		    		  if(detecttype == 1) 
		    		  {
		    			  score = r.getPsnr();
		    			  m.put("suggestion", "无");
		    		  }
		    		  else if(detecttype == 2)
		    		  {
		    			  score = r.getOverall();
		    			  if(r.getBlur() < 4) suggestion += "调节摄像机焦距，远端通道会受上下变换影响\n";
		    			  if(r.getOverdark() < 4 || r.getOverbright() < 4) suggestion += "调节摄像机光圈，或会场灯光、窗帘等布置\n";
		    			  if(r.getColorcast() < 4) suggestion += "调节摄像机红、蓝通道增益\n";
		    			  if(r.getLowcontrast() < 4) suggestion += "调节摄像机对比度和光圈\n";
		    			  if(chaintype.equals("远端"))
		    				  if(r.getBlackscreen() < 4 || r.getFrozen() < 4 || r.getMosaic() < 4) suggestion += "远程通道信号异常\n";
		    			  m.put("suggestion", suggestion);
		    		  }
		    			  
		    		  if(4 <= score && score <= 5)
		    		  {
		    			  m.put("localvideostate", "正常");
		    			  m.put("chainstate", "正常");
		    			  m.put("score",score);
		    		  }
		    		  else if(3 <= score && score < 4)
		    		  {
		    			  m.put("localvideostate", "异常");
		    			  m.put("chainstate", "异常");
		    			  m.put("score",score);
		    		  }
		    		  else if(2 <= score && score < 3)
		    		  {
		    			  m.put("localvideostate", "故障");
		    			  m.put("chainstate", "故障");
		    			  m.put("score",score);
		    		  }
		    		  else if(score < 2)
		    		  {
		    			  m.put("localvideostate", "严重故障");
		    			  m.put("chainstate", "严重故障");
		    			  m.put("score",score);
		    		  }
		    	  }

		        //System.out.println(k + " : " + m.get(k));
		      }
		      
		    }
			
			rs.put("Data", chains);
			
			
			List<String> time=new ArrayList<String>();
			for(int i=0;i<chains.size();i++) {
				String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chains.get(i).get("time"));
				time.add(datetime);
			}
			
			rs.put("time", time);
			rs.put("count", count);
			rs.put("countpage", countpage);
			rs.put("flag", "success");
			rs.put("msg", "展示当前战区通道状态成功");
		
			return rs;
			
			//final WebRegion r = JSONObject.parseObject(gisobject.content, WebRegion.class);
			//final String jsonstr = JSONObject.toJSONString(radarmessage);
		}
	
	//通道状态列表
	@PostMapping(value = "/chainstate/getChainInfo")
	public Map<String, Object> getChainInfo()
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		String serverip = null;
		try{
			InetAddress addr = InetAddress.getLocalHost();
			serverip = addr.getHostAddress().toString(); //获取本机ip
		}
		catch(Exception e){  
            e.printStackTrace();  
        }  
		
		System.out.println(serverip);
		
		List<Map<String, Object>> infos;
		
		infos = conferenceService.getConferenceInfoByServerip(serverip);
		
		if(infos.size() == 0)
		{
			rs.put("infos",infos);
			rs.put("flag", "failure");
			rs.put("msg", "查询通道状态信息失败");
			return rs;
		}
		else
		{
			rs.put("infos",infos);
			rs.put("flag", "success");
			rs.put("msg", "查询通道状态信息成功");
		}
		
		return rs;
	}
	
	
	
	//后台工作模式控制
		@PostMapping(value = "/chainstate/control")
		public Map<String, Object> control(@RequestParam("controltype") String controltype)
		{
			System.out.println("调用contol");
			System.out.println(controltype);
			
			Map<String, Object> rs = new HashMap<String, Object>();
			
			int type = Integer.valueOf(controltype);
			
			VideoProcessControl m_VideoProcessControl = new VideoProcessControl();
			
			int ret = m_VideoProcessControl.control(type);
			
			rs.put("result", ret);
			
			if(ret >= 0)
			{
				rs.put("flag", "success");
				rs.put("msg", "视频通道质量检测通知成功");
			}
			else {
				rs.put("flag", "failure");
				rs.put("msg", "视频通道质量检测通知失败");
			}
		
			return rs;
		}
	
	  
	  public static void main(String[] args) {
	  
		  VideoProcessControl m_VideoProcessControl = new VideoProcessControl();
			
		  int ret = m_VideoProcessControl.control(1);
			
		  
		  System.out.print(ret);
		  
		  /*String serverip = null;
			try{
				InetAddress addr = InetAddress.getLocalHost();
				serverip = addr.getHostAddress().toString(); //获取本机ip
			}
			catch(Exception e){  
	            e.printStackTrace();  
	        }  
			System.out.println(serverip);*/
	  }
	
	
	

}
