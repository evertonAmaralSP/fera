package br.com.abril.fera.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import br.com.abril.fera.dao.ProductDAO;
import br.com.abril.fera.dao.SourceDAO;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Source;

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

	@RequestMapping("/")
	public String list(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		List<Source> listSources = sourceDao.listByProduct(product);
		model.addAttribute("listSources", listSources);
		return SOURCE_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newSource(ModelMap model) {
		model.addAttribute("source", new Source());
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editSource(ModelMap model,@PathVariable int id) {
		Source source = sourceDao.get(id);
		model.addAttribute("source", source);
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteSource(@PathVariable int id) {
		sourceDao.delete(id);
		return REDIRECT_SOURCES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveSource(Model model, @Valid @ModelAttribute Source source, Errors errors,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		source.setProduct(product);
		
		if (errors.hasErrors()) {
			return SOURCE_FORM;
		}
		sourceDao.saveOrUpdate(source);
		return REDIRECT_SOURCES;
	}

}
