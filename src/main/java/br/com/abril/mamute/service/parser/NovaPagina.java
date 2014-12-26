package br.com.abril.mamute.service.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NovaPagina extends BaseParser {

	private static final Pattern NOVA_PAGINA = Pattern.compile("<\\s*nova-pagina\\s*/>");

	@Override
	protected String doPrepareReplacement(Matcher matcher) {
		return "<novapagina>";
	}

	@Override
	protected Pattern doGetPattern() {
		return NOVA_PAGINA;
	}

}
