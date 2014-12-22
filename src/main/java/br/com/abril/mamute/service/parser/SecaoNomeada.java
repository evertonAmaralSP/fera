package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecaoNomeada implements Parser {
	private static final Pattern SECAO_NOMEADA = Pattern.compile("<secao classe=\"([^\"]*)\">");
	private static final Pattern SECAO_NOMEADA_ENDING = Pattern.compile("</\\s*secao\\s*>");

	@Override
	public String parse(String texto) {
		return parseEndings(parseOpenings(texto));
	}

	private String parseOpenings(String texto) {
		Matcher matcher = SECAO_NOMEADA.matcher(texto);
		StringBuffer sb = new StringBuffer(texto.length());
		while(matcher.find())
			matcher.appendReplacement(sb, String.format("<div class=\"%s\">", matcher.group(1)));
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	private String parseEndings(String texto) {
		Matcher matcher = SECAO_NOMEADA_ENDING.matcher(texto);
		StringBuffer sb = new StringBuffer(texto.length());
		while(matcher.find())
			matcher.appendReplacement(sb, "</div>");
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}

}
