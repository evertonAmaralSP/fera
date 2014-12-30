package br.com.abril.mamute.service.parser;

import java.util.Map;

public class SecaoNomeada extends BaseParser {

	@Override
	protected String doGetHtml(Map<String, String> attributesAndValues) {
		return String.format("<div class=\"%s\">%s</div>", attributesAndValues.get("classe"), getBody());
	}

	@Override
	protected String[] doGetAttributesNames() {
		return new String[]{"classe"};
	}

	@Override
	protected String doGetSelector() {
		return "secao";
	}
	
	
//	private static final Pattern SECAO_NOMEADA = Pattern.compile("<secao classe=\"([^\"]*)\">");
//	private static final Pattern SECAO_NOMEADA_ENDING = Pattern.compile("</\\s*secao\\s*>");
//
//	@Override
//	public String parse(String texto) {
//		return parseEndings(parseOpenings(texto));
//	}
//
//	private String parseOpenings(String texto) {
//		Matcher matcher = SECAO_NOMEADA.matcher(texto);
//		StringBuffer sb = new StringBuffer(texto.length());
//		while(matcher.find())
//			matcher.appendReplacement(sb, String.format("<div class=\"%s\">", matcher.group(1)));
//		matcher.appendTail(sb);
//		return sb.toString();
//	}
//	
//	private String parseEndings(String texto) {
//		Matcher matcher = SECAO_NOMEADA_ENDING.matcher(texto);
//		StringBuffer sb = new StringBuffer(texto.length());
//		while(matcher.find())
//			matcher.appendReplacement(sb, "</div>");
//		
//		matcher.appendTail(sb);
//		
//		return sb.toString();
//	}

}
