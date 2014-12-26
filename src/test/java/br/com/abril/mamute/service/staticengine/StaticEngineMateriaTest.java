package br.com.abril.mamute.service.staticengine;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

import org.junit.Test;

import br.com.abril.mamute.model.Materia;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class StaticEngineMateriaTest {

	@Test
	public void test() {
		String modelo = "<html>${materia.doido()}</html>";
		Template template = null;
    try {
	    template = new Template("materia", new StringReader(modelo), configuracaoBasica());
    } catch (IOException e) {
	    // TODO Auto-generated catch block
    	System.out.println(e.getMessage());
    }
    
		System.out.println(template.getName());
	}

	
	public Configuration configuracaoBasica() {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(Materia.class,"materia");
	  cfg.setObjectWrapper(new DefaultObjectWrapper());
	  return cfg;
  }

}
