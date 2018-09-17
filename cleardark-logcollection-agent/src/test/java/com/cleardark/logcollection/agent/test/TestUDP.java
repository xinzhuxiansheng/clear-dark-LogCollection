package com.cleardark.logcollection.agent.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

public class TestUDP {
	private static final Logger logger =  LogManager.getLogger("udpAppenderagent");
	
	public static void main(String[] args) {
		DateTime startTime = new DateTime();
		
		logger.info("1");
		logger.info("2");
		logger.info("3");
		
		/*for(int i=0;i<100000;i++) {
			logger.info(i);
		}*/
		System.out.println(startTime.toString("yyyy-MM-dd HH:mm:ss"));
		System.out.println(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
	}
}
