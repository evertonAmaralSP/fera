package br.com.provaconceito;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupTest {
	Document doc;
	
	String htmlMaisCodigoAlexandria = "<h1>Lorem ipsum dolor</h1><p> sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris <strong>nisi ut aliquip</strong> ex ea commodo consequat. Duis aute</p><p>irure dolor in</p> reprehenderit in voluptate velit essecillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. "
		+ "<conteudo id=\"http://editorial.api.abril.com.br/materias/viajeaqui\" tipo_recurso=\"materia\" />"
		+ "<conteudo id=\"TESTEMAROTO\" tipo_recurso=\"Validado\" />";
	
	@Test
	public void testSoup01(){
		doc = Jsoup.parse(htmlMaisCodigoAlexandria);
		Elements conteudos = doc.select("conteudo[tipo_recurso~=(?i)]");
		for (Element element : conteudos) {
//	    System.out.println(element.attr("id"));
//	    System.out.println(element.attr("tipo_recurso"));
    }
	}
}
