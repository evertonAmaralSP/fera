package br.com.abril.mamute.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.service.StaticEngine;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.support.date.DateUtils;
import br.com.abril.mamute.support.factory.FileFactory;

/**
 * Handles requests for the product home page.
 */
@Controller
@RequestMapping("/sources")
public class SourceController {
	
	private static final String REDIRECT_SOURCES = "redirect:/sources/";
	private static final String SOURCE_LIST = "sources/SourceList";
	private static final String SOURCE_FORM = "sources/SourceForm";
	private static final String SOURCE_RE = "sources/SourceRe";
	private static final String SOURCE_UN = "sources/SourceUn";

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private TemplateDAO templateDao;
	@Autowired
	private Editorial editorial;
	@Autowired
	private StaticEngine staticEngine;
	@Autowired
	private FileFactory fileFactory;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		List<Source> listSources = sourceDao.list();
		ModelAndView model = new ModelAndView(SOURCE_LIST);
		model.addObject("listSources", listSources);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newSource(ModelMap model) {
		model.addAttribute("source", new Source());
		model.addAttribute("listProduct", productDao.list());
		model.addAttribute("listTemplate", templateDao.list());
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editSource(ModelMap model,@PathVariable String id) {
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		model.addAttribute("source", source);
		model.addAttribute("listProduct", productDao.list());
		model.addAttribute("listTemplate", templateDao.list());
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteSource(HttpServletRequest request) {
		int sourceId = Integer.parseInt(request.getParameter("id"));
		sourceDao.delete(sourceId);
		return REDIRECT_SOURCES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveSource(Model model, @Valid @ModelAttribute Source source, Errors errors) {
		validateProductId(source, errors);
		validateTemplateId(source, errors);
		if (errors.hasErrors()) {
			model.addAttribute("listProduct", productDao.list());
			model.addAttribute("listTemplate", templateDao.list());
			return SOURCE_FORM;
		}
		sourceDao.saveOrUpdate(source);
		return REDIRECT_SOURCES;
	}

	@RequestMapping(value = "/{id}/re", method = RequestMethod.GET)
	public String reShowSource(ModelMap model,@PathVariable String id) {
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		model.addAttribute("source", source);
		
		return SOURCE_RE;
	}
	
	@RequestMapping(value = "/{id}/re", method = RequestMethod.POST)
	public String reproccessSource(ModelMap model,@PathVariable String id,HttpServletRequest request) throws ComunicacaoComEditorialException {
		long tempoInicioBuscaMaterias = System.currentTimeMillis();
		String dataRetroativa = request.getParameter("dataRetroativa"); 
		
		
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		Date data = DateUtils.formataData(dataRetroativa);
		
		ResultadoBuscaMateria resultadoBuscaConteudo = editorial.getListaRetroativaPorData(source.getSource(),data);
		List<Materia> listaMateria = editorial.listaMaterias(resultadoBuscaConteudo); 

		long totalBuscaMaterias = System.currentTimeMillis()-tempoInicioBuscaMaterias;
		
		long tempoInicioProcessamentoMaterias = System.currentTimeMillis();
		
		for (Materia materia : listaMateria) {
    	materia = editorial.getMateriaId(materia.getId());

    	String path = fileFactory.generatePathOfDirectoryTemplate(source.getProduct().getPath(), source.getTemplate().getPath());
    	String modelo = getTemplateDocument(source);
    	Map<String, Object> conteudo = getConteudo(materia);

    	staticEngine.process(modelo, conteudo, path);
    }
		long totalProcessamentoMaterias = System.currentTimeMillis()-tempoInicioProcessamentoMaterias;
	  model.addAttribute("source", source);
		model.addAttribute("resultado", resultadoBuscaConteudo);
		model.addAttribute("lista", listaMateria);
		model.addAttribute("tempoConsultaEditorial", totalBuscaMaterias);
		model.addAttribute("tempoProcessamentoArquivos", totalProcessamentoMaterias);
		model.addAttribute("dataRetroativa", dataRetroativa);
		return SOURCE_RE;
	}

	@RequestMapping(value = "/{id}/un", method = RequestMethod.GET)
	public String unShowSource(ModelMap model,@PathVariable String id) {
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		model.addAttribute("source", source);
		
		return SOURCE_UN;
	}
	@RequestMapping(value = "/{id}/un", method = RequestMethod.POST)
	public String unproccessSource(ModelMap model,@PathVariable String id,HttpServletRequest request) throws ComunicacaoComEditorialException {
		long tempoInicioBuscaMaterias = System.currentTimeMillis();
		String slug = request.getParameter("slug"); 
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		
		ResultadoBuscaMateria resultadoBuscaConteudo = editorial.getListaConsultaSlug(source.getSource(),slug);
		List<Materia> listaMateria = editorial.listaMaterias(resultadoBuscaConteudo); 
		long totalBuscaMaterias = System.currentTimeMillis()-tempoInicioBuscaMaterias;
		long tempoInicioProcessamentoMaterias = System.currentTimeMillis();
		for (Materia materia : listaMateria) {
    	materia = editorial.getMateriaId(materia.getId());

    	String path = fileFactory.generatePathOfDirectoryTemplate(source.getProduct().getPath(), source.getTemplate().getPath());
    	String modelo = getTemplateDocument(source);
    	Map<String, Object> conteudo = getConteudo(materia);

    	staticEngine.process(modelo, conteudo, path);
    }
		long totalProcessamentoMaterias = System.currentTimeMillis()-tempoInicioProcessamentoMaterias;
	  model.addAttribute("source", source);
		model.addAttribute("resultado", resultadoBuscaConteudo);
		model.addAttribute("lista", listaMateria);
		model.addAttribute("tempoConsultaEditorial", totalBuscaMaterias);
		model.addAttribute("tempoProcessamentoArquivos", totalProcessamentoMaterias);
		return SOURCE_UN;
	}
	private String getTemplateDocument(Source source) {
	  Template template = source.getTemplate();
	  if(template==null || StringUtils.isEmpty(template.getDocument())) {
	  	throw new IllegalArgumentException();
	  }
		return template.getDocument();
	}
	
	private Map<String, Object> getConteudo(Object obj) {
	  Map<String, Object> conteudo = new HashMap<String, Object>();
	  conteudo.put("materia", obj);
	  return conteudo;
  }
	
	private void validateTemplateId(Source source, Errors errors) {
	  if (source.getTemplate().getId() == null) errors.rejectValue("template", "validate.template.fail.mandatory_field");
  }

	private void validateProductId(Source source, Errors errors) {
	  if (source.getProduct().getId() == null) errors.rejectValue("product", "validate.product.fail.mandatory_field");
  }

}
