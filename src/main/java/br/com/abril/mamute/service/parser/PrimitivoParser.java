package br.com.abril.mamute.service.parser;

import java.util.ArrayList;
import java.util.List;

public class PrimitivoParser {
	public static final Parser NULL_PARSER = new Parser() {
		@Override
		public String parse(String texto) {
			return texto;
		}
	};
	
	private List<Parser> parsers = new ArrayList<>();

	public String parse(String texto) {
		for (Parser parser : parsers) {
			texto = parser.parse(texto);
		}
		
		return texto;
	}

	public void addPrimitivoParser(Parser parser) {
		parsers.add(parser);
	}
}
