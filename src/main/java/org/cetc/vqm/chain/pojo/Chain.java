package org.cetc.vqm.chain.pojo;

import java.io.Serializable;
import java.util.Date;

public class Chain implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int chainid;
	
	private int chainnumber;
	
	private String chainname;
	
	private String type;
    
    private int conferenceid;
    
    private Date time;

	@Override
	public String toString() {
		return "Chain [chainid=" + chainid + ", chainname=" + chainname + ", conferenceid="
				+ conferenceid + ", time=" + time + "]";
	}

	public int getChainid() {
		return chainid;
	}

	public void setChainid(int chainid) {
		this.chainid = chainid;
	}
	
	public int getChainnumber() {
		return chainnumber;
	}

	public void setChainnumber(int chainnumber) {
		this.chainnumber = chainnumber;
	}

	public String getChainname() {
		return chainname;
	}

	public void setChainname(String chainname) {
		this.chainname = chainname;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getConferenceid() {
		return conferenceid;
	}

	public void setConferenceid(int conferenceid) {
		this.conferenceid = conferenceid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
    
    
    
    
}
