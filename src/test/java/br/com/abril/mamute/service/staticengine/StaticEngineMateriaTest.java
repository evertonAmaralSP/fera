package br.com.abril.mamute.service.staticengine;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class StaticEngineMateriaTest {

	@InjectMocks
	private StaticEngineMateria staticEngineMateria;
	
	@Test(expected=IOException.class)
	public void testValidateErrorSintax() throws IOException {
		String modelo = "<html>${materia.doido()</html>";
		staticEngineMateria.validate(modelo);
	}
	
	@Test
	public void testValidate() throws IOException {
		String modelo = "<html>${materia.doido()}</html>";
		staticEngineMateria.validate(modelo); 
	}


}
