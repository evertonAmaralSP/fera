package br.com.abril.mamute.service.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import br.com.abril.mamute.support.json.JsonUtil;

import com.google.gson.JsonObject;

@RunWith(PowerMockRunner.class)
public class ParserTest {
	private static final String SOUND_CLOUD_TEXT = "<conteudo href=\"https://soundcloud.com/davidguetta/sets/david-guetta-listen-deluxe-edition-previews\" id=\"\" slug=\"\" tipo_recurso=\"sound_cloud\" titulo=\"David Gueta\" type=\"application/json\" />";
	private static final String SOUND_CLOUD_EXPECTED = "<sound_cloud titulo=\"David Gueta\" href=\"https://soundcloud.com/davidguetta/sets/david-guetta-listen-deluxe-edition-previews\" id=\"\" slug=\"\" type=\"application/json\">(</sound_cloud>)?";
	
	private PrimitivoParser parser;
	
	@Mock
	private Parser mockParser;

	@Before
	public void setup() {
		parser = new PrimitivoParser();
		parser.setJsonUtil(new JsonUtil());
	}

	@Test
	public void testEmptyParserDeliversSameText() {
		String texto = "teste";
		assertEquals(texto, parser.parse(texto));
	}

	@Test
	public void testParseShouldCallChildren() throws Exception {		
		parser.addParser(mockParser);
		
		parser.parse("texto");
		
		Mockito.verify(mockParser).parse(anyString(), Mockito.any(JsonObject.class));
	}
	
	@Test
	public void testPrimitivoConteudoImagem() throws Exception {
		parser.addParser(new ConteudoParser());
		String entidade = wrapTextIntoJson("<p> A ideia é ver como o Ubber renderiza cada primitivo:</p> <p> 1: Imagem</p> <p> &nbsp;<conteudo href=\"http://stage.midia.api.abril.com.br/imagens/54a2818a9678a00b16000b36\" id=\"http://stage.midia.api.abril.com.br/imagens/54a2818a9678a00b16000b36\" slug=\"54a2818a9678a00b16000b36\" tipo_recurso=\"imagem\" titulo=\"Leandro Damião, de 25 anos, marcou apenas 11 gols pelo Santos em 2014\" type=\"application/json\" /></p>");
		String textoParseado = parser.parse(entidade);
		
		String expected = "<figure>\\n*\\s*<img src=\"http://stage.msalx.veja.abril.com.br/2014/12/30/0842/stSsd/alx_454802358_original.jpeg?1419936109\" alt=\"Leandro Damião, de 25 anos, marcou apenas 11 gols pelo Santos em 2014\" title=\"Leandro Damião, de 25 anos, marcou apenas 11 gols pelo Santos em 2014\">\\n*\\s*<figcaption>\\n*\\s*Leandro Damião, de 25 anos, marcou apenas 11 gols pelo Santos em 2014 | Crédito: Alexandre Schneider\\n*\\s*</figcaption>\\n*\\s*</figure>";
		
		assertContains(textoParseado, expected);
		assertNotContains(textoParseado, "tipo_recurso=\"imagem\"");
	}
	
	@Test
	public void testPrimitivoConteudoMapa() throws Exception {
		parser.addParser(new ConteudoParser());
		String entidade = wrapTextIntoJson("<p>Quando queremos podemos</p></p><p>Agora suportamos soud cloud</p><p><conteudo href=\"https://soundcloud.com/davidguetta/sets/david-guetta-listen-deluxe-edition-previews\" id=\"\" slug=\"\" tipo_recurso=\"sound_cloud\" titulo=\"David Gueta\" type=\"application/json\" /></p></p><p>E tambem suportamos gmaps</p><p><conteudo href=\"//www.google.com/maps/search/(-23.6269015, -46.6901785)\" id=\"(-23.6269015, -46.6901785)\" slug=\"\" tipo_recurso=\"mapa\" titulo=\"mapa\" type=\"application/json\" /></p>\");}");
		String textoParseado = parser.parse(entidade);
		
		assertContains(textoParseado, "<mapa titulo=\"mapa\" href=\"//www.google.com/maps/search/\\(-23.6269015, -46.6901785\\)\" id=\"\\(-23.6269015, -46.6901785\\)\" slug=\"\" type=\"application/json\">");
		assertNotContains(textoParseado, "tipo_recurso=\"mapa\"");
	}
	
	@Test
	public void testConteudoSoundCloud() throws Exception {
		parser.addParser(new ConteudoParser());
		String entidade = wrapTextIntoJson(String.format("<p>Quando queremos podemos</p><p>Agora suportamos soud cloud</p><p>%s</p></p><p>E tambem suportamos gmaps</p><p><conteudo href=\"//www.google.com/maps/search/(-23.6269015, -46.6901785)\" id=\"(-23.6269015, -46.6901785)\" slug=\"\" tipo_recurso=\"mapa\" titulo=\"mapa\" type=\"application/json\" /></p>\");}", SOUND_CLOUD_TEXT));
		String textoParseado = parser.parse(entidade);
		
		assertContains(textoParseado, "<p>Quando queremos podemos</p>");
		assertContains(textoParseado, SOUND_CLOUD_EXPECTED);
		assertNotContains(textoParseado, "tipo_recurso=\"sound_cloud\"");
		assertNotContains(textoParseado, "<html");
	}
	
