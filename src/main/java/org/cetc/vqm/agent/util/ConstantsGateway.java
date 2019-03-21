package org.cetc.vqm.agent.util;

import java.util.Random;

import com.alibaba.druid.util.StringUtils;

public interface ConstantsGateway{
	String cacheMaster = ConfigManager.getInstance().getString("cacheMaster");//2
	String cacheSlave = ConfigManager.getInstance().getString("cacheSlave");//2
	
	String dist_compute_base_mode = ConfigManager.getInstance().getString("dist_compute_base_mode");
	
	int delay = ConfigManager.getInstance().getInt("delay");
	int pagesize = ConfigManager.getInstance().getInt("pagesize");//2
	
	int dbbatchsize = ConfigManager.getInstance().getInt("dbbatchsize");
	int dbdelaytime = ConfigManager.getInstance().getInt("dbdelaytime");
	int dbdelaytime_fastdata = ConfigManager.getInstance().getInt("dbdelaytime_fastdata");
	
	int paddinglen = ConfigManager.getInstance().getInt("paddinglen");//2
	double default_height = ConfigManager.getInstance().getInt("default_height");//8;
	int distance_step = ConfigManager.getInstance().getInt("distance_step");//8;
	int distance_step_num_max = ConfigManager.getInstance().getInt("distance_step_num_max");//8;
	int distance_step_num_min = ConfigManager.getInstance().getInt("distance_step_num_min");//8;
	int trajectorylen = ConfigManager.getInstance().getInt("trajectorylen");//8;
	int distance_max = distance_step * distance_step_num_max;
	int cameramovingblocknum = 36;//偶数
	float cameramovinganglebound = 360;//度
	
	String split = ";";
	
	String[] types = "radar;sensor;".split(split);
	String prefix_radar = "radar";
	String prefix_sonar = "sensor";
	String prefix_camera = "camera";
	//gateway.properties
	String fastdatamode = ConfigManager.getInstance().getString("fastdatamode");
	boolean isfastdata = StringUtils.equals(fastdatamode, "fastdata") || StringUtils.equals(fastdatamode, "all") ;
	boolean ishistory = StringUtils.equals(fastdatamode, "history") || StringUtils.equals(fastdatamode, "all") ;
	String dbengine = ConfigManager.getInstance().getString("dbengine");
	String fastdbengine = ConfigManager.getInstance().getString("fastdbengine");
	String cameracontrolfile = ConfigManager.getInstance().getString("cameracontrolfile");
	
	Random random = new Random();
	String defaultimage = "default.jpg";
	int messagecount = 5;
	
	int sleeptime_thread = ConfigManager.getInstance().getInt("sleeptime_thread");
	int sleeptime_cameracontroller = ConfigManager.getInstance().getInt("sleeptime_cameracontroller");
	int sleeptime_net = ConfigManager.getInstance().getInt("sleeptime_net");
	int sleeptime_view = ConfigManager.getInstance().getInt("sleeptime_view");
	int sleeptime_camera = ConfigManager.getInstance().getInt("sleeptime_camera");
	int sleeptime_remote_on = ConfigManager.getInstance().getInt("sleeptime_remote_on");
	int sleeptime_remote_off = ConfigManager.getInstance().getInt("sleeptime_remote_off");
	int sleeptime_remote_loop = ConfigManager.getInstance().getInt("sleeptime_remote_loop");
	
	
	String configfile="gateway.properties";
	String ver="1.20170724.v1";
	
	String timestamp_view="yyyy-MM-dd HH:mm:ss";
	String timestamp1="yyyyMMddHHmmssSSS";
	String timestamp1_postfix = "mmssSSS";
	String timestamp="yyyyMMddHHmmss";
	String timestamp_postfix = "ss";
	int timestamp_postfix_length = timestamp_postfix.length();
	int timestamp1_postfix_length = timestamp1_postfix.length();
	String tftp_file_ext=ConfigManager.getInstance().getString("tftp_file_ext");
	String key_log_home = "mylog.home";
	String key_log4jxml = "log4j.configurationFile";
	String key_conf = "conf.prop";
	int countLimit=9999;
	int alertLimit = 60;
	
	int db_lastrecords_num = ConfigManager.getInstance().getInt("db_lastrecords_num");
	int db_query_limit = 1024;
	
	String tmpdir = System.getProperty("java.io.tmpdir");
	String unionmode = ConfigManager.getInstance().getString("unionmode");
	String uploadmode =  ConfigManager.getInstance().getString("uploadmode");
	
	public static String genInfo(final String sensorname,final int objid){
		return sensorname+NumberTool.padding(objid);
	}
	
	public static String getSonarInfo(final int objid){
		return genInfo(prefix_sonar,objid);
	}
	
	public static String getRadarInfo(final int objid){
		return genInfo(prefix_radar,objid);
	}
	
	public static String getSonarInfo(final String objid){
		return genInfo(prefix_sonar,Integer.valueOf(objid));
	}
	
	public static String getRadarInfo(final String objid){
		return genInfo(prefix_radar,Integer.valueOf(objid));
	}
}
