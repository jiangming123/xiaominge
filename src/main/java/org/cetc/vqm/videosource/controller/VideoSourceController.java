package org.cetc.vqm.videosource.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.agent.control.VideoProcessControl;
import org.cetc.vqm.agent.control.VideoSourceControl;
import org.cetc.vqm.agent.control.VideoSourceNetWorker;
import org.cetc.vqm.agent.util.AbsLog;
import org.cetc.vqm.agent.util.ConsolePrinter;
import org.cetc.vqm.agent.util.StringTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoSourceController {
	
	@PostMapping(value = "/videosource/play")
	public Map<String, Object> play(@RequestParam("time") String time,@RequestParam("pattern") String pattern)
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		new VideoSourceControl().control(time,pattern);
		
		if(VideoSourceNetWorker.answer1.equals(""))
		{
			rs.put("flag", "failure");
			rs.put("msg", "视频源播放数据返回失败");
			return rs;
		}
		
		rs.put("time", VideoSourceNetWorker.answer1);
		rs.put("pattern", VideoSourceNetWorker.answer2);
		rs.put("play", VideoSourceNetWorker.answer3);
		
		if(VideoSourceNetWorker.answer1.substring(0, 1).equals("T") && VideoSourceNetWorker.answer2.substring(0, 1).equals("T"))
		{
			VideoProcessControl m_VideoProcessControl = new VideoProcessControl();
			int ret = m_VideoProcessControl.notice(time,pattern);
			if(ret >= 0) {
				rs.put("msg", "视频源播放成功");
				rs.put("flag", "success");
			}else {
				rs.put("msg", "视频源播放失败");
				rs.put("flag", "failure");
			}
		}
		else
		{
			rs.put("msg", "视频源播放失败");
			rs.put("flag", "failure");
		}
	
		return rs;
	}
	
	@PostMapping(value = "/videosource/info")
	static public Map<String, Object> info()
	{
		Map<String, Object> rs = new HashMap<String, Object>();
		
		new VideoSourceControl().control();
		
		if(VideoSourceNetWorker.answer1.equals(""))
		{
			rs.put("flag", "failure");
			rs.put("msg", "视频源查询失败");
			return rs;
		}
			
		
		String[] sourceStrArray = VideoSourceNetWorker.answer1.split("\\：");
		String temp = sourceStrArray[1];
		sourceStrArray = temp.split("\\，");
		List<String> videoformats=new ArrayList<String>();
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
            videoformats.add(sourceStrArray[i]);
        }
        
        sourceStrArray = VideoSourceNetWorker.answer2.split("\\：");
		temp = sourceStrArray[1];
		sourceStrArray = temp.split("\\，");
		List<String> HDs=new ArrayList<String>();
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
            HDs.add(sourceStrArray[i]);
        }
        
        sourceStrArray = VideoSourceNetWorker.answer3.split("\\：");
		temp = sourceStrArray[1];
		sourceStrArray = temp.split("\\，");
		List<String> SDs=new ArrayList<String>();
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
            SDs.add(sourceStrArray[i]);
        }
		
		rs.put("videoformats", videoformats);
		rs.put("HDs", HDs);
		rs.put("SDs", SDs);
		
		rs.put("flag", "success");
		rs.put("msg", "视频源查询成功");
	
		return rs;
	}
	
	public static void main(String[] args) {
		String[] msg = {"hello","hello"};
		
		info();
		
		return ;
		//new VideoSourceControl().control();
		

		/*String[] sourceStrArray = VideoSourceNetWorker.answer1.split("\\：");
		String temp = sourceStrArray[1];
		sourceStrArray = temp.split("\\，");
		List<String> videoformats=new ArrayList<String>();
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
            videoformats.add(sourceStrArray[i]);
        }*/
        
        /*new VideoSourceControl().control("0","1");
        System.out.println(VideoSourceNetWorker.answer1);
        System.out.println(VideoSourceNetWorker.answer2);
        System.out.println(VideoSourceNetWorker.answer3);
        
        
        if(VideoSourceNetWorker.answer1.substring(0, 1).equals("T") && VideoSourceNetWorker.answer2.substring(0, 1).equals("T")
				&& VideoSourceNetWorker.answer3.substring(0, 1).equals("T"))
		{
        	System.out.println("视频源播放成功");
		}
		else System.out.println("视频源播放失败");*/
        
		
	}
	

}
