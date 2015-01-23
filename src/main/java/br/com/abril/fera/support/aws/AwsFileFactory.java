package br.com.abril.fera.support.aws;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.fera.model.Export;
import br.com.abril.fera.support.factory.FileVisitorUploadS3;

@Component
public class AwsFileFactory {

	@Autowired
	FileVisitorUploadS3 visitorS3;
	
	public void uploadFile(Export export) throws IOException {
		visitorS3.config(export);
		
		Files.walkFileTree(visitorS3.getPathProduct(), visitorS3);
	}

}
