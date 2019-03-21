package org.cetc.vqm.agent.control;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.cetc.vqm.agent.util.AbsLog;
import org.cetc.vqm.agent.util.ConsolePrinter;

public class VideoSourceNetWorker extends AbsLog{
	final int off_time = 10 * 1000;
	final String host;
	final int port;
	Socket socket;
	final byte[] buffer = new byte[100];
	public static String answer1 = "";
	public static String answer2 = "";
	public static String answer3 = "";
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public VideoSourceNetWorker(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	public Socket initsocket() throws Exception {
		socket = new Socket(host, port);
		socket.setSoTimeout(off_time);
		answer1 = "";
		answer2 = "";
		answer3 = "";
		return socket;
	}
	
	public void closesocket() throws Exception {
		socket.close();
	}
	
	public void sendcmd(final String[] cmds) throws Exception {
		final PrintWriter output = new PrintWriter(socket.getOutputStream());
		 final BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		for (String cmd : cmds) {
			String msg = cmd;
			logger.debug(msg);
			output.println(msg);
			output.flush();
			 String recv = input.readLine();
			 String value = new String(recv.getBytes("ISO-8859-1"),"utf-8");
			 ConsolePrinter.printInRect(value);
			 logger.debug(recv);
		}
	}

	public String sendcmd(final String cmd) throws Exception {
		final PrintWriter output = new PrintWriter(socket.getOutputStream());
		BufferedInputStream buffered_in = new BufferedInputStream(socket.getInputStream());
		//final BufferedReader input = new BufferedReader(new	InputStreamReader(socket.getInputStream(),"UTF-8"));
		byte[] buffer = new byte[256];
		String[] msg = new String[2];
		msg[0] = "视频源控制： " + cmd;
		msg[1] = cmd;
		ConsolePrinter.printInRect(msg);
		output.println(msg[1]);
		output.flush();
		int ret = buffered_in.read(buffer,0, 3);     //  3个字节
		ret = buffered_in.read(buffer,0, 256);
		String answer = new String(buffer,"gbk");
		logger.debug(answer);
		
		answer.replace("\n", "");
		
		String[] sourceStrArray = answer.split("\r");

		answer1 = sourceStrArray[1];
		answer2 = sourceStrArray[2];
		answer3 = sourceStrArray[3];
		
		return cmd;
	}
	
	public void control(final String command) throws Exception {
		final String[] cmds = command.split(";");
		sendcmd(cmds);
		return ;
	}

	public void control2(final String command) throws Exception {
		sendcmd(command);
		return ;
	}

}
