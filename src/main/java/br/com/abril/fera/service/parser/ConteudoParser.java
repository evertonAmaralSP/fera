package br.com.abril.fera.service.parser;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.abril.fera.service.parser.template.ConteudoTemplate;
import br.com.abril.fera.service.parser.util.ParserUtil;

import com.google.gson.JsonObject;

@Component
public class ConteudoParser extends BaseParser {
	protected String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity) {
		return getTemplate(attributesAndValues).render(attributesAndValues, entity);
	}

	private ConteudoTemplate getTemplate(Map<String, String> attributesAndValues) {
		try {
			return ConteudoTemplate.valueOf(ParserUtil.getAttributeWithDefault(attributesAndValues, "tipo_recurso", "UNKOWN").toUpperCase());
		} catch (IllegalArgumentException e) {
		}
		
		return ConteudoTemplate.UNKOWN;
	}

	@Override
	protected String[] doGetAttributesNames() {
		return new String[] {"tipo_recurso","titulo", "href", "slug", "id", "type"};
	}

	@Override
	protected String doGetCssSelector() {
		return "conteudo";
	}
}
