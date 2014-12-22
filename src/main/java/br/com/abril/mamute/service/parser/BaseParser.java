package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseParser implements Parser {
	@Override
	public String parse(String texto) {
		Matcher matcher = doGetPattern().matcher(texto);
		StringBuffer sb = new StringBuffer(texto.length());
		while(matcher.find())
			matcher.appendReplacement(sb, doPrepareReplacement(matcher));
		
		matcher.appendTail(sb);
		return sb.toString();
	}

	protected abstract String doPrepareReplacement(Matcher matcher);
	protected abstract Pattern doGetPattern();
}
