package br.com.abril.mamute.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Test;

public class SystemConfigurationTest {

	@AfterClass
	public static void after (){
		SystemConfiguration.getInstance().setProperties(null);
	}

	@Test
	public void testLongProperty() {
		Properties properties = mock(Properties.class);

		doReturn("999999999999").when(properties).getProperty("Long");

		SystemConfiguration.getInstance().setProperties(properties);

		assertEquals((Long) 999999999999L, SystemConfiguration.getPropertyAsLong("Long"));
	}
	@Test
	public void testMissingLongProperty() {

		assertEquals((Long) 0L, SystemConfiguration.getPropertyAsLong("teste"));
	}

	@Test
	public void testBooleanProperty() {
		Properties properties = mock(Properties.class);

		doReturn("true").when(properties).getProperty("Boolean");

		SystemConfiguration.getInstance().setProperties(properties);

		assertEquals((Boolean) true, SystemConfiguration.getPropertyAsBoolean("Boolean"));
	}

	@Test
	public void testMissingBooleanProperty() {

		assertEquals((Boolean) false, SystemConfiguration.getPropertyAsBoolean("teste"));
	}

	@Test
	public void testIntegerProperty() {
		Properties properties = mock(Properties.class);

		doReturn("10").when(properties).getProperty("Integer");

		SystemConfiguration.getInstance().setProperties(properties);

		assertEquals((Integer) 10, SystemConfiguration.getPropertyAsInteger("Integer"));
	}

	@Test
	public void testMissingIntegerProperty() {

		assertEquals((Integer) 0, SystemConfiguration.getPropertyAsInteger("teste"));
	}

}
