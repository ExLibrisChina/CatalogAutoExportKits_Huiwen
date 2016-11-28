package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.proquest.apac.summontool.FileUploader;
import com.proquest.apac.summontool.MarcExportRunner;

public class SpringTest {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring_fullexport.xml");
		MarcExportRunner job = (MarcExportRunner)context.getBean("marcExportRunner");
		//job.runExport();
		job.doExportFull();
		
		//FileUploader upload = (FileUploader)context.getBean("fileUploader");
		//upload.uploadFiles();
	}

}
