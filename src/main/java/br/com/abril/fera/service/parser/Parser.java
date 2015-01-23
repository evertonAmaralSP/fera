package br.com.abril.fera.service.parser;

import br.com.abril.fera.support.json.JsonUtil;

import com.google.gson.JsonObject;

public interface Parser {
	public String parse(String corpo, JsonObject entity);
	public void setJsonUtil(JsonUtil jsonUtil);
}
