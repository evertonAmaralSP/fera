package br.com.abril.mamute.service.edtorial;

import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class EdtorialUrlsTest {
	

	@InjectMocks
	private EdtorialUrls editorialUrls;
	
	@Test
	public void testFilterOrderPrimeiroParametro() throws URISyntaxException {
		String url = "http://www.mamute.com.br";
		String resultado = editorialUrls.filterOrder(url, editorialUrls.DATA_DISPONIBILIZACAO);
		final String expected = url+"?order="+editorialUrls.DATA_DISPONIBILIZACAO;
		assertTrue(expected.equals(resultado));
	}
	@Test
	public void testFilterOrderSegundoParametro() throws URISyntaxException {
		String url = "http://www.mamute.com.br?marca=mamute";
		String resultado = editorialUrls.filterOrder(url, editorialUrls.DATA_DISPONIBILIZACAO);
		final String expected = url+"&order="+editorialUrls.DATA_DISPONIBILIZACAO;
		assertTrue(expected.equals(resultado));
	}

	@Test
	public void testFilterParametro() throws URISyntaxException {
		String url = "http://www.mamute.com.br";
		String resultado = editorialUrls.filterParam(url, editorialUrls.DATA_DISPONIBILIZACAO,"04/12/2014");
		final String expected = url+"?"+editorialUrls.DATA_DISPONIBILIZACAO+"="+"04%2F12%2F2014";
		assertTrue(expected.equals(resultado));
	}

	@Test
	public void testFilterParamEstruturado() throws URISyntaxException {
		String url = "http://www.mamute.com.br/id";
		String resultado = editorialUrls.paramEstruturado(url, "BLABLA");
		final String expected = url+"/BLABLA";
		assertTrue(expected.equals(resultado));
	}
	
	@Test
	public void testFilterParametro2() throws URISyntaxException {
		String url = "http://www.mamute.com.br/busca";
		String query = "data_disponibilizacao_inicio=02%2F01%2F2014&marca=viajeaqui&order=data_disponibilizacao&per_page=500";
		String resultado = editorialUrls.paramQuery(url,query);
		resultado = editorialUrls.filterParam(resultado, "pw",4+"");
		final String expected = url + "?" + query + "&pw=4";
		assertTrue(expected.equals(resultado));
	}

}
