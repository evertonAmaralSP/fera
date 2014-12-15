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

import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.model.TemplateType;

/**
 * Handles requests for the templateType home page.
 */
@Controller
@RequestMapping("/templateTypes")
public class TemplateTypeController {

	private static final String TEMPLATE_TYPE_FORM = "templateTypes/TemplateTypeForm";
	private static final String TEMPLATE_TYPE_LIST = "templateTypes/TemplateTypeList";
	@Autowired
	private TemplateTypeDAO templateTypeDao;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		List<TemplateType> listTemplateTypes = templateTypeDao.list();
		ModelAndView model = new ModelAndView(TEMPLATE_TYPE_LIST);
		model.addObject("listTemplateTypes", listTemplateTypes);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newTemplateTyp(ModelMap model) {
		model.addAttribute("templateType", new TemplateType());
		return TEMPLATE_TYPE_FORM;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editTemplateType(ModelMap model, HttpServletRequest request) {
		int templateTypeId = Integer.parseInt(request.getParameter("id"));
		TemplateType templateType = templateTypeDao.get(templateTypeId);
		model.addAttribute("templateType", templateType);
		return TEMPLATE_TYPE_FORM;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteTemplateType(HttpServletRequest request) {
		int templateTypeId = Integer.parseInt(request.getParameter("id"));
		templateTypeDao.delete(templateTypeId);
		return "redirect:/templateTypes/";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveTemplateType(Model model, @Valid @ModelAttribute TemplateType templateType, Errors erros) {

		if (erros.hasErrors()) {
			return TEMPLATE_TYPE_FORM;
		}
		
		templateTypeDao.saveOrUpdate(templateType);
		return "redirect:/templateTypes/";
	}

}
