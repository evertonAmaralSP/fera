package br.com.abril.fera.service.pooling;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.abril.fera.dao.ExportDAO;
import br.com.abril.fera.dao.SourceDAO;
import br.com.abril.fera.dao.TemplateDAO;
import br.com.abril.fera.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.fera.model.Export;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Source;
import br.com.abril.fera.model.Template;
import br.com.abril.fera.model.editorial.Materia;
import br.com.abril.fera.model.editorial.ResultadoBuscaMateria;
import br.com.abril.fera.service.edtorial.Editorial;
import br.com.abril.fera.service.staticengine.StaticEngineMateria;
import br.com.abril.fera.support.aws.AwsFileFactory;
import br.com.abril.fera.support.factory.FileFactory;

@Service
public class ManagerPooling {

	@Autowired
	private SourceDAO sourceDAO;
	@Autowired
	private ExportDAO exportDao;
	@Autowired
	private TemplateDAO templateDAO;
	@Autowired
	private Editorial editorial;
	@Autowired
	StaticEngineMateria staticEngine;
	@Autowired
	AwsFileFactory awsFileFactory;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private FileFactory fileFactory;

	public void processPoolingExports(){
	List<Export> exports = exportDao.listExportActives();
		for (Export export : exports) {
			try {
		    awsFileFactory.uploadFile(export);
		  } catch (IOException e) {
		  	logger.error("Não foi possivel para a applicação exportar conteudos para ( {} ), favor verifique conexão. ({})", new Object[] { export.getName(), export.getType()});
		  }
	  }
	}

	
	
	public void processPoolingSources(){
		List<Source> list = sourceDAO.listSourceActives();

		for (Source source : list) {
			ResultadoBuscaMateria buscaMateria;
			for (Template template : source.getTemplates()) {
				try {
					
		      buscaMateria = editorial.getListaInSource(source.getSource(),template.getLastUpdateDateUpdatePooling());
		      Materia[] listaMateria = buscaMateria.getResultado();
		      boolean atualizarTemplate = false;
						for (Materia materia : listaMateria) {
							materia = editorial.getMateriaId(materia.getId(),true);
					    if(template.getLastUpdateDateUpdatePooling()==null || template.getLastUpdateDateUpdatePooling().before(materia.getUltimaAtualizacao().getData())) {
					    	
					
					    	String path = getPathTemplate(template);
					    	String modelo = getTemplateDocument(template);
					    	Map<String, Object> conteudo = getConteudo(materia);
					
					    	staticEngine.process(modelo, conteudo, path);
					    	
					    	if (template.getLastUpdateDateUpdatePooling() == null || template.getLastUpdateDateUpdatePooling().before(materia.getUltimaAtualizacao().getData())) {
					    		template.setLastUpdateDateUpdatePooling(materia.getUltimaAtualizacao().getData());
					    		atualizarTemplate=true;
					    	}
					    	
					    }
						}
						if(listaMateria.length > 0 && atualizarTemplate) {
							templateDAO.saveOrUpdate(template);
						}
						
	      } catch (ComunicacaoComEditorialException e) {
		      logger.error("Não foi possivel para a applicação acessar ( {} ), favor verifique conexão.", new Object[] { source.getSource()}); 
	      } catch (URISyntaxException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }  
      }
    }
	}

	private String getTemplateDocument(Template template) {
	  
	  if(template==null || StringUtils.isEmpty(template.getDocument())) {
	  	logger.error("O template para source ( {} ) nao esta carregando ou esta com o parametro document vazio.", new Object[] { template.getName()});
			throw new IllegalArgumentException();

	  }
		return template.getDocument();
  }

	/* TODO: RETIRAR ESSE METODO DAQUI 
	 * MANAGERPOOLING NAO DEVE SABER RECUPERAR PATH
	 */
	private String getPathTemplate(Template template) {
	  Product product = template.getProduct();
	  if(template==null || StringUtils.isEmpty(template.getPath())) {
	  	logger.error("O template para source ( {} ) nao esta carregando ou esta com o parametro path vazio.", new Object[] { template.getName()});
			throw new IllegalArgumentException();
	  }
	  if(product==null || StringUtils.isEmpty(product.getPath())) {
	  	logger.error("O applicacao para source ( {} ) nao esta carregando ou esta com o parametro path vazio.", new Object[] { template.getName()});
	  	throw new IllegalArgumentException();
	  }
	  String path = fileFactory.generatePathOfDirectoryTemplate(product.getPath(),template.getPath());
		return path;
  }

	private Map<String, Object> getConteudo(Object obj) {
	  Map<String, Object> conteudo = new HashMap<String, Object>();
	  conteudo.put("materia", obj);
	  return conteudo;
  }
}
