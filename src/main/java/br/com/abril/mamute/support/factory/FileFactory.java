package br.com.abril.mamute.support.factory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import br.com.abril.mamute.config.SystemConfiguration;

public class FileFactory {

	private static final String DIR_TMP = SystemConfiguration.getPropertyAsString(SystemConfiguration.DIR_TMP);
	private static final String FILE_EXTENSION = SystemConfiguration.getPropertyAsString(SystemConfiguration.FILE_EXTENSION);
	
	public static String generatePathOfDirectoryTemplate(String pathProduction, String pathTemaplte) {
		if(StringUtils.isEmpty(pathProduction) || StringUtils.isEmpty(pathTemaplte)) throw new IllegalArgumentException();
	  return DIR_TMP + "/" + pathProduction + "/" + pathTemaplte;
  }
	public static void createDiretorio(String path) throws IOException {
	  FileUtils.forceMkdir(new File(path));
  }
	
	public static File createHtmlFile(String path, String slug) {
	  File file = new File(path + "/" + slug + FILE_EXTENSION);
	  return file;
  }
}
