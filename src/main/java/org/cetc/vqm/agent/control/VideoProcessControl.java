package org.cetc.vqm.agent.control;

import org.cetc.vqm.agent.util.Agent;
import org.cetc.vqm.agent.util.AgentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoProcessControl {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Agent tcp_videoprocess_control;
	public VideoProcessNetWorker videoprocess;
	
	public VideoProcessControl() {
		init();
	}
	
	public void init(){
		tcp_videoprocess_control = AgentManager.getInstance().findAgent("tcp_videoprocess_control");
		final String[] peer = tcp_videoprocess_control.peer.split(";");
		String host = peer[0];
		int port = Integer.parseInt(peer[1]);
		videoprocess = new VideoProcessNetWorker(host, port);
	}
	
	//视频源通知	
	public int notice(String time,String pattern) {
		int ret = 0;
		try {
			System.out.println("startnotice");
			videoprocess.initsocket();
			ret = videoprocess.sendnotice(time,pattern);
			videoprocess.closesocket();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		System.out.println("overnotice");
			return ret;
	}
	
	//视频通道质量检测控制
	public int control(int type) {
		// TODO Auto-generated method stub
		int ret = 0;
		try {
			System.out.println("startcontrol");
			videoprocess.initsocket();
			ret = videoprocess.sendcontrol(type);
			videoprocess.closesocket();
			System.out.println("overcontrol");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		return ret;
		
	}
	
	public static void main(String[] args) {
		
		new VideoProcessControl().notice("1","1");
		
		return ;
	}

	
		

}
