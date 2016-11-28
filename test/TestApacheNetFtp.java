package test;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

import com.proquest.apac.summontool.ftp.FtpHelper;

public class TestApacheNetFtp {

	public static void main(String[] args) throws SocketException, IOException {
		//test2();
		testMyFtpHelper();
	}
	
	public static void testMyFtpHelper(){
		FtpHelper ftp = new FtpHelper();
		try {
			ftp.connect("ftp.summon.serialssolutions.com", 21, "wavetest-catalogus", "IbHE74uEkb");
			ftp.uploadFilesInDir("./exports/deletes", "/deletes/");
			ftp.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    public static void testDirect() throws SocketException, IOException {
		FTPClient client = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		
		client.connect("ftp.summon.serialssolutions.com");
		client.login("wavetest-catalogus", "IbHE74uEkb");
		System.out.println("connect to using apache...ftp.summon.****.com");
		client.setFileType(FTPClient.BINARY_FILE_TYPE);
		client.storeFile("/updates/test_ftp_upload.txt",new FileInputStream("c:/cqu_marc_recompile.log.txt"));
		
		client.disconnect();
		System.out.println("upload complete");
    }
}