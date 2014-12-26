package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConteudoParser extends BaseParser {
	private final Pattern CONTEUDO_PATTERN = Pattern.compile("<\\s*conteudo(\\s+href=\"([^\"]*)\")?(\\s+id=\"([^\"]*)\")?(\\s+slug=\"([^\"]*)\")?(\\s+tipo_recurso=\"([^\"]*)\")?(\\s+titulo=\"([^\"]*)\")?(\\s+type=\"[^\"]*\")?\\s*\\/>", Pattern.CASE_INSENSITIVE);

	@Override
	protected String doPrepareReplacement(Matcher matcher) {
		String conteudoPrimitivo = matcher.group();
		
		String resourceType = getValue("tipo_recurso", conteudoPrimitivo);
		String titulo = getValue("titulo", conteudoPrimitivo);
		String href = getValue("href", conteudoPrimitivo);
		String slug = getValue("slug", conteudoPrimitivo);
		String id = getValue("id", conteudoPrimitivo);
		String type = getValue("type", conteudoPrimitivo);
		
		switch (resourceType) {
		case "sound_cloud":
			return String.format("<soundcloud titulo=\"%s\" href=\"%s\" id=\"%s\" slug=\"%s\" type=\"%s\">", titulo, href, id, slug, type);
		case "mapa":
			return String.format("<mapa titulo=\"%s\" href=\"%s\" id=\"%s\" slug=\"%s\" type=\"%s\">", titulo, href, id, slug, type);
		}
		
		return "";
	}
	

	@Override
	protected Pattern doGetPattern() {
		return CONTEUDO_PATTERN;
	}
}
