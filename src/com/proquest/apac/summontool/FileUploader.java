package com.proquest.apac.summontool;

/**
 * @author PChen
 * ftp upload by ftp
 */
public interface FileUploader {

	void setFtpConf(FtpConf ftpConf);
	
	/**
	 * iterate a file directory to upload all its files
	 * @param fileDir
	 */
	void uploadFiles();
	
}
