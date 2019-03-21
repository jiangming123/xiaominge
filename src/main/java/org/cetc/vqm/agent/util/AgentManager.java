package org.cetc.vqm.agent.util;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public final class AgentManager extends AbsManager{
	private static AgentManager agentManager;
	final String configkey = "agents";
	// 通过单例模式实例化个数
	public static AgentManager getInstance() {
		if (agentManager == null) {
			agentManager = new AgentManager();
		}
		//System.out.println("agentManager");
		return agentManager;
	}

	public final Properties properties;
	public final String agentsconfig;

	private AgentManager() {
		agentsconfig = ConfigManager.getInstance().getString(configkey);

		if(agentsconfig==null){
			System.out.println("disable "+configkey);
			properties = null;
		}else{
			properties = ConfigFileLoader.load(agentsconfig);

			init();
		}
	}

	private void init() {
		final Set<String> keys = properties.stringPropertyNames();
		System.out.println(keys);
		for (String key : new TreeSet<String>(keys)) {
			if (key.startsWith("agent.") && key.endsWith(".name")) {
				final Agent a = new Agent();
				a.setName(Trim(properties.getProperty(key)));
				final String prefix = key.substring(0, key.length() - "name".length());
				a.setHost(Trim(properties.getProperty(prefix + "host")));
				a.setPort(Integer.valueOf(Trim(properties.getProperty(prefix + "port"))));
				a.setType(Trim(properties.getProperty(prefix + "type")));
				a.setInfo(Trim(properties.getProperty(prefix + "info")));
				a.setPeer(Trim(properties.getProperty(prefix + "peer")));
				a.setArgs(Trim(properties.getProperty(prefix + "args")));
				agents.add(a);
			}
		}
		System.out.println(JSONObject.toJSONString(agents));
	}

	public Agent findAgent(String name){
		return getTargetAgent(name);
	}
	
	public Agent getTargetAgent(final String targetAgentName) {
		if(StringUtils.isEmpty(targetAgentName)){//isNullOrEmpty(targetAgentName)){
			return null;
		}
		Agent a = null;
		for (Agent agent : agents) {
			if (targetAgentName.equals(agent.getName())) {
				a = agent;
			}
		}
		return a;
	}
	
	public final ArrayList<Agent> agents = new ArrayList<Agent>();

}
