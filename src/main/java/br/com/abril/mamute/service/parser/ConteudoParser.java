package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConteudoParser extends BaseParser {
	private final Pattern CONTEUDO_PATTERN = Pattern.compile("<\\s*conteudo(\\s+href=\"([^\"]*)\")?(\\s+id=\"([^\"]*)\")?(\\s+slug=\"([^\"]*)\")?(\\s+tipo_recurso=\"([^\"]*)\")?(\\s+titulo=\"([^\"]*)\")?(\\s+type=\"[^\"]*\")?\\s*\\/>", Pattern.CASE_INSENSITIVE);

	@Override
	protected String doPrepareReplacement(Matcher matcher) {
		String conteudoPrimitivo = matcher.group();
		
		String type = getValue("tipo_recurso", conteudoPrimitivo);
		String titulo = getValue("titulo", conteudoPrimitivo);
		String href = getValue("href", conteudoPrimitivo);
		
		switch (type) {
		case "sound_cloud":
			return String.format("<soundcloud titulo=\"%s\" href=\"%s\">", titulo, href);
		case "mapa":
			return String.format("<mapa titulo=\"%s\" href=\"%s\">", titulo, href);
		}
		
		return "";
	}
	

	@Override
	protected Pattern doGetPattern() {
		return CONTEUDO_PATTERN;
	}
}
