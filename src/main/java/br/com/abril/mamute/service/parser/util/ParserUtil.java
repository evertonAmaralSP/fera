package br.com.abril.mamute.service.parser.util;

import java.util.Map;

public class ParserUtil {
	public static String getAttributeWithDefault(Map<String, String> attributesAndValues, String chave, String dft) {
		String value = attributesAndValues.get(chave);
		
		return value == null || "".equals(value) ? dft:value;
	}
}
