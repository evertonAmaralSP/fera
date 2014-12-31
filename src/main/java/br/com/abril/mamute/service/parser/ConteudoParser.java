package br.com.abril.mamute.service.parser;

import java.util.Map;

import org.springframework.stereotype.Component;


import br.com.abril.mamute.service.parser.bean.ConteudoRelacionado;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class ConteudoParser extends BaseParser {
	protected String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity) {
		return getTemplate(attributesAndValues).render(attributesAndValues, entity);
	}

	private Template getTemplate(Map<String, String> attributesAndValues) {
		try {
			return Template.valueOf(getAttributeWithDefault(attributesAndValues, "tipo_recurso", "UNKOWN").toUpperCase());
		} catch (IllegalArgumentException e) {
		}
		
		return Template.UNKOWN;
	}

	@Override
	protected String[] doGetAttributesNames() {
		return new String[] {"tipo_recurso","titulo", "href", "slug", "id", "type"};
	}

	@Override
	protected String doGetCssSelector() {
		return "conteudo";
	}

	protected static String getAttributeWithDefault(Map<String, String> attributesAndValues, String chave, String dft) {
		String value = attributesAndValues.get(chave);
		
		return value == null || "".equals(value) ? dft:value;
	}
	
	private enum Template {
		IMAGEM {
			@Override
			protected String render(Map<String, String> attributesAndValues, JsonObject entity) {
				String href = getAttributeWithDefault(attributesAndValues, "href", "");
				String titulo = getAttributeWithDefault(attributesAndValues, "titulo", "");
				String id = getAttributeWithDefault(attributesAndValues, "id", "");
				String credito = getCreditoForImage(entity, id);
				
				return String.format("<figure><img src=\"%s\" alt=\"%s\" title=\"%s\"><figcaption>%s | Crédito: %s</figcaption></figure>", href, titulo, titulo, titulo, credito);
			}

			private String getCreditoForImage(JsonObject entity, String id) {
				ConteudoRelacionado cr = findConteudoRelacionadoById(id, entity);
				String credito = cr.getCredito();
				return credito;
			}
		}, MAPA, SOUND_CLOUD, UNKOWN;
		
		protected String render(Map<String, String> attributesAndValues, JsonObject entity) {
			return String.format("<%s titulo=\"%s\" href=\"%s\" id=\"%s\" slug=\"%s\" type=\"%s\">", attributesAndValues.get("tipo_recurso"), attributesAndValues.get("titulo"), attributesAndValues.get("href"), attributesAndValues.get("id"), attributesAndValues.get("slug"), attributesAndValues.get("type"));
		}

		protected ConteudoRelacionado findConteudoRelacionadoById(String id, JsonObject entity) {
			JsonElement cr = entity.get("conteudos_relacionados");
			if(cr == null || !cr.isJsonArray()) return new ConteudoRelacionado();

			JsonArray conteudosRelacionados = cr.getAsJsonArray();
			
			return findConteudoRelacionadoById(id, conteudosRelacionados);
		}

		private ConteudoRelacionado findConteudoRelacionadoById(String id, JsonArray conteudosRelacionados) {
			for (JsonElement jsonElement : conteudosRelacionados) {
				JsonObject conteudoRelacionado = jsonElement.getAsJsonObject();
				JsonElement crID = conteudoRelacionado.get("id");
				
				if(crID == null)
					continue;
				
				if(crID.getAsString().equals(id))
					return new ConteudoRelacionado(conteudoRelacionado);
			}
			
			return new ConteudoRelacionado();
		}
	}
}
