package br.com.abril.mamute.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.edtorial.EdtorialUrls;
import br.com.abril.mamute.support.date.DateUtils;

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

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private TemplateDAO templateDao;
	@Autowired
	private Editorial editorial;

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
		String dataRetroativa = request.getParameter("dataRetroativa"); 
		long tempoInicio = System.currentTimeMillis();
		
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		Date data = DateUtils.formataData(dataRetroativa);
		
		ResultadoBuscaMateria resultadoBuscaConteudo = editorial.getListaRetroativaPorData(source.getSource(),data);

		List<Materia> l = new ArrayList<Materia>();
		if(resultadoBuscaConteudo.getTotalResultados().intValue() > resultadoBuscaConteudo.getItensPorPagina().intValue() ){
			String linkUltimaPagina = resultadoBuscaConteudo.getLinkByRel("ultima").getHref();
			int ultimaPagina = 1;
			 try {
		    List<NameValuePair> listaParam = new URIBuilder(linkUltimaPagina).getQueryParams();
		    for (NameValuePair nameValuePair : listaParam) {
		      if("pw".equals(nameValuePair.getName())) {
		      	ultimaPagina = Integer.parseInt(nameValuePair.getValue());
		      }
	      }
	    
		    
				for (int index = ultimaPagina; index >= 2 ; index--) {
					String url = EdtorialUrls.BUSCA_ULTIMAS_MATEIAS;
					url = EdtorialUrls.paramQuery(url, resultadoBuscaConteudo.getQuery());
					url = EdtorialUrls.filterParam(url, "pw", index+"");
			    ResultadoBuscaMateria tmp = editorial.getResultadoBuscaMateria(url);
			    for (Materia m : tmp.getResultado()) {
		        l.add(m);
	        }
		    } 
				
				
			} catch ( Exception e ) { }
		}
	  for (Materia m : resultadoBuscaConteudo.getResultado()){
		  l.add(m);
		} 
	  long total = System.currentTimeMillis()-tempoInicio;
	  
		model.addAttribute("source", source);
		model.addAttribute("resultado", resultadoBuscaConteudo);
		model.addAttribute("lista", l);
		model.addAttribute("temp", total);
		return SOURCE_RE;
	}
	
	
	
	private void validateTemplateId(Source source, Errors errors) {
	  if (source.getTemplate().getId() == null) errors.rejectValue("template", "validate.template.fail.mandatory_field");
  }

	private void validateProductId(Source source, Errors errors) {
	  if (source.getProduct().getId() == null) errors.rejectValue("product", "validate.product.fail.mandatory_field");
  }

}
