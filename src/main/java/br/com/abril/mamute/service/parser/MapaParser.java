package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;

public class MapaParser implements Parser {
	private Matcher matcher;

	public MapaParser(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public String parse(String texto) {
		return null;
	}
}
