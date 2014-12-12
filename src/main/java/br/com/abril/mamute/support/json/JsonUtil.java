package br.com.abril.mamute.support.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class JsonUtil {

	public JsonObject fromString(String jsonString) {
		JsonElement jsonElement = this.getJsonHandler().fromJson(jsonString, JsonElement.class);
		return jsonElement.getAsJsonObject();
	}

	public Gson getJsonHandler() {
		return new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
	}

	public JsonElement getAttribute(JsonObject jsonObject, String attributeKey){
		if(jsonObject == null){
			throw new IllegalArgumentException("jsonObject nao pode ser nulo");
		}

		if(attributeKey == null){
			throw new IllegalArgumentException("attributeKey nao pode ser nulo");
		}

		JsonElement jsonElement = null;

		if(!jsonObject.has(attributeKey)){
			jsonElement = jsonObject.get(attributeKey);
		}

		return jsonElement;
	}

}
