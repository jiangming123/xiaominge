package org.cetc.vqm.agent.util;

import java.util.Properties;

import com.alibaba.druid.util.StringUtils;

public final class ConfigManager {
	private static ConfigManager configManager;

	// 通过单例模式实例化个数
	public static ConfigManager getInstance() {
		if (configManager == null) {
			configManager = new ConfigManager();
		}
		return configManager;
	}

	private final Properties properties;

	public Properties getProperties() {
		return properties;
	}

	// 在构造工具类是读取配置文件
	private ConfigManager() {
		String gatewayconf = System.getProperty(ConstantsGateway.key_conf);
		if(StringUtils.isEmpty(gatewayconf)){
			gatewayconf = ConstantsGateway.configfile;    //gateway.properties
		}
		properties = ConfigFileLoader.load(gatewayconf); // 配置文件路径 文件不存在
	}

	// 通过Key获取对应的value的值
	public String getString(final String key) {
		return properties.getProperty(key);
	}
	
	// 通过Key获取对应的value的值
	public int getInt(final String key) {
		final String keyvalue = properties.getProperty(key).trim();
		return Integer.parseInt(keyvalue);
	}

	public final String ver = ConstantsGateway.ver;
	
	public boolean separated() {
		return "separated".equals(getString("mode"));
	}
	
}
