package org.cetc.vqm.agent.util;


public abstract class AbsAgent extends AbsLog {
	public final Agent agent;
	public final int sleeptime = ConstantsGateway.sleeptime_net;
	public final int sleeptime_view = ConstantsGateway.sleeptime_view;
	public AbsAgent(final Agent agent) {
		super();
		this.agent = agent;
		logger.debug("starting a agent:"+this.toString());
	}
}
