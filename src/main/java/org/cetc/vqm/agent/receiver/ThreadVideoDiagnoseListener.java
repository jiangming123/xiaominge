package org.cetc.vqm.agent.receiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.cetc.vqm.agent.util.AbsAgent;
import org.cetc.vqm.agent.util.Agent;
import org.cetc.vqm.agent.util.ConsolePrinter;
import org.cetc.vqm.agent.util.ResourceManager;


public class ThreadVideoDiagnoseListener extends AbsAgent implements Runnable {
	public ThreadVideoDiagnoseListener(final Agent agent) {
		super(agent);
	}

	public void run() {
		//ResourceManager.tcplistener = Thread.currentThread();
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(srvsock!=null){
				try {
					srvsock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				srvsock=null;
			}
		}
	}
	ServerSocket srvsock=null;
	void listen() throws IOException {
		int serverPort = agent.getPort();
		srvsock = new ServerSocket(serverPort);
		ResourceManager.srvsock = srvsock;
		final String target_host_port = agent.host + ":" + agent.port;
		ConsolePrinter.printInRect("Server TCP Listener: "+target_host_port);
		logger.debug("Listening " + srvsock.getInetAddress().getHostAddress() + ":" + serverPort);
		while (ResourceManager.isRunning) {
			// 监听一端口，等待客户接入
			final Socket socket = srvsock.accept();
			// 将会话交给线程处理
			String conn_info = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
			logger.info("connecting a client :" + conn_info);
			Runnable worker = new ServerThreadVideoDiagnoseWorker(socket);
			ResourceManager.agentThreadPool.execute(worker);
			if(Thread.currentThread().isInterrupted()){
				break;
			}
		}
		
		srvsock.close();
		srvsock=null;
		
	}
	// main method

}