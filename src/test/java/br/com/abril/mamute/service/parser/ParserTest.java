package br.com.abril.mamute.service.parser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ParserTest {
	private PrimitivoParser parser;
	
	@Mock
	private Parser mockParser;
	
	
	@Before
	public void setup() {
		parser = new PrimitivoParser();
	}

	@Test
	public void testEmptyParserDeliversSameText() {
		String texto = "teste";
		assertEquals(texto, parser.parse(texto));
	}

	@Test
	public void testParseShouldCallChildren() throws Exception {		
		parser.addPrimitivoParser(mockParser);
		
		parser.parse("texto");
		
		Mockito.verify(mockParser).parse(anyString());
	}
}
