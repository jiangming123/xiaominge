package org.cetc.vqm;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.cetc.vqm.agent.DataSwitcher;
import org.cetc.vqm.agent.util.Agent;
import org.cetc.vqm.agent.util.AgentCreator;
import org.cetc.vqm.agent.util.AgentManager;
import org.cetc.vqm.spring.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SpringUtil.class)
public class VQMApplication extends SpringBootServletInitializer{

	
	public static void main(String[] args) {
		SpringApplication.run(VQMApplication.class, args);
		System.out.println("project started");
		
		for (Agent agent : AgentManager.getInstance().agents) {    //agents=agents_deploy.properties
			AgentCreator.create(agent, "local");
		}
		
		/*ChainstateImpl Dao = new ChainstateImpl();
		Chainstate dbrecord = new Chainstate();
		dbrecord.chainid = 6;
		dbrecord.detecttype = 1;
		dbrecord.chaintype = 1;
		dbrecord.chainstate = 1;
		dbrecord.distant_video_json = "正常";
		dbrecord.network_state_json = "正常";
		dbrecord.local_video_json = "正常";
		dbrecord.display = 1;
		Dao.add("chainstate", dbrecord);*/
		
		/*String serverip = "169.254.13.21";
		  
		DataSwitcher dataSwitcher =new DataSwitcher(null,serverip);*/

	}
	
	@Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
