package br.com.abril.mamute.service.parser;

import java.sql.DriverManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConteudoParser implements Parser {
	private final Pattern CONTEUDO_PATTERN = Pattern.compile("/<\\s*conteudo(\\s+href=\"([^\"]*)\")?(\\s+id=\"([^\"]*)\")?(\\s+slug=\"([^\"]*)\")?(\\s+tipo_recurso=\"([^\"]*)\")?(\\s+titulo=\"([^\"]*)\")?(\\s+type=\"[^\"]*\")?\\s*\\/>/", Pattern.CASE_INSENSITIVE);
	private final Pattern TYPE_PATTERN = Pattern.compile("/type=\"([^\"]*)\"/");

	@Override
	public String parse(String texto) {		
		Matcher matcher = CONTEUDO_PATTERN.matcher(texto);
		while(matcher.find()) {
			texto = chooseParser(matcher).parse(texto);
		}
		
		return texto;
	}

	private Parser chooseParser(Matcher matcher) {
		String type = getType(matcher);
		
		switch (type) {
		case "sound_cloud":
			return new SoundCloudParser(matcher);
		case "mapa":
			return new MapaParser(matcher);
		}
		
		return PrimitivoParser.NULL_PARSER;
	}

	private String getType(Matcher primitivoMatcher) {
		String primitivo = primitivoMatcher.group();
		Matcher typeMatcher = TYPE_PATTERN.matcher(primitivo);
		
		if(!typeMatcher.find())
			return null;
		
		return typeMatcher.group(1);
	}
}
