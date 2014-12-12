package br.com.abril.mamute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.dao.ApplicationDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.model.Template;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/templates")
public class TemplateController {

	@Autowired
	private TemplateDAO templateDao;
	@Autowired
	private ApplicationDAO applicationDao;
	@Autowired
	private TemplateTypeDAO templateTypeDao;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		List<Template> listTemplates = templateDao.list();
		ModelAndView model = new ModelAndView("templates/TemplateList");
		model.addObject("listTemplates", listTemplates);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newTemplate() {
		ModelAndView model = new ModelAndView("templates/TemplateForm");
		model.addObject("template", new Template());
		model.addObject("listApplication", applicationDao.list());
		model.addObject("listType", templateTypeDao.list());
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editTemplate(HttpServletRequest request) {
		int templateId = Integer.parseInt(request.getParameter("id"));
		Template template = templateDao.get(templateId);
		ModelAndView model = new ModelAndView("templates/TemplateForm");
		model.addObject("template", template);
		model.addObject("listApplication", applicationDao.list());
		model.addObject("listType", templateTypeDao.list());
		return model;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteTemplate(HttpServletRequest request) {
		int templateId = Integer.parseInt(request.getParameter("id"));
		templateDao.delete(templateId);
		return new ModelAndView("redirect:/templates/");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveTemplate(@ModelAttribute Template template) {
		templateDao.saveOrUpdate(template);
		return new ModelAndView("redirect:/templates/");
	}

}
