package com.proquest.apac.summontool;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.spi.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proquest.apac.core.marc.Field;
import com.proquest.apac.core.marc.Marc;
import com.proquest.apac.core.marc.MarcChar;
import com.proquest.apac.core.marc.impl.FieldImpl;
import com.proquest.apac.core.marc.impl.MarcImpl;
import com.proquest.apac.core.marc.impl.MarcValidator;

/**
 * Worker class of all operations
 * @author PChen
 *
 */
public class MarcExportRunner {
	private FtpConf ftpConf;
	private MarcDao marcDao;
	private final Logger logger = LoggerFactory.getLogger(MarcExportRunner.class);
	private MarcValidator validator = new MarcValidator();
	
	public void setFtpConf(FtpConf ftpConf) {
		this.ftpConf = ftpConf;
	}
	
	public void setMarcDao(MarcDao marcDao) {
		this.marcDao = marcDao;
	}
	
	/**
	 * periodical running task
	 */
	public void runExport(){
		
		logger.info("generate marc file checkpoint:");
		Date lastExportDate = getLastCheckedDate();
		logger.info("last export date: {}", DateFormatUtils.format(lastExportDate, "yyyy-MM-dd"));
		
		//check if we need an export
		if(DateUtils.truncatedCompareTo(lastExportDate, new Date(), Calendar.DATE) >=0 ){
			logger.info("no need to export.Task abort");
			return;
		}
			
		logger.info("begin a new export:");
		File writeLockFile = new File(ftpConf.getFileDir() + ftpConf.getWriteLockFilename());
		try {
			writeLockFile.createNewFile();
		} catch (IOException e1) {
			logger.error("can not create write.lock file : {}",e1.getMessage());
		}
		
		doExport(lastExportDate);

		try {
			String newLastExportDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
			logger.info("export to file complete successfully, setting last export date to : {} ", newLastExportDate);
			FileUtils.writeStringToFile(new File(ftpConf.getFileDir() + ftpConf.getLastrunFilename()), newLastExportDate);
			FileUtils.deleteQuietly(writeLockFile);
		} catch (IOException e) {
			logger.error("setting last export date error: {}", e.getMessage());
		}
	}

	/**
	 * 导出全Marc数据列表
	 */
	public void doExportFull() {
		
		logger.info("========================================");
		
		String locaMapFile = ftpConf.getFileDir()+ "location_map_"+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".txt";
		logger.info("eport location map to file [{}] ",locaMapFile);
		
		for(Entry<String, String> entry : marcDao.getLocationMap().entrySet()){
			try{
				FileUtils.write(new File(locaMapFile), entry.getKey() + ":" + entry.getValue(),true);
				FileUtils.write(new File(locaMapFile),"\r\n",true);
			}catch(IOException ex){
				logger.error("write location entry error: {}" , ex);
			}
		}
		
		logger.info("========================================");
		logger.info("export Full MARC file begin");
	
		//1.CN full
		
		List<String> marcIdList = marcDao.getCNMarcFullList();
		String filename = ftpConf.getFileDir()+ "fullmarc/" + ftpConf.getSummonId()+"-catalogcn-full-"
						+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".marc";
		logger.info("export CN full to file: {}" , filename);
		writeMarcToFile(filename, marcIdList,ftpConf.getMarcCNFields());
		logger.info("export CN full complete");
		
		//2.US full
		marcIdList = marcDao.getUSMarcFullList();
		filename = ftpConf.getFileDir()+ "fullmarc/" + ftpConf.getSummonId()+"-catalogus-full-"
				+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".marc";
		logger.info("export US full to file: {}" , filename);
		writeMarcToFile(filename, marcIdList,ftpConf.getMarcUSFields());
		logger.info("export US full complete");
		logger.info("export full marc complete!");
		logger.info("========================================");
	}
	
