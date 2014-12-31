package br.com.abril.mamute.service.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.mamute.support.json.JsonUtil;

import com.google.gson.JsonObject;

public abstract class BaseParser implements Parser {
	@Autowired
	private JsonUtil jsonUtil;
	
	public void setJsonUtil(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}
	
	private ThreadLocal<String> tagBody = new ThreadLocal<>();
	
	@Override
	public String parse(String corpo, JsonObject entity) {
		Document document = Jsoup.parseBodyFragment(corpo);
		
		substituteOcurrencesOfPrimitivo(entity, document.select(doGetCssSelector()));
		
		return document.body().html();
	}

	private void substituteOcurrencesOfPrimitivo(JsonObject entity, Elements elements) {
		for (Element element : elements) {
			storeTheTagBody(element);
			
			replaceElement(entity, element);
		}
	}

	private void replaceElement(JsonObject entity, Element element) {
		element.after(doGetHtml(retrieveAttributesAndValues(element), entity));
		element.remove();
	}

	private void storeTheTagBody(Element element) {
		tagBody.set(element.html());
	}

	protected Map<String, String> retrieveAttributesAndValues(Element element) {
		String[] attributesNames = doGetAttributesNames();
		
		if(attributesNames == null || attributesNames.length == 0)
			return Collections.emptyMap();
		
		Map<String, String> attrValues = new HashMap<>();
		for(String attrName : attributesNames)
			attrValues.put(attrName, element.attr(attrName));
		
		return attrValues;
	}

	protected abstract String doGetHtml(Map<String, String> attributesAndValues, JsonObject entity);
	protected abstract String[] doGetAttributesNames();
	protected abstract String doGetCssSelector();

	protected String getBody() {
		return tagBody.get();
	}
}
