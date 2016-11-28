package com.proquest.apac.summontool.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PChen
 * ftp wrapper class
 */
public class FtpHelper {
	private final Logger logger = LoggerFactory.getLogger(FtpHelper.class);
	private FTPClient ftpClient = new FTPClient();

    public boolean connect(String hostname,int port,String username,String password) throws IOException{   
        ftpClient.setConnectTimeout(30*1000);
    	ftpClient.connect(hostname, port); 
        //ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out))); 
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){   
            if(ftpClient.login(username, password)){  
            	logger.info("login to [{}] with username[{}] password=[{}] success",hostname,username,password);
                ftpClient.enterLocalPassiveMode();   
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE); 
            	return true;   
            } 
            else{  
            	logger.error("unable to login to ftp server[{}] with username[{}] password=[{}], abort!",hostname,username,password );
    	        disconnect();   
            }
        }
        return false;   
    }   

    /**  
     * 断开与远程服务器的连接  
     * @throws IOException  
     */  
    public void disconnect() throws IOException{   
        if(ftpClient.isConnected()){   
            ftpClient.disconnect();   
        }   
    } 
    
	/**
	 * update files in local dir to remote dir
	 * @param localDir
	 * @param remoteDir
	 * @throws IOException
	 */
	public void uploadFilesInDir(String localDir,String remoteDir) throws IOException{
		if(!ftpClient.isConnected()) 
			throw new IOException("connect to ftp server first");
		try {
			FTPFile[] remoteFiles = ftpClient.listFiles(remoteDir);
			File[] localFiles = new File(localDir).listFiles();
			if(localFiles == null || localFiles.length == 0){
				logger.info("no files in dir[{}] need to be uploaded",localDir);
				return;
			}
			Arrays.sort(localFiles, new Comparator<File>(){
			    public int compare(File f1, File f2){
			        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			    } 
			});
			
			for(File f : localFiles){
				ensureRemoteNotExist(f,remoteDir, remoteFiles);
				uploadOneFile(f,remoteDir);
				FileUtils.deleteQuietly(f);
				logger.debug("delete uploaded file [{}]" , f.getName());
			}
			logger.info("upload local dir=[{}] to remote dir[{}] complete",localDir,remoteDir);
			
			FileUtils.deleteDirectory(new File(localDir));
			logger.info("[{}] directory deleted",localDir);
		} catch (SocketException e) {
			logger.error("socket exception {}", e);
		} catch (IOException e) {
			logger.error("socket exception {}", e);
		}
	}

	public void uploadOneFile(final File f,String remoteDir) throws IOException {
		if(!f.isFile() || !f.canRead()){
			logger.info("File {} cannot be read,give up this one",f.getName());
			return;
		}
		
		String remoteFileName = remoteDir+f.getName();
		FileInputStream fis = new FileInputStream(f);
		ftpClient.storeFile(remoteFileName, fis);
		fis.close();
		logger.info("upload local file[{}] filesize=[{}]bytes,about {}k to remote dir[{}]completed ", f.getName(),f.length(),f.length()/1024,remoteDir);
	}

	public void ensureRemoteNotExist(File f, String remoteDir, FTPFile[] remoteFiles) throws IOException {
		for(FTPFile ftpfile : remoteFiles){
			if(ftpfile.isFile() && ftpfile.getName().equalsIgnoreCase((f.getName()))){
				logger.debug("remote file[{}] exists,delete it first",f.getName());
				ftpClient.deleteFile(remoteDir + ftpfile.getName());
			}
		}
	}
}
