package br.com.abril.mamute.service.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.mamute.support.json.JsonUtil;

import com.google.gson.JsonObject;

@Component
public class PrimitivoParser {
	@Autowired
	private JsonUtil jsonUtil;
	
	public static final Parser NULL_PARSER = new Parser() {
		@Override
		public String parse(String corpo, JsonObject entity) {
			return "";
		}

		@Override
		public void setJsonUtil(JsonUtil jsonUtil) {
		}
	};
	
	public void setJsonUtil(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}
	
	private List<Parser> parsers = new ArrayList<>();

	public String parse(String entityAsJsonString) {
		return parse(getJson(entityAsJsonString));
	}

	private JsonObject getJson(String entityAsJsonString) {
		try {
			return jsonUtil.fromString(entityAsJsonString);
		} catch (IllegalStateException e) {
			return jsonUtil.fromString(String.format("{\"corpo\":\"%s\"}", entityAsJsonString));
		}
	}
	
	public String parse(JsonObject entity) {
		String corpo = entity.get("corpo").getAsString();
		
		for (Parser parser : parsers)
			corpo = parser.parse(corpo, entity);
		
		return corpo;
	}

	public PrimitivoParser addParser(Parser parser) {
		parser.setJsonUtil(jsonUtil);
		parsers.add(parser);
		
		return this;
	}
}
