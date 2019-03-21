package org.cetc.vqm.agent.control;

import org.cetc.vqm.agent.util.ConstantsGateway;

public class VideoSourceCommand {

	
	String time;
	String pattern;
	String type;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return "time " + time + ConstantsGateway.split + "pattern " + pattern + ConstantsGateway.split + "play"	+ ConstantsGateway.split;
	}
	
	public String toString2() {
		return "info?";
	}
	
	
	
	
}
