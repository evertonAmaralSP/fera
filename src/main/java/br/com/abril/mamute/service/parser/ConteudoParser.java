package br.com.abril.mamute.service.parser;

import java.util.Map;


public class ConteudoParser extends BaseParser {
	protected String doGetHtml(Map<String, String> attributesAndValues) {
		return String.format("<%s titulo=\"%s\" href=\"%s\" id=\"%s\" slug=\"%s\" type=\"%s\">", attributesAndValues.get("tipo_recurso"), attributesAndValues.get("titulo"), attributesAndValues.get("href"), attributesAndValues.get("id"), attributesAndValues.get("slug"), attributesAndValues.get("type"));
	}

	@Override
	protected String[] doGetAttributesNames() {
		return new String[] {"tipo_recurso","titulo", "href", "slug", "id", "type"};
	}



	protected String doGetSelector() {
		return "conteudo";
	}
}
