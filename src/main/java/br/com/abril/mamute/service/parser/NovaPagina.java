package br.com.abril.mamute.service.parser;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class NovaPagina extends BaseParser {
	@Override
	protected String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity) {
		return "<novapagina>";
	}

	@Override
	protected String[] doGetAttributesNames() {
		return null;
	}

	@Override
	protected String doGetSelector() {
		return "nova-pagina";
	}
}
