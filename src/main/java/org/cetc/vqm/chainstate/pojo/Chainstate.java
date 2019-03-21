package org.cetc.vqm.chainstate.pojo;

import java.util.Date;

public class Chainstate {

	private static final long serialVersionUID = 1L;
	
	private int chainstateid;
	
	private int chainid;
	
	private int detecttype;
	
	private String chaintype;
	
	private float chainstate;
	
	private String distant_video_json;
	
	private String network_state_json;
	
	private String local_video_json;

	private String path;
	
	private int display;
	
	private Date time;
	
	@Override
	public String toString() {
		return "chainstate [chainstateid=" + chainstateid + ", chainid=" + chainid + ", detecttype=" + detecttype
				+ ", chainstate=" + chainstate + ", distant_video_json=" + distant_video_json + ", network_state_json="
				+ network_state_json + ", local_video_json=" + local_video_json + ", time=" + time + "]";
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public int getChainstateid() {
		return chainstateid;
	}

	public void setChainstateid(int chainstateid) {
		this.chainstateid = chainstateid;
	}

	public int getChainid() {
		return chainid;
	}

	public void setChainid(int chainid) {
		this.chainid = chainid;
	}

	public int getDetecttype() {
		return detecttype;
	}

	public void setDetecttype(int detecttype) {
		this.detecttype = detecttype;
	}

	public String getChaintype() {
		return chaintype;
	}

	public void setChaintype(String chaintype) {
		this.chaintype = chaintype;
	}

	public float getChainstate() {
		return chainstate;
	}

	public void setChainstate(float chainstate) {
		this.chainstate = chainstate;
	}

	public String getDistant_video_json() {
		return distant_video_json;
	}

	public void setDistant_video_json(String distant_video_json) {
		this.distant_video_json = distant_video_json;
	}

	public String getNetwork_state_json() {
		return network_state_json;
	}

	public void setNetwork_state_json(String network_state_json) {
		this.network_state_json = network_state_json;
	}

	public String getLocal_video_json() {
		return local_video_json;
	}

	public void setLocal_video_json(String local_video_json) {
		this.local_video_json = local_video_json;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	
	
}
