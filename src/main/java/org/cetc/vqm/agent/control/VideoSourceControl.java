package org.cetc.vqm.agent.control;

import org.cetc.vqm.agent.util.Agent;
import org.cetc.vqm.agent.util.AgentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoSourceControl {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Agent tcp_videosource_control;
	public VideoSourceNetWorker videosource;
	
	public VideoSourceControl() {
		init();
	}
	
	public void init(){
		tcp_videosource_control = AgentManager.getInstance().findAgent("tcp_videosource_control");
		final String[] peer = tcp_videosource_control.peer.split(";");
		String host = peer[0];
		int port = Integer.parseInt(peer[1]);
		videosource = new VideoSourceNetWorker(host, port);
	}
	
	//视频源控制	
	public VideoSourceCommand control(String time,String pattern) {
			final VideoSourceCommand cmd = new VideoSourceCommand();
			cmd.setTime(time);
			cmd.setPattern(pattern); 
			execute(cmd);
			return cmd;
	}
	
	//视频源数据查询	
		public VideoSourceCommand control() {
				final VideoSourceCommand cmd = new VideoSourceCommand();
				execute2(cmd);
				return cmd;
		}
	
	public void execute(final VideoSourceCommand cmd) {
		try {
			System.out.println("startvideosourcecontrol");
			videosource.initsocket();
			videosource.control2(cmd.toString());
			videosource.closesocket();
		} catch (Exception e) {
			try {
				videosource.closesocket();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.println("overvideosourcecontrol");
	}
	
	public void execute2(final VideoSourceCommand cmd) {
		try {
			System.out.println("startvideosourceinfo");
			videosource.initsocket();
			videosource.control2(cmd.toString2());
			videosource.closesocket();
		} catch (Exception e) {
			try {
				videosource.closesocket();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		System.out.println("overvideosourceinfo");

	}
	//videosource.closesocket();
}