	private void doExport(Date lastExportDate) {
		//1.CN updates
		List<String> marcIdList = marcDao.getUpdatedCNMarcIdList(lastExportDate);
		String filename = ftpConf.getFileDir()+ "CN/updates/" + ftpConf.getSummonId()+"-catalogcn-updates-"
						+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".marc";
		logger.info("export CN udpates to file: {}" , filename);
		writeMarcToFile(filename, marcIdList,ftpConf.getMarcCNFields());
		logger.info("export CN udpates complete");
		logger.info("========================================");
		
		//2.US updates
		marcIdList = marcDao.getUpdatedUSMarcIdList(lastExportDate);
		filename = ftpConf.getFileDir()+ "US/updates/" + ftpConf.getSummonId()+"-catalogus-updates-"
				+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".marc";
		logger.info("export US udpates to file: {}" , filename);
		writeMarcToFile(filename, marcIdList,ftpConf.getMarcUSFields());
		logger.info("export US udpates complete");
		logger.info("========================================");
		
		//3.CN deletes
		marcIdList = marcDao.getDeletedCNMarcIdList(lastExportDate);
		filename = ftpConf.getFileDir()+ "CN/deletes/" + ftpConf.getSummonId()+"-catalogcn-deletes-"
				+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".deletes";
		logger.info("export CN deletes to file: {}" , filename);
		writeMarcIdToFile(filename, marcIdList);
		logger.info("export CN deletes complete");
		logger.info("========================================");
		
		//4.US deletes
		marcIdList = marcDao.getDeletedUSMarcIdList(lastExportDate);
		filename = ftpConf.getFileDir()+ "US/deletes/" + ftpConf.getSummonId()+"-catalogus-deletes-"
				+DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".deletes";
		logger.info("export US deletes to file: {}" , filename);
		writeMarcIdToFile(filename, marcIdList);
		logger.info("export US deletes complete");
		logger.info("========================================");
		
	}
	
	private void writeMarcIdToFile(String filename, List<String> marcIdList) {
		try {
			for(String s : marcIdList){
				FileUtils.write(new File(filename), s,true);
				FileUtils.write(new File(filename),"\r\n",true);
				logger.debug(s);
			}
			logger.info("write marc id count={}",marcIdList.size());
		} catch (IOException e) {
			logger.error("write to file [{}] error [{}]",filename, e.getMessage());
		}		
	}

	private void writeMarcToFile(String filename, List<String> marcIdList,String necessaryFields) {
		try {
			int errorCount = 0;
			int successCount = 0;
			boolean isValid = true;
			String marcField = ftpConf.getMarcLocation().substring(0,3);
			String marcSubField = ftpConf.getMarcLocation().substring(3);
			for(String s : marcIdList){
				try{
					String marc = marcDao.getSingleMarcData(s);
					String marcLoca = marcDao.getItemLocationByMarcId(s);
					Marc m = new MarcImpl(marc);
					
					if(ftpConf.isMarcCheckMarc()){
						isValid = validator.validate(m, necessaryFields);
						if(!isValid){
							logger.error("marc id=[{}] not exported:[{}]",s,validator.getErrorMap() );
							errorCount ++;
							continue;
						}
					}
					
					Field f = new FieldImpl(marcField,"  " + MarcChar.SUBFIELD_DELIMITER + marcSubField + marcLoca);
					logger.debug("add field {}={},full field string={}",ftpConf.getMarcLocation(),marcLoca,f.getName()+f.getData());
					Marc mAdded = m.addField(f);
					FileUtils.write(new File(filename),mAdded.getStrMarc(),"utf-8",true);
					FileUtils.write(new File(filename),"\r\n",true);
					successCount ++;
					if(successCount % 10000 == 0)
						logger.info("exporting count to {} ", successCount);
				}catch(Exception ex){
					errorCount ++;
					logger.error("exporting error:" + ex);
				}
			}
			logger.info("write id total count={},success={},error={} ",marcIdList.size(),
					marcIdList.size()-errorCount ,errorCount);
		} catch (Exception e) {
			logger.error("write to file [{}] error [{}]",filename, e.getMessage());
		}
	}

	private Date getLastCheckedDate(){
		try{
			String lastDate = FileUtils.readFileToString(new File(ftpConf.getFileDir() + ftpConf.getLastrunFilename()));
			return DateUtils.parseDate(lastDate, "yyyy-MM-dd");
		}catch(Exception ex){
			logger.error("parse last checked date error {}, fall back to today",ex.getMessage());
			return new Date();
		}
	}
}
