package br.com.abril.mamute.service.parser;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ParserTest {
	private PrimitivoParser parser;
	
	@Mock
	private Parser mockParser;
	
	
	@Before
	public void setup() {
		parser = new PrimitivoParser();
	}

	@Test
	public void testEmptyParserDeliversSameText() {
		String texto = "teste";
		assertEquals(texto, parser.parse(texto));
	}

	@Test
	public void testParseShouldCallChildren() throws Exception {		
		parser.addPrimitivoParser(mockParser);
		
		parser.parse("texto");
		
		Mockito.verify(mockParser).parse(anyString());
	}
	
	@Test
	public void testPrimitivoConteudoImagem() throws Exception {
		parser.addPrimitivoParser(new ConteudoParser());
		String textoParseado = parser.parse("<p><conteudo slug=\"54a03fee9678a06e3000f051\" titulo=\"Balsa que se incendiou no canal de Otrante, entre a Grécia e Itália\" type=\"application/json\" href=\"http://stage.midia.api.abril.com.br/imagens/54a03fee9678a06e3000f051\" tipo_recurso=\"imagem\" id=\"http://stage.midia.api.abril.com.br/imagens/54a03fee9678a06e3000f051\" /></p>");
		
		assertContains(textoParseado, "<imagem titulo=\"Balsa que se incendiou no canal de Otrante, entre a Grécia e Itália\" href=\"http://stage.midia.api.abril.com.br/imagens/54a03fee9678a06e3000f051\" id=\"http://stage.midia.api.abril.com.br/imagens/54a03fee9678a06e3000f051\" slug=\"54a03fee9678a06e3000f051\" type=\"application/json\">");
		assertNotContains(textoParseado, "tipo_recurso=\"imagem\"");
	}
	
	@Test
	public void testPrimitivoConteudoMapa() throws Exception {
		parser.addPrimitivoParser(new ConteudoParser());
		String textoParseado = parser.parse("<p>Quando queremos podemos</p></p><p>Agora suportamos soud cloud</p><p><conteudo href=\"https://soundcloud.com/davidguetta/sets/david-guetta-listen-deluxe-edition-previews\" id=\"\" slug=\"\" tipo_recurso=\"sound_cloud\" titulo=\"David Gueta\" type=\"application/json\" /></p></p><p>E tambem suportamos gmaps</p><p><conteudo href=\"//www.google.com/maps/search/(-23.6269015, -46.6901785)\" id=\"(-23.6269015, -46.6901785)\" slug=\"\" tipo_recurso=\"mapa\" titulo=\"mapa\" type=\"application/json\" /></p>\");}");
		
		assertContains(textoParseado, "<mapa titulo=\"mapa\" href=\"//www.google.com/maps/search/\\(-23.6269015, -46.6901785\\)\" id=\"\\(-23.6269015, -46.6901785\\)\" slug=\"\" type=\"application/json\">");
		assertNotContains(textoParseado, "tipo_recurso=\"mapa\"");
	}
	
	@Test
	public void testConteudoSoundCloud() throws Exception {
		parser.addPrimitivoParser(new ConteudoParser());
		String textoParseado = parser.parse("<p>Quando queremos podemos</p><p>Agora suportamos soud cloud</p><p><conteudo href=\"https://soundcloud.com/davidguetta/sets/david-guetta-listen-deluxe-edition-previews\" id=\"\" slug=\"\" tipo_recurso=\"sound_cloud\" titulo=\"David Gueta\" type=\"application/json\" /></p></p><p>E tambem suportamos gmaps</p><p><conteudo href=\"//www.google.com/maps/search/(-23.6269015, -46.6901785)\" id=\"(-23.6269015, -46.6901785)\" slug=\"\" tipo_recurso=\"mapa\" titulo=\"mapa\" type=\"application/json\" /></p>\");}");
		
		assertContains(textoParseado, "<p>Quando queremos podemos</p>");
		assertContains(textoParseado, "<sound_cloud titulo=\"David Gueta\" href=\"https://soundcloud.com/davidguetta/sets/david-guetta-listen-deluxe-edition-previews\" id=\"\" slug=\"\" type=\"application/json\">");
		assertNotContains(textoParseado, "tipo_recurso=\"sound_cloud\"");
		assertNotContains(textoParseado, "<html");
	}
	
	@Test
	public void testSecaoNomeada() throws Exception {
		parser.addPrimitivoParser(new SecaoNomeada());
		String textoParseado = parser.parse("<p>Quando queremos podemos</p><secao classe=\"Nova Seção\"><p>Quando queremos podemos</p></secao>");
		
		assertContains(textoParseado, "<div class=\"Nova Seção\">\\n*\\s*<p>Quando queremos podemos</p>\\n*\\s*</div>");
		assertNotContains(textoParseado, "<secao classe=\"Nova Seção\">");
	}
	
	@Test
	public void testNovaPaginaDeveriaPaginar() throws Exception {
		parser.addPrimitivoParser(new NovaPagina());
		String textoParseado = parser.parse("<p>Quando queremos podemos</p><nova-pagina/><secao classe=\"Nova Seção\"><p>Quando queremos podemos</p></secao>");
		
		assertContains(textoParseado, "<novapagina>");
		assertNotContains(textoParseado, "<nova-pagina/>");
	}

	private void assertContains(String texto, String regexp) {
		assertTrue("Era esperado encontrar \"" + regexp + "\", mas não foi encontrado", contains(texto, regexp));
	}
	
	private void assertNotContains(String texto, String regexp) {
		assertFalse("Não era esperado encontrar \"" + regexp + "\", mas foi", contains(texto, regexp));
	}

	private boolean contains(String texto, String regexp) {
		return Pattern.compile(regexp).matcher(texto).find();
	}
}

