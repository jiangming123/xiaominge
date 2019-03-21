package org.cetc.vqm.agent.util;

import org.apache.commons.lang3.StringUtils;

public class ConsolePrinter {
	final static int num = 64;
	public static String getLine(int num,char element){
		StringBuilder line = new StringBuilder();
		for (int i=0;i<num;i++){
			line.append(element);
		}
		return line.toString();
	}
	
	public static void printInRect(String msg){
		char element = '*';
		String line = getLine(num,element);
		System.out.println(line);
		System.out.println('*'+" "+msg);
		System.out.println(line);
	}
	
	public static void printInRect(String[] msg){
		char element = '*';
		String line = getLine(num,element);
		System.out.println(line);
		for(String m:msg){
			if(StringUtils.isNotEmpty(m)){
				System.out.println('*'+" "+m);
			}
		}
		System.out.println(line);
	}
	
	public static void main(String[] args) {
		String[] msg = {"hello","hello"};
		msg[1] = StringTool.format("abc", ' ', 12);
		printInRect(msg);
	}

}
