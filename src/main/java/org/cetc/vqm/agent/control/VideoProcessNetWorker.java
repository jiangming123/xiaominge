package org.cetc.vqm.agent.control;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.cetc.vqm.agent.util.AbsLog;
import org.cetc.vqm.agent.util.ConsolePrinter;

public class VideoProcessNetWorker extends AbsLog{
	final int off_time = 10 * 1000;
	final String host;
	final int port;
	Socket socket;
	final byte[] buffer = new byte[100];
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public VideoProcessNetWorker(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	public Socket initsocket() throws Exception {
		socket = new Socket(host, port);
		socket.setSoTimeout(off_time);

		return socket;
	}
	
	public void closesocket() throws Exception {
		socket.close();
	}
	

	public int sendnotice(String time,String pattern) throws Exception {
		
		final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		BufferedInputStream buffered_in = new BufferedInputStream(socket.getInputStream());
		byte[] buffer = new byte[256];
		buffer[0] = (byte) 0xC9;
		buffer[1] = (byte) 0xF2;
		buffer[2] = 5;
		buffer[3] = (byte) Integer.parseInt(time);
		buffer[4] = (byte) Integer.parseInt(pattern);
		output.write(buffer, 0, 5);
		int ret = buffered_in.read(buffer,0, 256);
		return buffer[0];
	}
	
	public int sendcontrol(int type) throws Exception {
		
		final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		BufferedInputStream buffered_in = new BufferedInputStream(socket.getInputStream());
		byte[] buffer = new byte[256];
		buffer[0] = (byte) 0xC9;
		buffer[1] = (byte) 0xF2;
		buffer[2] = (byte) type;
		output.write(buffer, 0, 3);
		int ret = buffered_in.read(buffer,0, 256);
		return buffer[0];
	}

	

}
