package com.proquest.apac.summontool;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proquest.apac.summontool.ftp.FtpHelper;

public class FtpFileUploader implements FileUploader {

	private final Logger logger = LoggerFactory.getLogger(FtpFileUploader.class);
	private FtpConf ftpConf;
	private FtpHelper ftpclient = new FtpHelper();

	public void setFtpConf(FtpConf ftpConf) {
		this.ftpConf = ftpConf;
	}

	@Override
	public void uploadFiles() {
		
		String writeLockFileName = ftpConf.getFileDir() + ftpConf.getWriteLockFilename();
		File writeLockFile = new File(writeLockFileName);
		if(writeLockFile.exists() && writeLockFile.isFile()){
			logger.info("upload/export mutex file[{}] exist, abort upload", writeLockFileName);
			logger.info("If you are sure that the exported files are correct, you can delete this lock file to enable uploader to continue ");
			return;
		}
		
		try {
			//1.upload CN files 
			logger.debug("uploading CN updates/deletes now");
			boolean connected = ftpclient.connect(ftpConf.getFtpCNAddress(), 21, ftpConf.getFtpCNUsername(), ftpConf.getFtpCNPassword());
			if(!connected) return;
			
			ftpclient.uploadFilesInDir(ftpConf.getFileDir() + "CN/updates/", "/updates/");
			ftpclient.uploadFilesInDir(ftpConf.getFileDir() + "CN/deletes/", "/deletes/");
			ftpclient.disconnect();
			
			//2.uplod US files
			logger.debug("uploading US updates/deletes now");
			connected = ftpclient.connect(ftpConf.getFtpUSAddress(), 21, ftpConf.getFtpUSUsername(), ftpConf.getFtpUSPassword());
			if(!connected) return;
			
			ftpclient.uploadFilesInDir(ftpConf.getFileDir() + "US/updates/", "/updates/");
			ftpclient.uploadFilesInDir(ftpConf.getFileDir() + "US/deletes/", "/deletes/");
			ftpclient.disconnect();			
			
			logger.info("upload all files done! job compeleted!");
			
		} catch (SocketException e) {
			logger.error("uploading socket error:{}", e);
			
		} catch (IOException e) {
			logger.error("uploading IO error:{}", e);
		}

	}
}
