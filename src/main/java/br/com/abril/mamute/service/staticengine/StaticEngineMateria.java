package br.com.abril.mamute.service.staticengine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.support.factory.FileFactory;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class StaticEngineMateria {
	
	@Autowired
	private FileFactory fileFactory;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Configuration cfg;

	public StaticEngineMateria() {
		this.cfg = new Configuration();
		this.cfg.setClassForTemplateLoading(Materia.class,"materia");
		this.cfg.setObjectWrapper(new DefaultObjectWrapper());
  }

	public void process(String modelo, Map<String, Object> conteudo,String path) {
		try {
			Template template = new Template("materia", new StringReader(modelo), cfg);
			fileFactory.createDiretorio(path);
			String slug = ((Materia)conteudo.get("materia")).getSlug();

			File file = fileFactory.createHtmlFile(path, slug);
			Writer writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF8"));
      template.process(conteudo, writer);
      writer.flush();
      writer.close();
		} catch (TemplateException e) {
			logger.error("[StaticEngine.process] erro ao processar template: {}", new Object[] {e.getMessage() });
		} catch (IOException e) {
			logger.error("[StaticEngine.process] erro ao gravar arquivo na pasta tmp: {}", new Object[] {e.getMessage() });
		}
	}	
	public void validate(String modelo) throws IOException {
    new Template("materia", new StringReader(modelo), cfg);
	}
}
