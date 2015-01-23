package br.com.abril.fera.support.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.fera.support.json.JsonUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RunWith(MockitoJUnitRunner.class)
public class JsonUtilTest {

	@InjectMocks
	private JsonUtil jsonUtil;

	@Test
	public void testGetJsonHandlerDeveRetornarUmaInstanciaDeGson() throws Exception {
		Gson jsonHandler = jsonUtil.getJsonHandler();
		assertNotNull(jsonHandler);
	}

	@Test
	public void testGetJsonHandlerDeveRetornarUmaInstanciaDeGsonQueSabeTratarDatas() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Gson jsonHandler = jsonUtil.getJsonHandler();
		Date parsedDate = jsonHandler.fromJson("\"01/01/1999 22:00:00\"", Date.class);
		String serializedDate = jsonHandler.toJson(parsedDate);

		assertEquals("01/01/1999 22:00:00", sdf.format(parsedDate));
		assertEquals("\"01/01/1999 22:00:00\"", serializedDate);
	}

	@Test
	public void testGetJsonHandlerDeveRetornarUmaInstanciaDeGsonQueSabeTratarDatas2() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		Gson jsonHandler = jsonUtil.getJsonHandler();
		Date parsedDate = jsonHandler.fromJson("\"2001-07-04T12:08:56.235-07:00\"", Date.class);
		String serializedDate = jsonHandler.toJson(parsedDate);

		assertEquals("2001-07-04T16:08:56.235-03:00", sdf.format(parsedDate));
		assertEquals("\"04/07/2001 16:08:56\"", serializedDate);
	}
	

	@Test
	public void testGetJsonHandlerDeveRetornarUmaInstanciaDeGsonQueSabeTratarDatas3() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		Gson jsonHandler = jsonUtil.getJsonHandler();
		Date parsedDate = jsonHandler.fromJson("\"2001-07-04T12:08:56Z\"", Date.class);
		String serializedDate = jsonHandler.toJson(parsedDate);

		assertEquals("2001-07-04T12:08:56Z", sdf.format(parsedDate));
		assertEquals("\"04/07/2001 12:08:56\"", serializedDate);
	}

	
	@Test
	public void testFromStringDeveRetornarUmJsonObjectQuandoRecebeUmaStringValida() {
		JsonObject parsedJson = jsonUtil.fromString("{'data_criacao':'01/01/1999 22:00:00'}");
		assertNotNull(parsedJson);
	}

}
