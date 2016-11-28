package test;

import org.apache.commons.lang3.StringUtils;

import com.proquest.apac.summontool.FtpConf;
import com.proquest.apac.summontool.MarcExportRunner;
import com.proquest.apac.summontool.impl.nop.NopMarcDaoImpl;

public class FileTest {

	public static void main(String[] args) {
        MarcExportRunner job = new MarcExportRunner();
        job.setFtpConf(new FtpConf());
        job.setMarcDao(new NopMarcDaoImpl());
        job.runExport();
	}

}
