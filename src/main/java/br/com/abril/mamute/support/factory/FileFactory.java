package br.com.abril.mamute.support.factory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Scope(value = "singleton")
public class FileFactory {

	@Value("${dir.tmp}")
	private String DIR_TMP;
	@Value("${file.extension}")
	private String FILE_EXTENSION;

	public String generatePathOfDirectoryTemplate(String pathProduction, String pathTemaplte) {
		if (StringUtils.isEmpty(pathProduction) || StringUtils.isEmpty(pathTemaplte))
			throw new IllegalArgumentException();
		return DIR_TMP + "/" + pathProduction + "/" + pathTemaplte;
	}

	public String generatePathOfDirectoryProduct(String pathProduction) {
		if (StringUtils.isEmpty(pathProduction))
			throw new IllegalArgumentException();
		return DIR_TMP + "/" + pathProduction;
	}

	public void createDiretorio(String path) throws IOException {
		FileUtils.forceMkdir(new File(path));
	}

	public File createHtmlFile(String path, String slug) {
		File file = new File(path + "/" + slug + FILE_EXTENSION);
		return file;
	}

	public void setDIR_TMP(String dIR_TMP) {
		DIR_TMP = dIR_TMP;
	}

	public void setFILE_EXTENSION(String fILE_EXTENSION) {
		FILE_EXTENSION = fILE_EXTENSION;
	}

}
