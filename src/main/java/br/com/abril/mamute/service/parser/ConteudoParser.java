package br.com.abril.mamute.service.parser;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class ConteudoParser extends BaseParser {
	protected String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity) {
		String tipoRecurso = attributesAndValues.get("tipo_recurso");
		
		return getTemplate(tipoRecurso).render(attributesAndValues, entity);
	}

	private Template getTemplate(String tipoRecurso) {
		try {
			return Template.valueOf(tipoRecurso.toUpperCase());
		} catch (IllegalArgumentException e) {
		}
		
		return Template.UNKOWN;
	}

	@Override
	protected String[] doGetAttributesNames() {
		return new String[] {"tipo_recurso","titulo", "href", "slug", "id", "type"};
	}


	protected String doGetSelector() {
		return "conteudo";
	}
	
	private enum Template {
		IMAGEM {
			@Override
			protected String render(Map<String, String> attributesAndValues, JsonObject entity) {
				String href = attributesAndValues.get("href");
				String titulo = attributesAndValues.get("titulo");
				String id = attributesAndValues.get("id");
				ConteudoRelacionado cr = findConteudoRelacionadoById(id, entity);
				String credito = cr.getCredito();
				
				return String.format("<figure><img src=\"%s\" alt=\"%s\" title=\"%s\"><figcaption>%s | Crédito: %s</figcaption></figure>", href, titulo, titulo, titulo, credito);
			}
		}, MAPA, SOUND_CLOUD, UNKOWN;
		
		protected String render(Map<String, String> attributesAndValues, JsonObject entity) {
			return String.format("<%s titulo=\"%s\" href=\"%s\" id=\"%s\" slug=\"%s\" type=\"%s\">", attributesAndValues.get("tipo_recurso"), attributesAndValues.get("titulo"), attributesAndValues.get("href"), attributesAndValues.get("id"), attributesAndValues.get("slug"), attributesAndValues.get("type"));
		}

		protected ConteudoRelacionado findConteudoRelacionadoById(String id, JsonObject entity) {
			JsonElement cr = entity.get("conteudos_relacionados");
			if(cr == null) return new ConteudoRelacionado();
			
			if(cr.isJsonArray()) {
				JsonArray conteudosRelacionados = cr.getAsJsonArray();
				
				for (JsonElement jsonElement : conteudosRelacionados) {
					JsonObject conteudoRelacionado = jsonElement.getAsJsonObject();
					JsonElement crID = conteudoRelacionado.get("id");
					
					if(crID == null)
						continue;
					
					if(crID.getAsString().equals(id)) {
						return new ConteudoRelacionado(conteudoRelacionado);
					}
				}
			}
			
			return new ConteudoRelacionado();
		}
	}
}
