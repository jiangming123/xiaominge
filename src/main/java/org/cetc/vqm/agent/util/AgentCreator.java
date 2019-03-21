package org.cetc.vqm.agent.util;


import org.cetc.vqm.agent.receiver.ThreadVideoDiagnoseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public final class AgentCreator {
	protected static final Logger logger = LoggerFactory.getLogger(AgentCreator.class);

	public static void create(final Agent agent,final String type) {
		if(!type.equals(agent.type)){
			return ;
		}
		final String name = agent.name;
		logger.debug(JSON.toJSONString(agent)+";"+type);
		switch (name) {
		case "tcp_videodiagnose_listener":
			ResourceManager.tcplistener = new Thread(new ThreadVideoDiagnoseListener(agent));
			ResourceManager.tcplistener.setPriority(6);
			ResourceManager.tcplistener.start();
			break;
		/*case "console":
			ResourceManager.agentThreadPool.execute(new ThreadConsole(agent));
			break;
		case "udp_sonar_receiver":
			//System.out.println("jiangmingsonarreceiver");
			ResourceManager.agentThreadPool.execute(new ThreadUDPSonarReceiver(agent));
			break;
		case "udp_sonar_sender":
			ResourceManager.agentThreadPool.execute(new ThreadUDPSonarSender(agent));
			break;
		case "tcp_radar_listener":
			//ResourceManager.agentThreadPool.execute(new ThreadTCPRadarListener(agent));
			ResourceManager.tcplistener = new Thread(new ThreadTCPRadarListener(agent));
			ResourceManager.tcplistener.setPriority(6);
			ResourceManager.tcplistener.start();
			break;
		case "tcp_radar_device":
			System.out.println("tcp_radar_device");
			ResourceManager.agentThreadPool.execute(new ThreadTCPRadarDevice(agent));
			break;
		case "tcp_radar_device_sim":
			System.out.println("tcp_radar_device_sim");
			ResourceManager.agentThreadPool.execute(new ThreadTCPRadarDevice(agent));
			break;
		case "tcp_alert_client":
			System.out.println("tcp_alert_client");
			//ResourceManager.agentThreadPool.execute(new ThreadNetAlert(agent));
			ResourceManager.agentThreadPool.execute(new ThreadNetAlertConfig(agent));
			break;
		case "tcp_radar_camera_tracing":
			System.out.println("tcp_radar_camera_tracing");
			//ResourceManager.agentThreadPool.execute(new ThreadNetAlert(agent));
			ResourceManager.agentThreadPool.execute(new ThreadRadarCameraTracing(agent));
			break;*/			
		default:
			logger.warn("no agent!");
		}
	}
}
