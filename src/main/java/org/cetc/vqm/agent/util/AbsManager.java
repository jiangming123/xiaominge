package org.cetc.vqm.agent.util;


import com.alibaba.druid.util.StringUtils;

public abstract class AbsManager extends AbsLog{
	public final String configkey = "devices";
	public String Trim(String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}else{
			return value.trim();
		}
	}
}
