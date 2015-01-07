package br.com.abril.mamute.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.staticengine.StaticEngineMateria;
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

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private Editorial editorial;
	@Autowired
	private StaticEngineMateria staticEngine;
	@Autowired
	private FileFactory fileFactory;

	@RequestMapping("/")
	public String handleRequest(ModelMap model) throws Exception {
		List<Source> listSources = sourceDao.list();
		model.addAttribute("listSources", listSources);
		return SOURCE_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newSource(ModelMap model) {
		model.addAttribute("source", new Source());
		model.addAttribute("listProduct", productDao.list());
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editSource(ModelMap model,@PathVariable String id) {
		int sourceId = Integer.parseInt(id);
		Source source = sourceDao.get(sourceId);
		model.addAttribute("source", source);
		model.addAttribute("listProduct", productDao.list());
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteSource(@PathVariable String id) {
		int sourceId = Integer.parseInt(id);
		sourceDao.delete(sourceId);
		return REDIRECT_SOURCES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveSource(Model model, @Valid @ModelAttribute Source source, Errors errors) {
		validateProductId(source, errors);
		if (errors.hasErrors()) {
			model.addAttribute("listProduct", productDao.list());
			return SOURCE_FORM;
		}
		sourceDao.saveOrUpdate(source);
		return REDIRECT_SOURCES;
	}

	private void validateProductId(Source source, Errors errors) {
	  if (source.getProduct() == null || source.getProduct().getId() == null) {
	  	errors.rejectValue("product", "validate.product.fail.mandatory_field");
	  }
  }

}
