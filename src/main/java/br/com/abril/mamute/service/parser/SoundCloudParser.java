package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;

public class SoundCloudParser implements Parser {
	private Matcher matcher;

	public SoundCloudParser(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public String parse(String texto) {
		return null;
	}
}
