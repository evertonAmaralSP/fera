package br.com.abril.mamute.support.aws;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@Scope("singleton")
public class AwsConfig {

	private String accessKey;

	private String secretKey;

	public AmazonS3 AmazonS3ClientFactory(String accessKey, String secretKey){
		return new AmazonS3Client(new BasicAWSCredentials(accessKey,secretKey));
	}
	
	
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
