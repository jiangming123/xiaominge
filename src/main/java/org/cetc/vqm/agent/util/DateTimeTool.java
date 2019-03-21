package org.cetc.vqm.agent.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public final class DateTimeTool {
	final static FastDateFormat df = FastDateFormat.getInstance(ConstantsGateway.timestamp1);// 设置日期格式
	final static FastDateFormat df_view = FastDateFormat.getInstance(ConstantsGateway.timestamp_view);
	
	public static String getCurrentTimeinfo() {
		return df.format(new Date());// new Date()为获取当前系统时间
	}
	
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());// new Date()为获取当前系统时间
	}
	
	public static String format(final Timestamp timestamp) {
		//return timestamp.toString();
		return df_view.format(timestamp);// new Date()为获取当前系统时间
	}

	public static String format(final String timeinfo) {
		String formated_timeinfo = timeinfo;
		try {
			formated_timeinfo = df_view.format(df.parse(timeinfo));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formated_timeinfo;// new Date()为获取当前系统时间
	}
	
	public static Timestamp getTimestamp(final String timeinfo) {
		Timestamp ts = null ;
		try {
			ts = new Timestamp(df.parse(timeinfo).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ts;// new Date()为获取当前系统时间
	}
	
	public static long delay2currenttime(final String timeinfo){
		long r = 0;
		try {
			Date d1 = df.parse(timeinfo);
			Date d2 = new Date();
			r = d2.getTime() - d1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static boolean isDelay2currenttime(final String timeinfo){
		return delay2currenttime(timeinfo)>ConstantsGateway.delay;
	}
	
	public static void main(String[] args){
		System.out.println(getCurrentTimestamp());
	}
}
