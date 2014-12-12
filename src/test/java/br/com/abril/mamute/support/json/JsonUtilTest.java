package br.com.abril.mamute.support.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

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
	public void testFromStringDeveRetornarUmJsonObjectQuandoRecebeUmaStringValida() {
		JsonObject parsedJson = jsonUtil.fromString("{'data_criacao':'01/01/1999 22:00:00'}");
		assertNotNull(parsedJson);
	}

}
