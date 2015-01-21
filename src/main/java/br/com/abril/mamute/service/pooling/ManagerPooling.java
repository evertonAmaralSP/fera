package br.com.abril.mamute.service.pooling;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.model.editorial.Materia;
import br.com.abril.mamute.model.editorial.ResultadoBuscaMateria;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.staticengine.StaticEngineMateria;
import br.com.abril.mamute.support.factory.FileFactory;

@Service
public class ManagerPooling {

	@Autowired
	private SourceDAO sourceDAO;
	@Autowired
	private TemplateDAO templateDAO;
	@Autowired
	private Editorial editorial;
	@Autowired
	StaticEngineMateria staticEngine;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private FileFactory fileFactory;

	public void processPoolings(){
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
