package br.com.abril.mamute.service.parser;

import br.com.abril.mamute.support.json.JsonUtil;

import com.google.gson.JsonObject;

public interface Parser {
	public String parse(String corpo, JsonObject entity);
	public void setJsonUtil(JsonUtil jsonUtil);
}
