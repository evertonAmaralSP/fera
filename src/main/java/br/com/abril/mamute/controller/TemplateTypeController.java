package br.com.abril.mamute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.model.TemplateType;

/**
 * Handles requests for the templateType home page.
 */
@Controller
@RequestMapping("/templateTypes")
public class TemplateTypeController {

	@Autowired
	private TemplateTypeDAO templateTypeDao;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		List<TemplateType> listTemplateTypes = templateTypeDao.list();
		ModelAndView model = new ModelAndView("templateTypes/TemplateTypeList");
		model.addObject("listTemplateTypes", listTemplateTypes);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newTemplateType() {
		ModelAndView model = new ModelAndView("templateTypes/TemplateTypeForm");
		model.addObject("templateType", new TemplateType());
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editTemplateType(HttpServletRequest request) {
		int templateTypeId = Integer.parseInt(request.getParameter("id"));
		TemplateType templateType = templateTypeDao.get(templateTypeId);
		ModelAndView model = new ModelAndView("templateTypes/TemplateTypeForm");
		model.addObject("templateType", templateType);
		return model;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteTemplateType(HttpServletRequest request) {
		int templateTypeId = Integer.parseInt(request.getParameter("id"));
		templateTypeDao.delete(templateTypeId);
		return new ModelAndView("redirect:/templateTypes/");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveTemplateType(@ModelAttribute TemplateType templateType) {
		templateTypeDao.saveOrUpdate(templateType);
		return new ModelAndView("redirect:/templateTypes/");
	}

}
