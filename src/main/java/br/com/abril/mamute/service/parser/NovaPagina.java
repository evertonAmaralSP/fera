package br.com.abril.mamute.service.parser;

import java.util.Map;

public class NovaPagina extends BaseParser {

	@Override
	protected String doGetHtml(Map<String, String> attributesAndValues) {
		return "<novapagina>";
	}

	@Override
	protected String[] doGetAttributesNames() {
		return null;
	}

	@Override
	protected String doGetSelector() {
		return "nova-pagina";
	}
//
//	private static final Pattern NOVA_PAGINA = Pattern.compile("<\\s*nova-pagina\\s*/>");
//
//	@Override
//	protected String doPrepareReplacement(Matcher matcher) {
//		return "<novapagina>";
//	}
//
//	@Override
//	protected Pattern doGetPattern() {
//		return NOVA_PAGINA;
//	}

	
}
