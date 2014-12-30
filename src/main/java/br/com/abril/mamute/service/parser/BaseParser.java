package br.com.abril.mamute.service.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class BaseParser implements Parser {
	private ThreadLocal<String> body = new ThreadLocal<>();
	
	@Override
	public String parse(String texto) {
		Document document = Jsoup.parseBodyFragment(texto);
		Elements elements = document.select(doGetSelector());
		
		for (Element element : elements) {
			body.set(element.html());
			element.after(doGetHtml(retrieveAttributesAndValues(element)));
			element.remove();
		}
		
		return document.body().html();
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

	protected abstract String doGetHtml(Map<String, String> attributesAndValues);
	protected abstract String[] doGetAttributesNames();
	protected abstract String doGetSelector();

	protected String getBody() {
		return body.get();
	}
}
