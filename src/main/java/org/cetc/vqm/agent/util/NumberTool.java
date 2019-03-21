package org.cetc.vqm.agent.util;

import java.math.BigDecimal;

public final class NumberTool {
	
	public static double round(final double num){
		final BigDecimal a = new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP);
		return a.doubleValue();
	}
	
	public static String padding(final int num){
		return padding(num,ConstantsGateway.paddinglen);
	}
	
	public static String padding(final int num,final int len){
		String numstr = Integer.toString(num);
		if(numstr.length()<len){
			for(int i=0;i<len-numstr.length();i++){
				numstr = "0"+numstr;
			}
		}
		return numstr;
	}
	
	public static double distance(final double x1,final double y1,final double x2,final double y2){
		final double c = (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
		return Math.sqrt(c);
	}
}
