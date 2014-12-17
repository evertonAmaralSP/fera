package br.com.abril.mamute.support.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

@Component
public class JsonUtil {

	public JsonObject fromString(String jsonString) {
		JsonElement jsonElement = this.getJsonHandler().fromJson(jsonString, JsonElement.class);
		return jsonElement.getAsJsonObject();
	}

	public Gson getJsonHandler() {
		final GsonBuilder registerTypeAdapter = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerizlier());
		registerTypeAdapter.setDateFormat("dd/MM/yyyy HH:mm:ss");
		return registerTypeAdapter.create();
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
	private static final String[] DATE_FORMATS = new String[] {
		  "dd/MM/yyyy HH:mm:ss",
		  "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
		  "yyyy-MM-dd'T'HH:mm:ss'Z'"
	};
	
	private class DateSerizlier implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) throws JsonParseException {
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format).parse(jsonElement.getAsString());
            } catch (ParseException e) {
            }
        }
        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString() + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}

}
