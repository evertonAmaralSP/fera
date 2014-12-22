package br.com.abril.mamute.support.factory;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpClientFactory {

	@Value("${http.client.config.timeout}")
	private static int HTTP_CLIENT_CONFIG_TIMEOUT;

	public static final String APPLICATION_JSON_UTF8 = "product/json; charset=utf-8";

	public CloseableHttpClient createHttpClient() {
		return this.createHttpClient(HTTP_CLIENT_CONFIG_TIMEOUT);
	}


	public CloseableHttpClient createHttpClient(int httpTimeout) {
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(httpTimeout);
		requestBuilder = requestBuilder.setSocketTimeout(httpTimeout);
		requestBuilder = requestBuilder.setConnectionRequestTimeout(httpTimeout);

		return HttpClientBuilder.create().disableContentCompression().setDefaultRequestConfig(requestBuilder.build()).build();
	}


	public static int getHTTP_CLIENT_CONFIG_TIMEOUT() {
		return HTTP_CLIENT_CONFIG_TIMEOUT;
	}


	public static void setHTTP_CLIENT_CONFIG_TIMEOUT(int hTTP_CLIENT_CONFIG_TIMEOUT) {
		HTTP_CLIENT_CONFIG_TIMEOUT = hTTP_CLIENT_CONFIG_TIMEOUT;
	}

}
