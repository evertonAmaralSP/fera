package br.com.abril.fera.support.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.abril.fera.model.Export;
import br.com.abril.fera.support.aws.AwsConfig;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;


@Component
public class FileVisitorUploadS3 extends SimpleFileVisitor<Path> {
	
	@Autowired
	private AwsConfig awsConfig;
	
	@Value("${dir.tmp}")
	private String dirTmp;
	private String dirTmpProduct;
	
	private AmazonS3 s3;
	private Export export;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) throws IOException {
		InputStream input  = new FileInputStream(path.toFile());
		String key = StringUtils.delete(path.toString(), dirTmpProduct);

		if(validatePastaMaterias(key)){
			logger.debug("###Arquivo exportado: "+key);	
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("text/html");
			s3.putObject(export.getPath(), key, input  , objectMetadata);
			Files.delete(path);
		}
			
		return FileVisitResult.CONTINUE;
	}


	private boolean validatePastaMaterias(String key) {
	  return key.matches("([^\"]*)materias([^\"]*)");
  }


	public void config(Export export) {
		this.export = export;
		s3 = awsConfig.AmazonS3ClientFactory(export.getAccessKey(), export.getSecretKey());
		dirTmpProduct = dirTmp +"/"+export.getProduct().getPath()+"/";
	}
	
	public void setDirTmp(String value) {
		dirTmp = value;
	}

	public Path getPathProduct() {
		return Paths.get(dirTmpProduct);
	}

}
