package com.proquest.apac.summontool;

import org.apache.commons.lang3.StringUtils;

/**
 * @author PChen
 * ftp parameter configuration file mapping class
 */
public class FtpConf {
	private String marcLocation = "999a";
	private String summonId = "wavetest";
	private String fileDir = "./exports";
	private String ftpUSAddress = "ftp.summon.serialssolutions.com";
	private String ftpUSUsername;
	private String ftpUSPassword;
	private String ftpCNAddress = "ftp.summon.serialssolutions.com";
	private String ftpCNUsername;
	private String ftpCNPassword;
	private String writeLockFilename = "write.lock";
	private String lastrunFilename = "last.run";
	private boolean marcCheckMarc = false;
	private String marcCNFields = "";
	private String marcUSFields = "";
	
	public boolean isMarcCheckMarc() {
		return marcCheckMarc;
	}
	public void setMarcCheckMarc(boolean marcCheckMarc) {
		this.marcCheckMarc = marcCheckMarc;
	}
	public String getMarcCNFields() {
		return marcCNFields;
	}
	public void setMarcCNFields(String marcCNFields) {
		if(StringUtils.isNoneBlank(marcCNFields))
			this.marcCNFields = marcCNFields;
	}
	public String getMarcUSFields() {
		return marcUSFields;
	}
	public void setMarcUSFields(String marcUSFields) {
		if(StringUtils.isNoneBlank(marcUSFields))
			this.marcUSFields = marcUSFields;
	}
	public String getWriteLockFilename() {
		return writeLockFilename;
	}
	public void setWriteLockFilename(String writeLockFilename) {
		this.writeLockFilename = writeLockFilename;
	}
	public String getLastrunFilename() {
		return lastrunFilename;
	}
	public void setLastrunFilename(String lastrunFilename) {
		this.lastrunFilename = lastrunFilename;
	}
	public String getMarcLocation() {
		return marcLocation;
	}
	public void setMarcLocation(String marcLocation) {
		this.marcLocation = marcLocation;
	}
	public String getSummonId() {
		return summonId;
	}
	public void setSummonId(String summonId) {
		this.summonId = summonId;
	}
	public String getFileDir() {
		return fileDir;
	}
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
	public String getFtpUSAddress() {
		return ftpUSAddress;
	}
	public void setFtpUSAddress(String ftpUSAddress) {
		this.ftpUSAddress = ftpUSAddress;
	}
	public String getFtpUSUsername() {
		return ftpUSUsername;
	}
	public void setFtpUSUsername(String ftpUSUsername) {
		this.ftpUSUsername = ftpUSUsername;
	}
	public String getFtpUSPassword() {
		return ftpUSPassword;
	}
	public void setFtpUSPassword(String ftpUSPassword) {
		this.ftpUSPassword = ftpUSPassword;
	}
	public String getFtpCNAddress() {
		return ftpCNAddress;
	}
	public void setFtpCNAddress(String ftpCNAddress) {
		this.ftpCNAddress = ftpCNAddress;
	}
	public String getFtpCNUsername() {
		return ftpCNUsername;
	}
	public void setFtpCNUsername(String ftpCNUsername) {
		this.ftpCNUsername = ftpCNUsername;
	}
	public String getFtpCNPassword() {
		return ftpCNPassword;
	}
	public void setFtpCNPassword(String ftpCNPassword) {
		this.ftpCNPassword = ftpCNPassword;
	}
}
