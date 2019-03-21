package org.cetc.vqm.warzone.pojo;

import java.io.Serializable;
import java.util.Date;

public class Warzone implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int zoneid;

    private String zonename;
    
    private String position;
    
    private int conference_state;
    
    private Date time;
	
	@Override
	public String toString() {
		return "Warzone [zoneid=" + zoneid + ", zonename=" + zonename + ", position=" + position + ", conference_state="
				+ conference_state + ", time=" + time + "]";
	}


	public int getZoneid() {
		return zoneid;
	}


	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getZonename() {
		return zonename;
	}

	public void setZonename(String zonename) {
		this.zonename = zonename;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getConference_state() {
		return conference_state;
	}

	public void setConference_state(int conference_state) {
		this.conference_state = conference_state;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
