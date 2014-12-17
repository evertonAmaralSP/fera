package br.com.abril.mamute.support.tipos;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TipoRecursoEnumTest {

	@Test
	public void testEnum() {
		assertTrue(TipoRecursoEnum.GALERIA_MULTIMIDIA.toString().equals("galeria_multimidia"));
		assertTrue(TipoRecursoEnum.IMAGEM.toString().equals("imagem"));
		assertTrue(TipoRecursoEnum.MATERIA.toString().equals("materia"));
	}

}
