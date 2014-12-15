package br.com.abril.mamute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.model.Source;

/**
 * Handles requests for the product home page.
 */
@Controller
@RequestMapping("/sources")
public class SourceController {
	

	final String SOURCE_LIST = "sources/SourceList";
	final String SOURCE_FORM = "sources/SourceForm";

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private TemplateDAO templateDao;

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

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editSource(ModelMap model, HttpServletRequest request) {
		int sourceId = Integer.parseInt(request.getParameter("id"));
		Source source = sourceDao.get(sourceId);
		model.addAttribute("source", source);
		model.addAttribute("listProduct", productDao.list());
		model.addAttribute("listTemplate", templateDao.list());
		return SOURCE_FORM;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteSource(HttpServletRequest request) {
		int sourceId = Integer.parseInt(request.getParameter("id"));
		sourceDao.delete(sourceId);
		return new ModelAndView("redirect:/sources/");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveSource(Model model, @Valid @ModelAttribute Source source, Errors errors) {
		Boolean validateMarca = source.getProduct().getId() != null;
		if (!validateMarca) {
			errors.rejectValue("product", "validate.product.fail.mandatory_field");
		}
		Boolean validateTemplate = source.getTemplate().getId() != null;
		if (!validateTemplate) {
			errors.rejectValue("template", "validate.template.fail.mandatory_field");
		}

		if (errors.hasErrors()) {
			return SOURCE_FORM;
		}

		sourceDao.saveOrUpdate(source);
		
		return "redirect:/sources/";
	}

}
