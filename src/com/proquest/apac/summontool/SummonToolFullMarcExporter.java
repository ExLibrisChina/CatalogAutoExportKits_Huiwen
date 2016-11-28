package com.proquest.apac.summontool;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Controller class for full export config and run
 * @author PChen
 *
 */
public class SummonToolFullMarcExporter {

	private static Logger logger = LoggerFactory.getLogger(SummonToolFullMarcExporter.class);
	
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring_fullexport.xml");
		MarcExportRunner runner = (MarcExportRunner)context.getBean("marcExportRunner");
		runner.doExportFull();
		System.out.println("Press any key to end!");
		System.in.read();
	}

}
