package org.cetc.vqm.agent.util;

import java.io.InputStreamReader;
import java.util.Properties;

public final class ConfigFileLoader {

	public static Properties load(final String configFile) {
		System.out.println(configFile);
		final Properties properties = new Properties();
		try {
			final InputStreamReader in = new InputStreamReader(
					ConfigManager.class.getClassLoader().getResourceAsStream(configFile), "UTF-8");
			// 读取配置文件
			properties.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

}
