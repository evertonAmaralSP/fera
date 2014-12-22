package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConteudoParser extends BaseParser {
	private final Pattern CONTEUDO_PATTERN = Pattern.compile("<\\s*conteudo(\\s+href=\"([^\"]*)\")?(\\s+id=\"([^\"]*)\")?(\\s+slug=\"([^\"]*)\")?(\\s+tipo_recurso=\"([^\"]*)\")?(\\s+titulo=\"([^\"]*)\")?(\\s+type=\"[^\"]*\")?\\s*\\/>", Pattern.CASE_INSENSITIVE);
	private final Pattern TYPE_PATTERN = Pattern.compile("tipo_recurso=\"([^\"]*)\"");

	@Override
	protected String doPrepareReplacement(Matcher matcher) {
		String type = getType(matcher);
		
		switch (type) {
		case "sound_cloud":
			return "<soundcloud>";
		case "mapa":
			return "<mapa>";
		}
		
		return "";
	}
	

	@Override
	protected Pattern doGetPattern() {
		return CONTEUDO_PATTERN;
	}


	private String getType(Matcher primitivoMatcher) {
		String primitivo = primitivoMatcher.group();
		Matcher typeMatcher = TYPE_PATTERN.matcher(primitivo);
		
		if(!typeMatcher.find())
			return "NONE";
		
		return typeMatcher.group(1);
	}
}
