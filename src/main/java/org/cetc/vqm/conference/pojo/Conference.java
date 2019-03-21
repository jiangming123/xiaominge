package org.cetc.vqm.conference.pojo;

import java.io.Serializable;
import java.util.Date;

public class Conference implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private int zoneid;

    private String conferencename;

    private String serverip;
    
    private Date time;

	
	@Override
	public String toString() {
		return "Conference [zoneid=" + zoneid + ", conferencename=" + conferencename + ", serverip=" + serverip + ", time=" + time + "]";
	}


	public int getZoneid() {
		return zoneid;
	}


	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
	}

	public String getConferencename() {
		return conferencename;
	}


	public void setConferencename(String conferencename) {
		this.conferencename = conferencename;
	}


	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}



}
