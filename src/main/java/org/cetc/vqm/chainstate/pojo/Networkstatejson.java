package org.cetc.vqm.chainstate.pojo;

public class Networkstatejson {


	int state;
	
	int fault;
	
	String reason;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getFault() {
		return fault;
	}

	public void setFault(int fault) {
		this.fault = fault;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	

}
