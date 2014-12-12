package br.com.abril.mamute.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import br.com.abril.mamute.config.SystemConfiguration;
import br.com.abril.mamute.model.Materia;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component("staticEngine")
public class StaticEngine {

	private static final String FILE_EXTENSION = SystemConfiguration.getPropertyAsString(SystemConfiguration.FILE_EXTENSION);
	Configuration cfg;

	public StaticEngine() {
		this.cfg = new Configuration();
		this.cfg.setClassForTemplateLoading(Materia.class,"materia");
		this.cfg.setObjectWrapper(new DefaultObjectWrapper());
  }

	public void process(String modelo, Map<String, Object> conteudo,String path) {
		try {
			Template template = new Template("materia", new StringReader(modelo), cfg);
			validateDiretorio(path);
			String slug = ((Materia)conteudo.get("materia")).getSlug();

			File file = new File(path + "/" + slug + FILE_EXTENSION);
			Writer writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
      template.process(conteudo, writer);
      writer.flush();
      writer.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void validateDiretorio(String path) throws IOException {
	  FileUtils.forceMkdir(new File(path));
  }
}
