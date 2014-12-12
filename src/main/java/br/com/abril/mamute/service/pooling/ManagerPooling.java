package br.com.abril.mamute.service.pooling;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.abril.mamute.config.SystemConfiguration;
import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.service.StaticEngine;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.support.log.Log;

@Service
public class ManagerPooling {

	private static final String DIR_TMP = SystemConfiguration.getPropertyAsString(SystemConfiguration.DIR_TMP);

	@Autowired
	private SourceDAO sourceDAO;
	@Autowired
	private Editorial editorial;
	@Autowired
	StaticEngine staticEngine;
	@Log
	private Logger logger;

	public void processPoolings(){
		List<Source> list = sourceDAO.listSourceActives();

		for (Source source : list) {
			ResultadoBuscaMateria buscaMateria = editorial.getListaInSource(source.getSource());
			Date ultimaAtualizacao = source.getLastUpdateDatePooling();
			String materiaId = source.getLastUpdateIdPooling();
			Materia[] listaMateria = buscaMateria.getResultado();
			for (Materia materia : listaMateria) {
	      if(source.getLastUpdateDatePooling()==null || source.getLastUpdateDatePooling().before(materia.getDataDisponibilizacao())) {


	      	String path = getPathTemplateSource(source);
	      	String modelo = getTemplateDocument(source);
	      	Map<String, Object> conteudo = getConteudo(materia);

	      	staticEngine.process(modelo, conteudo, path);

	      	if (ultimaAtualizacao ==null || ultimaAtualizacao.before(materia.getDataDisponibilizacao())) {
	      		ultimaAtualizacao = materia.getDataDisponibilizacao();
	      		materiaId = materia.getId();
	      	}
	      }
	    }
			source.setLastUpdateDatePooling(ultimaAtualizacao);
			source.setLastUpdateIdPooling(materiaId);
			sourceDAO.saveOrUpdate(source);
    }
	}

	private String getTemplateDocument(Source source) {
	  Template template = source.getTemplate();
	  if(template==null || StringUtils.isEmpty(template.getDocument())) {
	  	logger.error("O template para source ( {} ) nao esta carregando ou esta com o parametro document vazio.", new Object[] { source.getName()});
			throw new IllegalArgumentException();

	  }
		return template.getDocument();
  }

	private String getPathTemplateSource(Source source) {
	  Template template = source.getTemplate();
	  Product product = template.getProduct();
	  if(template==null || StringUtils.isEmpty(template.getPath())) {
	  	logger.error("O template para source ( {} ) nao esta carregando ou esta com o parametro path vazio.", new Object[] { source.getName()});
			throw new IllegalArgumentException();
	  }
	  if(product==null || StringUtils.isEmpty(product.getPath())) {
	  	logger.error("O applicacao para source ( {} ) nao esta carregando ou esta com o parametro path vazio.", new Object[] { source.getName()});
	  	throw new IllegalArgumentException();
	  }
	  String path = DIR_TMP + "/" + product.getPath() + "/" + template.getPath();
		return path;
  }

	private Map<String, Object> getConteudo(Object obj) {
	  Map<String, Object> conteudo = new HashMap<String, Object>();
	  conteudo.put("materia", obj);
	  return conteudo;
  }
}
