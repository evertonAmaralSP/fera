package br.com.abril.mamute.support.factory;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import br.com.abril.mamute.config.SystemConfiguration;

@Component
public class HttpClientFactory {

	private static final int HTTP_CLIENT_CONFIG_TIMEOUT = SystemConfiguration.getPropertyAsInteger(SystemConfiguration.HTTP_CLIENT_CONFIG_TIMEOUT);

	public static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

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

}
