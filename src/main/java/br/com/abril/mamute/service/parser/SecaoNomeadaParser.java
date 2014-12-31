package br.com.abril.mamute.service.parser;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class SecaoNomeadaParser extends BaseParser {
	@Override
	protected String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity) {
		return String.format("<div class=\"%s\">%s</div>", attributesAndValues.get("classe"), getBody());
	}

	@Override
	protected String[] doGetAttributesNames() {
		return new String[]{"classe"};
	}

	@Override
	protected String doGetCssSelector() {
		return "secao";
	}
}
