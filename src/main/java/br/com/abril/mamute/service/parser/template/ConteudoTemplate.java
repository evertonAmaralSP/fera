package br.com.abril.mamute.service.parser.template;

import java.util.Map;

import br.com.abril.mamute.service.parser.bean.ConteudoRelacionado;
import br.com.abril.mamute.service.parser.util.ParserUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum ConteudoTemplate {
	IMAGEM {
		@Override
		public String render(Map<String, String> attributesAndValues, JsonObject entity) {
			String href = ParserUtil.getAttributeWithDefault(attributesAndValues, "href", "");
			String titulo = ParserUtil.getAttributeWithDefault(attributesAndValues, "titulo", "");
			String id = ParserUtil.getAttributeWithDefault(attributesAndValues, "id", "");
			String credito = getCreditoForImage(entity, id);
			
			return String.format("<figure><img src=\"%s\" alt=\"%s\" title=\"%s\"><figcaption>%s | Crédito: %s</figcaption></figure>", href, titulo, titulo, titulo, credito);
		}

		private String getCreditoForImage(JsonObject entity, String id) {
			ConteudoRelacionado cr = findConteudoRelacionadoById(id, entity);
			String credito = cr.getCredito();
			return credito;
		}
	}, MAPA, SOUND_CLOUD, GALERIA_MULTIMIDIA, 
	UNKOWN {

		@Override
		public String render(Map<String, String> attributesAndValues, JsonObject entity) {
			return String.format("<!-- Unkown type of primitive: %s-->", attributesAndValues.get("tipo_recurso"));
		}
	};
	
	public String render(Map<String, String> attributesAndValues, JsonObject entity) {
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