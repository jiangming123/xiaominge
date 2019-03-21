package org.cetc.vqm.agent;

import java.net.InetAddress;

import org.cetc.vqm.chain.pojo.Chain;
import org.cetc.vqm.chainstate.dao.ChainstateImpl;
import org.cetc.vqm.chainstate.pojo.Chainstate;
import org.cetc.vqm.server.ChainDiagnoseMessage;
import org.cetc.vqm.spring.SpringUtil;

import com.alibaba.fastjson.JSONObject;

public class DataSwitcher {

	public  ChainstateImpl dao = SpringUtil.getBean(ChainstateImpl.class);
	
	ChainDiagnoseMessage chaindiagnosemessage;
	
	String clientip;
	
	public ChainDiagnoseMessage getChaindiagnosemessage() {
		return chaindiagnosemessage;
	}

	public void setChaindiagnosemessage(ChainDiagnoseMessage chaindiagnosemessage) {
		this.chaindiagnosemessage = chaindiagnosemessage;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public  DataSwitcher(ChainDiagnoseMessage chaindiagnosemessage, String serverip){
		
		int conferenceid = dao.getConferenceIDbyServerIP(serverip);

		Chain chain = dao.getchain(conferenceid,chaindiagnosemessage.getChainnumber());
		
		if(chain == null) return;
		
		Chainstate dbrecord = new Chainstate();
		
		dbrecord.setChainid(chain.getChainid());
		dbrecord.setDetecttype(chaindiagnosemessage.getDetecttype());
		dbrecord.setChaintype(chain.getType());
		dbrecord.setChainstate(chaindiagnosemessage.getChainstate());
		final String distantvideojsonstr = JSONObject.toJSONString(chaindiagnosemessage.getDistantvideojson());
		dbrecord.setDistant_video_json(distantvideojsonstr);
		final String networkstatejsonstr = JSONObject.toJSONString(chaindiagnosemessage.getNetworkstatejson());
		dbrecord.setNetwork_state_json(networkstatejsonstr);
		final String localvideojsonstr = JSONObject.toJSONString(chaindiagnosemessage.getLocalvideojson());
		dbrecord.setLocal_video_json(localvideojsonstr);
		dbrecord.setPath(chaindiagnosemessage.getPath());
		dao.add(dbrecord);
		if(chaindiagnosemessage.getChainstate() < 4)
			dao.add(chain.getChainid(),chain.getType(),chaindiagnosemessage);

    }
	

	 public static void main(String[] args) {
		  
		  String serverip = "169.254.13.21";
		  
		  DataSwitcher dataSwitcher =new DataSwitcher(null,serverip);
		  
		  
			
		  
	  }
	
}
