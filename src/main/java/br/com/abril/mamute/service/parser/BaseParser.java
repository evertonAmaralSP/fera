package br.com.abril.mamute.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseParser implements Parser {
	public static final Pattern CHAVE_VALOR = Pattern.compile("\\s([^=\\s]*)\\s*=\\s*\"([^\"]*)\"");
	protected Map<String, Map<String, String>> chavesValoresHolder = new HashMap<>();
	
	@Override
	public String parse(String texto) {
		Matcher matcher = doGetPattern().matcher(texto);
		StringBuffer sb = new StringBuffer(texto.length());
		while(matcher.find())
			matcher.appendReplacement(sb, doPrepareReplacement(matcher));
		
		matcher.appendTail(sb);
		return sb.toString();
	}

	protected abstract String doPrepareReplacement(Matcher matcher);
	protected abstract Pattern doGetPattern();
	
	protected String getValue(String key, String primitivesText) {
		return getKeyValues(primitivesText).get(key);
	}
	
	protected Map<String, String> getKeyValues(String primitivesText) {
		Map<String, String> chavesValores = null;
		
		chavesValores = getChavesValores(primitivesText);
		
		Matcher matcher = CHAVE_VALOR.matcher(primitivesText);
		while(matcher.find())
			chavesValores.put(matcher.group(1), matcher.group(2));
		
		return chavesValores;
	}

	private Map<String, String> getChavesValores(String primitivesText) {
		Map<String, String> chavesValores;
		if((chavesValores = chavesValoresHolder.get(primitivesText)) == null) {
			chavesValores = new HashMap<>();
			chavesValoresHolder.put(primitivesText, chavesValores);
		}
		
		return chavesValores;
	}
}
