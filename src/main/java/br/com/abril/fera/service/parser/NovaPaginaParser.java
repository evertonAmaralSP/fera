package br.com.abril.fera.service.parser;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class NovaPaginaParser extends BaseParser {
	@Override
	protected String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity) {
		return "<novapagina>";
	}

	@Override
	protected String[] doGetAttributesNames() {
		return null;
	}

	@Override
	protected String doGetCssSelector() {
		return "nova-pagina";
	}
}
