package com.proquest.apac.summontool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main controller for automatically update tool
 * @author PChen
 *
 */
public class SummonToolRunner {
	private static Logger logger = LoggerFactory.getLogger(SummonToolRunner.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring_sync.xml");
		logger.info("SummonAutoSync started......");
	}
}
