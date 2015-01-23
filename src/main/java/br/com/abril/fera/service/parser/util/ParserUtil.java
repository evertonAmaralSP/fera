package br.com.abril.fera.service.parser.util;

import java.util.Map;

import br.com.abril.fera.service.parser.bean.ConteudoRelacionadoOuEmbutido;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ParserUtil {
	public static String getAttributeWithDefault(Map<String, String> attributesAndValues, String chave, String dft) {
		String value = attributesAndValues.get(chave);
		
		return value == null || "".equals(value) ? dft:value;
	}
	
	public static ConteudoRelacionadoOuEmbutido findConteudoRelacionadoById(String id, JsonObject entity) {
		return findConteudo(id, entity.get("conteudos_relacionados"));
	}
	
	public static ConteudoRelacionadoOuEmbutido findConteudoEmbutidoById(String id, JsonObject entity) {
		return findConteudo(id, entity.get("conteudos_relacionados"));
	}

	private static ConteudoRelacionadoOuEmbutido findConteudo(String id, JsonElement element) {
		if(element == null || !element.isJsonArray()) return new ConteudoRelacionadoOuEmbutido();

		JsonArray conteudosRelacionados = element.getAsJsonArray();
		
		return ParserUtil.findConteudoRelacionadoOuEmbutidoById(id, conteudosRelacionados);
	}

	private static ConteudoRelacionadoOuEmbutido findConteudoRelacionadoOuEmbutidoById(String id, JsonArray conteudosRelacionados) {
		for (JsonElement jsonElement : conteudosRelacionados) {
			JsonObject conteudoRelacionadoOuEmbutido = jsonElement.getAsJsonObject();
			JsonElement crID = conteudoRelacionadoOuEmbutido.get("id");
			
			if(crID == null)
				continue;
			
			if(crID.getAsString().equals(id))
				return new ConteudoRelacionadoOuEmbutido(conteudoRelacionadoOuEmbutido);
		}
		
		return new ConteudoRelacionadoOuEmbutido();
	}
}
