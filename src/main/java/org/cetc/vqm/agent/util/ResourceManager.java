package org.cetc.vqm.agent.util;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cetc.vqm.server.ChainDiagnoseMessage;

public final class ResourceManager {
	public static final ExecutorService sensorThreadPool = Executors.newCachedThreadPool();
	public static final ExecutorService dbThreadPool = Executors.newCachedThreadPool();
	public static final ExecutorService agentThreadPool = Executors.newCachedThreadPool();
	public static final ExecutorService cameraThreadPool = Executors.newCachedThreadPool();
	public static final Set<String> cameraSessions = new HashSet<String>();
	public static final Map<String,ChainDiagnoseMessage> jsonpool = new ConcurrentHashMap<String,ChainDiagnoseMessage>();
	public static final Map<String,List<ChainDiagnoseMessage>> cachepool = new ConcurrentHashMap<String,List<ChainDiagnoseMessage>>();
	public static Boolean isRunning = true;
	public static ServerSocket srvsock=null;
	public static Thread tcplistener = null;
}
