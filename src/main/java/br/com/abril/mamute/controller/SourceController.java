package br.com.abril.mamute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private TemplateDAO templateDao;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		List<Source> listSources = sourceDao.list();
		ModelAndView model = new ModelAndView("sources/SourceList");
		model.addObject("listSources", listSources);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newSource() {
		ModelAndView model = new ModelAndView("sources/SourceForm");
		model.addObject("source", new Source());
		model.addObject("listProduct", productDao.list());
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editSource(HttpServletRequest request) {
		int sourceId = Integer.parseInt(request.getParameter("id"));
		Source source = sourceDao.get(sourceId);
		ModelAndView model = new ModelAndView("sources/SourceForm");
		model.addObject("source", source);
		model.addObject("listProduct", productDao.list());
		model.addObject("listTemplate", templateDao.list());
		return model;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteSource(HttpServletRequest request) {
		int sourceId = Integer.parseInt(request.getParameter("id"));
		sourceDao.delete(sourceId);
		return new ModelAndView("redirect:/sources/");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveSource(@ModelAttribute Source source) {
		sourceDao.saveOrUpdate(source);
		return new ModelAndView("redirect:/sources/");
	}

}
