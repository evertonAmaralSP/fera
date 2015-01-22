package br.com.abril.mamute.config;

import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import br.com.abril.mamute.config.settings.ConnectionSettings;
import br.com.abril.mamute.config.settings.MongoConfig;
import br.com.abril.mamute.dao.ExportDAO;
import br.com.abril.mamute.dao.ExportDAOImpl;
import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.ProductDAOImpl;
import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.dao.SourceDAOImpl;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.dao.TemplateDAOImpl;
import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.dao.TemplateTypeDAOImpl;
import br.com.abril.mamute.dao.UploadDAO;
import br.com.abril.mamute.dao.UploadDAOImpl;
import br.com.abril.mamute.model.Export;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.model.TemplateType;
import br.com.abril.mamute.model.Upload;

import com.google.gson.Gson;

@Configuration
@ComponentScan("br.com.abril.mamute")
@Import({ MongoConfig.class })
@EnableTransactionManagement
@EnableScheduling
@PropertySource("classpath:/mamute.properties")
public class ApplicationContextConfig extends WebMvcConfigurationSupport {

	private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
	private static final String VIEWS = "/WEB-INF/views/";

	private static final String RESOURCES_HANDLER = "/resources/";
	private static final String RESOURCES_LOCATION = RESOURCES_HANDLER + "**";

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getDemoInterceptor());
	}

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_SOURCE);
		messageSource.setCacheSeconds(5);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setFallbackToSystemLocale(true);
		return messageSource;
	}

	@Bean
	public TemplateResolver templateResolver() {
		TemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix(VIEWS);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCacheable(false);
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10000000);
		return multipartResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}


	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}

	@Controller
	static class FaviconController {
		@RequestMapping("favicon.ico")
		String favicon() {
			return "forward:/resources/images/favicon.ico";
		}
	}

	@Autowired
	ConnectionSettings conn;

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(conn.getDriverClassName());
		dataSource.setUrl(conn.getUrl());
		dataSource.setUsername(conn.getUsername());
		dataSource.setPassword(conn.getPassword());

		return dataSource;
	}
	
	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		return properties;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.addAnnotatedClasses(Product.class);
		sessionBuilder.addAnnotatedClasses(TemplateType.class);
		sessionBuilder.addAnnotatedClasses(Template.class);
		sessionBuilder.addAnnotatedClasses(Source.class);
		sessionBuilder.addAnnotatedClasses(Upload.class);
		sessionBuilder.addAnnotatedClasses(Export.class);
		return sessionBuilder.buildSessionFactory();
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

		return transactionManager;
	}

	@Autowired
	@Primary
	@Bean(name = "productDao")
	public ProductDAO getProductDao(SessionFactory sessionFactory) {
		return new ProductDAOImpl(sessionFactory);
	}

	@Autowired
	@Primary
	@Bean(name = "templateTypeDao")
	public TemplateTypeDAO getTemplateTypeDao(SessionFactory sessionFactory) {
		return new TemplateTypeDAOImpl(sessionFactory);
	}

	@Autowired
	@Primary
	@Bean(name = "templateDao")
	public TemplateDAO getTemplateDao(SessionFactory sessionFactory) {
		return new TemplateDAOImpl(sessionFactory);
	}

	@Autowired
	@Primary
	@Bean(name = "uploadDao")
	public UploadDAO getUploadDao(SessionFactory sessionFactory) {
		return new UploadDAOImpl(sessionFactory);
	}

	@Autowired
	@Primary
	@Bean(name = "sourceDao")
	public SourceDAO getSourceDao(SessionFactory sessionFactory) {
		return new SourceDAOImpl(sessionFactory);
	}
	
	@Autowired
	@Primary
	@Bean(name = "exportDao")
	public ExportDAO getExportDao(SessionFactory sessionFactory) {
		return new ExportDAOImpl(sessionFactory);
	}
	
	@Autowired
	@Bean(name = "gson")
	public Gson getGson() {
		return new Gson();
	}
	
	@Autowired
	@Bean(name = "demoInterceptor")
	public TemporarioInterceptor getDemoInterceptor() {
		return new TemporarioInterceptor();
	}
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
