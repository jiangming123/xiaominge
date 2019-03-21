package org.cetc.vqm.server;

import org.cetc.vqm.chainstate.pojo.Distantvideojson;
import org.cetc.vqm.chainstate.pojo.Localvideojson;
import org.cetc.vqm.chainstate.pojo.Networkstatejson;

public class ChainDiagnoseMessage {//extends AbsMessage{
	
	private int chainnumber;   //通道编号
	
	private int detecttype;  //检测类型
	
	private float chainstate;  //通道状态
	
	private Distantvideojson distantvideojson;

	private Networkstatejson networkstatejson;
	
	private Localvideojson localvideojson;
	
	private String path;
	
	private String time;

	public int getChainnumber() {
		return chainnumber;
	}

	public void setChainnumber(int chainnumber) {
		this.chainnumber = chainnumber;
	}

	public int getDetecttype() {
		return detecttype;
	}

	public void setDetecttype(int detecttype) {
		this.detecttype = detecttype;
	}

	public float getChainstate() {
		return chainstate;
	}

	public void setChainstate(float chainstate) {
		this.chainstate = chainstate;
	}

	public Distantvideojson getDistantvideojson() {
		return distantvideojson;
	}

	public void setDistantvideojson(Distantvideojson distantvideojson) {
		this.distantvideojson = distantvideojson;
	}

	public Networkstatejson getNetworkstatejson() {
		return networkstatejson;
	}

	public void setNetworkstatejson(Networkstatejson networkstatejson) {
		this.networkstatejson = networkstatejson;
	}

	public Localvideojson getLocalvideojson() {
		return localvideojson;
	}

	public void setLocalvideojson(Localvideojson localvideojson) {
		this.localvideojson = localvideojson;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
