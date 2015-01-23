package br.com.abril.fera.service.parser.template;

import java.util.Map;

import br.com.abril.fera.service.parser.bean.ConteudoRelacionadoOuEmbutido;
import br.com.abril.fera.service.parser.util.ParserUtil;

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
			ConteudoRelacionadoOuEmbutido cr = ParserUtil.findConteudoRelacionadoById(id, entity);
			String credito = cr.getCredito();
			return credito;
		}
	}, MAPA, SOUND_CLOUD, TABELA, GALERIA_MULTIMIDIA {

		@Override
		public String render(Map<String, String> attributesAndValues, JsonObject entity) {
//			String href = ParserUtil.getAttributeWithDefault(attributesAndValues, "href", "");
//			String titulo = ParserUtil.getAttributeWithDefault(attributesAndValues, "titulo", "");
//			String id = ParserUtil.getAttributeWithDefault(attributesAndValues, "id", "");
//			
//			ConteudoRelacionado conteudoRelacionado = ParserUtil.findConteudoRelacionadoById(id, entity);
			
			return super.render(attributesAndValues, entity);
		}
	},
	UNKOWN {

		@Override
		public String render(Map<String, String> attributesAndValues, JsonObject entity) {
			return String.format("<!-- Unkown type of primitive: %s-->", attributesAndValues.get("tipo_recurso"));
		}
	};
	
	public String render(Map<String, String> attributesAndValues, JsonObject entity) {
		return String.format("<%s titulo=\"%s\" href=\"%s\" id=\"%s\" slug=\"%s\" type=\"%s\">", attributesAndValues.get("tipo_recurso"), attributesAndValues.get("titulo"), attributesAndValues.get("href"), attributesAndValues.get("id"), attributesAndValues.get("slug"), attributesAndValues.get("type"));
	}


}