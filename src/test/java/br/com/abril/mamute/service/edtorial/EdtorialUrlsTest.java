package br.com.abril.mamute.service.edtorial;

import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import org.junit.Test;


public class EdtorialUrlsTest {

	
	@Test
	public void testFilterOrderPrimeiroParametro() throws URISyntaxException {
		String url = "http://www.mamute.com.br";
		String resultado = EdtorialUrls.filterOrder(url, EdtorialUrls.DATA_DISPONIBILIZACAO);
		final String expected = url+"?order="+EdtorialUrls.DATA_DISPONIBILIZACAO;
		System.out.println(expected);
		System.out.println(resultado);
		assertTrue(expected.equals(resultado));
	}
	@Test
	public void testFilterOrderSegundoParametro() throws URISyntaxException {
		String url = "http://www.mamute.com.br?marca=mamute";
		String resultado = EdtorialUrls.filterOrder(url, EdtorialUrls.DATA_DISPONIBILIZACAO);
		final String expected = url+"&order="+EdtorialUrls.DATA_DISPONIBILIZACAO;
		System.out.println(expected);
		System.out.println(resultado);
		assertTrue(expected.equals(resultado));
	}

	@Test
	public void testFilterParametro() throws URISyntaxException {
		String url = "http://www.mamute.com.br";
		String resultado = EdtorialUrls.filterParam(url, EdtorialUrls.DATA_DISPONIBILIZACAO,"04/12/2014");
		final String expected = url+"?"+EdtorialUrls.DATA_DISPONIBILIZACAO+"="+"04%2F12%2F2014";
		System.out.println(expected);
		System.out.println(resultado);
		assertTrue(expected.equals(resultado));
	}

	@Test
	public void testFilterParamEstruturado() throws URISyntaxException {
		String url = "http://www.mamute.com.br/id";
		String resultado = EdtorialUrls.paramEstruturado(url, "BLABLA");
		final String expected = url+"/BLABLA";
		System.out.println(expected);
		System.out.println(resultado);
		assertTrue(expected.equals(resultado));
	}
	
	@Test
	public void testFilterParametro2() throws URISyntaxException {
		String url = "http://www.mamute.com.br/busca";
		String query = "data_disponibilizacao_inicio=02%2F01%2F2014&marca=viajeaqui&order=data_disponibilizacao&per_page=500";
		String resultado = EdtorialUrls.paramQuery(url,query);
		resultado = EdtorialUrls.filterParam(resultado, "pw",4+"");
		final String expected = url + "?" + query + "&pw=4";
		System.out.println(expected);
		System.out.println(resultado);
		assertTrue(expected.equals(resultado));
	}

}
