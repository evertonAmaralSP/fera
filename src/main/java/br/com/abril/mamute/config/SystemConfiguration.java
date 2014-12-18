package br.com.abril.mamute.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class SystemConfiguration {

	private Properties properties;

	private static Logger logger = LoggerFactory.getLogger(SystemConfiguration.class);

	private static final String MAMUTE_PROPERTIES_FILE_NAME = "mamute.properties";

	public static final String FILE_EXTENSION = "file.extension";
	public static final String DIR_TMP = "dir.tmp";

	public static final String HTTP_CLIENT_CONFIG_TIMEOUT = "http.client.config.timeout";
	public static final String EDITORIAL_BASE_URI = "editorial.base.uri";
	public static final String EDITORIAL_NUMERO_ITEM_POR_PAGINA = "editorial.numero.item.por.pagina";
	
	public static final String DATASOURCE_DRIVE = "dataSource.driver";
	public static final String DATASOURCE_URL = "dataSource.url";
	public static final String DATASOURCE_USERNAME = "dataSource.username";
	public static final String DATASOURCE_PASSWORD = "dataSource.password";

	private static SystemConfiguration instance;

	public static SystemConfiguration getInstance() {
		if (null == instance) {
			instance = new SystemConfiguration();
		}

		return instance;
	}

	private SystemConfiguration() {
		if (this.properties == null) {
			String propertyFilename = MAMUTE_PROPERTIES_FILE_NAME;
			try {
				loadProperties(propertyFilename);
				logger.info("Arquivo de propriedades {} carregado com sucesso.", new Object[] { propertyFilename });
			} catch (IOException e) {
				logger.error("Erro ao carregar o arquivo de propriedades do sistema {}", new Object[] { propertyFilename }, e);
			}
		}
	}

	private synchronized void loadProperties(String propertiesFilename) throws IOException {
		Resource resource = new FileSystemResource(propertiesFilename);
		if (!resource.exists()) {
			resource = new ClassPathResource(propertiesFilename);
		}
		setProperties(PropertiesLoaderUtils.loadProperties(resource));
	}

	public static String getPropertyAsString(String propertyName) {
		return getInstance().getProperties().getProperty(propertyName);
	}

	public static Long getPropertyAsLong(String propertyName) {
		String propertyAsString = getPropertyAsString(propertyName);
		if (null == propertyAsString) {
			logger.warn("O valor de {} nao foi definido no arquivo ", new Object[] { propertyName });
			return 0L;
		}
		return Long.parseLong(propertyAsString);
	}

	public static Boolean getPropertyAsBoolean(String propertyName) {
		String propertyAsString = getPropertyAsString(propertyName);
		if (null == propertyAsString) {
			logger.warn("O valor de {} nao foi definido no arquivo ", new Object[] { propertyName });
			return false;
		}
		return Boolean.parseBoolean(propertyAsString);
	}

	public static Integer getPropertyAsInteger(String propertyName) {
		String propertyAsString = getPropertyAsString(propertyName);
		if (null == propertyAsString) {
			logger.warn("O valor de {} nao foi definido no arquivo ", new Object[] { propertyName });
			return 0;
		}
		return Integer.parseInt(propertyAsString);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
