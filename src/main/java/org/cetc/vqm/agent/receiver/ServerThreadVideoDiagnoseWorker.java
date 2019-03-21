package org.cetc.vqm.agent.receiver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.cetc.vqm.agent.DataSwitcher;
import org.cetc.vqm.agent.util.AbsLog;
import org.cetc.vqm.agent.util.ConsolePrinter;
import org.cetc.vqm.agent.util.ConstantsGateway;
import org.cetc.vqm.agent.util.ResourceManager;
import org.cetc.vqm.chainstate.dao.ChainstateMapper;
import org.cetc.vqm.server.ChainDiagnoseMessage;
import org.cetc.vqm.server.ChainDiagnoseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.codec.binary.Hex;
import com.alibaba.fastjson.JSONObject;

// inner-class ServerThread
public class ServerThreadVideoDiagnoseWorker extends AbsLog implements Runnable {
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	@Autowired
	ChainstateMapper chainstateMapper;
	

	// Ready to conversation
	public ServerThreadVideoDiagnoseWorker(final Socket s) throws IOException {
		this.socket = s;
		// 构造该会话中的输入输出流
		// in = new BufferedReader(new
		// InputStreamReader(socket.getInputStream(), "GB2312"));socket
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	final int packetlength = 120;
	final byte[] buffer = new byte[packetlength];
	final byte[] packet = new byte[packetlength];
	final byte[] head = { (byte) 0xC9, (byte) 0xF1 };
	
	int readlen=0;
	// Execute conversation
	public void run() {
		final BufferedInputStream buffered_in = new BufferedInputStream(in);
		try {
			recvpacket(buffered_in);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				buffered_in.close();
				socket.close();
				out = null;
				in = null;
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	


	void recvpacket(final BufferedInputStream buffered_in) throws Exception {
		// Communicate with client until "bye " received.
		
		while (ResourceManager.isRunning) {
			// 通过输入流接收客户端信息
			readlen = buffered_in.read(buffer, 0, packetlength);
			
			if(buffer[0]==head[0]&&buffer[1]==head[1]){
				alignmentpacket(buffered_in);
			}else{
				noalignmentpacket(buffered_in);
			}
		}
		// 通过输出流向客户端发送信息
		// out.println(line);
		// out.flush();
	}
	
	public static String toHexString(byte[] byteArray) {
		  if (byteArray == null || byteArray.length < 1)
		   throw new IllegalArgumentException("this byteArray must not be null or empty");
		 
		  final StringBuilder hexString = new StringBuilder();
		  for (int i = 0; i < byteArray.length; i++) {
		   if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
		    hexString.append("0");
		   hexString.append(Integer.toHexString(0xFF & byteArray[i]));
		  }
		  return hexString.toString().toLowerCase();
		 }
	
	void alignmentpacket(final BufferedInputStream buffered_in) throws Exception {
		// Communicate with client until "bye " received.
		if(readlen == packetlength){
			logger.debug("packet is completed");
			System.arraycopy(buffer, 0, packet, 0, packetlength);
			finishOnePacket(packet);
		}else if(readlen < packetlength){
			logger.warn("packet is not completed");
			System.arraycopy(buffer, 0, packet, 0, readlen);
			int bufpos = readlen;
			int copylen = packetlength-bufpos;
			readlen = buffered_in.read(buffer, 0, copylen);
			System.arraycopy(buffer, 0, packet, bufpos, readlen);
			if(readlen == copylen){
				finishOnePacket(packet);
			}else{
				logger.warn("packet is not completed and discarded");
			}

		}		

	}

	void noalignmentpacket(final BufferedInputStream buffered_in) throws Exception {
		logger.warn("packet is not aligned");
		for(int bufpos = 0;bufpos<readlen-2;bufpos++){
			if (buffer[bufpos] == head[0]) {
				if (buffer[bufpos + 1] == head[1]) {
					int copylen = packetlength-bufpos;
					System.arraycopy(buffer, bufpos, packet, 0, copylen);
					logger.debug("copylen="+(packetlength-bufpos));
					readlen = buffered_in.read(buffer, 0, bufpos);
					if(readlen==bufpos){
						System.arraycopy(buffer, 0, packet, copylen, bufpos);
						finishOnePacket(packet);
					}else{
						logger.warn("packet is not completed");
					}
					break;
				}
			}
		}		
	}

	void finishOnePacket(final byte[] packet) throws Exception {
		final String source_host_port = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		final String target_host_port = socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort();
		final String hexdata = Hex.encodeHexString(packet);
		final ChainDiagnoseMessage chaindiagnosemessage = ChainDiagnoseParser.parse(packet);
		logger.info(source_host_port + "=>" + target_host_port);
		logger.info("Received " + packet.length + " bytes : " + hexdata);
		final String clientip = socket.getLocalAddress().getHostAddress();

		String serverip = null;
		try{
			InetAddress addr = InetAddress.getLocalHost();
			serverip = addr.getHostAddress().toString(); //获取本机ip
		}
		catch(Exception e){  
            e.printStackTrace();  
        }  

		DataSwitcher dataSwitcher =new DataSwitcher(chaindiagnosemessage,serverip);
		
		
		
		

		//DataSwitcher.upload(chaindiagnosemessage,clientip);
		//putdb(source_host_port, target_host_port, chaindiagnosemessage, hexdata);
	}

	/*void putdb(final String source_host_port, final String target_host_port, final RadarMessage radarmessage, final String packet) throws Exception {
		//final String timeinfo = DateTimeTool.getCurrentTimestamp();
		/*final String timeinfo = radarmessage.getTimeinfo();
		logger.info(source_host_port + "=>" + target_host_port);
		final String host = source_host_port.split(":")[0];
		final String key = ConstantsGateway.prefix_radar+":"+host;
		final String[] objids = ConfigDevices.getInstance().find(key);
		final String jsonstr = JSONObject.toJSONString(radarmessage);*/
		//logger.info(jsonstr);
		
		
		
		/*for (String objid : objids) {
			final String sensorname = ConstantsGateway.getRadarInfo(objid);
			final DbRecord dbrecord = new DbRecord();
			// dbrecord.setUid(timestamp);
			dbrecord.setJson(jsonstr);
			dbrecord.setSource(source_host_port);
			dbrecord.setInfo(sensorname);
			dbrecord.setTimeinfo(timeinfo);
			dbrecord.setAlert(radarmessage.getAlert());
			dbrecord.setCreationtimestamp(DateTimeTool.getCurrentTimestamp());
			dbrecord.setPacket(packet);
			DataSwitcher.upload(sensorname,dbrecord);
			logger.info(jsonstr);
		}*/

	//}
	
	
	//WebConfigListener

}