	@Test
	public void testSecaoNomeada() throws Exception {
		parser.addParser(new SecaoNomeadaParser());
		String entidade = wrapTextIntoJson("<p>Quando queremos podemos</p><secao classe=\"Nova Seção\"><p>Quando queremos podemos</p></secao>");
		String textoParseado = parser.parse(entidade);
		
		assertContains(textoParseado, "<div class=\"Nova Seção\">\\n*\\s*<p>Quando queremos podemos</p>\\n*\\s*</div>");
		assertNotContains(textoParseado, "<secao classe=\"Nova Seção\">");
	}
	
	@Test
	public void testNovaPaginaDeveriaPaginar() throws Exception {
		parser.addParser(new NovaPaginaParser());
		String entidade = wrapTextIntoJson("<p>Quando queremos podemos</p><nova-pagina/><secao classe=\"Nova Seção\"><p>Quando queremos podemos</p></secao>");
		String textoParseado = parser.parse(entidade);
		
		assertContains(textoParseado, "<novapagina>");
		assertNotContains(textoParseado, "<nova-pagina/>");
	}
	
	@Test
	public void testSecaoNomeadaComSoundcloudDentro() throws Exception {
		parser.addParser(new SecaoNomeadaParser()).addParser(new ConteudoParser());
		String entidade = wrapTextIntoJson(String.format("<p>Quando queremos podemos</p></p><p>Agora suportamos soud cloud</p><p><secao classe=\"Nova Seção\">%s</secao></p></p><p>E tambem suportamos gmaps</p><p><conteudo href=\"//www.google.com/maps/search/(-23.6269015, -46.6901785)\" id=\"(-23.6269015, -46.6901785)\" slug=\"\" tipo_recurso=\"mapa\" titulo=\"mapa\" type=\"application/json\" /></p>\");}", SOUND_CLOUD_TEXT));
		String textoParseado = parser.parse(entidade);
		
		
		assertContains(textoParseado, String.format("<div class=\"Nova Seção\">\\n*\\s*%s\\n*\\s*</div>", SOUND_CLOUD_EXPECTED));
		assertNotContains(textoParseado, "tipo_recurso=\"mapa\"");
	}
	
	@Test
	public void testGaleriaMultimidia() throws Exception {
		parser.addParser(new ConteudoParser());
		String entidade = wrapTextIntoJson("<p>Eu disse duas.. :(</p><p>&nbsp;<conteudo href=\"http://editorial.api.abril.com.br/galerias_multimidia/549b0f8e6b6c1250cd0000f1\" id=\"http://editorial.api.abril.com.br/galerias_multimidia/549b0f8e6b6c1250cd0000f1\" slug=\"549b0f8e6b6c1250cd0000f1\" tipo_recurso=\"galeria_multimidia\" titulo=\"Fundos legais\" type=\"application/json\" /></p>");
		
		String textoParseado = parser.parse(entidade);
		
		assertContains(textoParseado, "<galeria_multimidia titulo=\"Fundos legais\" href=\"http://editorial.api.abril.com.br/galerias_multimidia/549b0f8e6b6c1250cd0000f1\" id=\"http://editorial.api.abril.com.br/galerias_multimidia/549b0f8e6b6c1250cd0000f1\" slug=\"549b0f8e6b6c1250cd0000f1\" type=\"application/json\">(</galeria_multimidia>)?");
		assertNotContains(textoParseado, "tipo_recurso=\"galeria_multimidia\"");
	}
	
	private String wrapTextIntoJson(String corpo) {
		String conteudosRelacionados = "{preview: \"http://stage.msalx.alexandria.abril.com.br/2014/12/30/0842/9t6ZS/alx_454802358_original.jpeg\", descricao: \"Leandro Damião, de 25 anos, marcou apenas 11 gols pelo Santos em 2014\", credito: \"Alexandre Schneider\", fonte: \"Getty Images\", titulo: \"Leandro Damião, de 25 anos, marcou apenas 11 gols pelo Santos em 2014\", tipo_recurso: \"imagem\", slug:\"54a2818a9678a00b16000b36\", id: \"http://stage.midia.api.abril.com.br/imagens/54a2818a9678a00b16000b36\"}";
		return String.format("{\"corpo\":\"%s\", conteudos_relacionados: [%s]}", corpo.replace("\"", "\\\""), conteudosRelacionados );
	}

	private void assertContains(String texto, String regexp) {
		assertTrue("Era esperado encontrar \"" + regexp + "\", mas não foi encontrado", doesTheTextContains(texto, regexp));
	}
	
	private void assertNotContains(String texto, String regexp) {
		assertFalse("Não era esperado encontrar \"" + regexp + "\", mas foi", doesTheTextContains(texto, regexp));
	}

	private boolean doesTheTextContains(String texto, String regexp) {
		return Pattern.compile(regexp).matcher(texto).find();
	}
}

