package com.proquest.apac.summontool;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.proquest.apac.summontool.ftp.FtpHelper;

/**
 * Main controller for automatically update tool
 * @author PChen
 *
 */
public class SummonToolTester {
	
	public static void main(String[] args) throws IOException {
		System.out.println("===========SummonAutoSync config tester=======");
		ApplicationContext context = new ClassPathXmlApplicationContext("spring_base.xml");
		
		//db test
		System.out.println("Database test:");
		MarcDao marcDao = (MarcDao)context.getBean("marcDao");
		try{
			marcDao.testAccessbility();
		}catch(Exception ex){
			System.out.println("test connnection error:==> " + ex.getMessage());
		}
		
		//ftp test
		FtpConf ftpConf = (FtpConf)context.getBean("ftpConf");
		FtpHelper ftpclient = new FtpHelper();
		System.out.println();
		System.out.println("testing ftp CN account now");
		try {
			ftpclient.connect(ftpConf.getFtpCNAddress(), 21, ftpConf.getFtpCNUsername(), ftpConf.getFtpCNPassword());
			ftpclient.disconnect();
		} catch (IOException e) {
			System.out.println("====>ftp connection failed:" + e.getMessage());
		}
		
		System.out.println("testing ftp US account now");
		try {
			ftpclient.connect(ftpConf.getFtpUSAddress(), 21, ftpConf.getFtpUSUsername(), ftpConf.getFtpUSPassword());
			ftpclient.disconnect();
		} catch (IOException e) {
			System.out.println("====>ftp connection failed:" + e.getMessage());
		}
		System.out.println();
		System.out.println("configuration test finished");
		System.out.println();
		System.out.println("press Enter key to close");
		System.in.read();
	}
}
