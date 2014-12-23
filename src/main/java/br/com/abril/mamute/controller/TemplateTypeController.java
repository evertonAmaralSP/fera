package br.com.abril.mamute.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.model.TemplateType;

/**
 * Handles requests for the templateType home page.
 */
@Controller
@RequestMapping("/templateTypes")
public class TemplateTypeController {

	private static final String REDIRECT_TEMPLATE_TYPES = "redirect:/templateTypes/";
	private static final String TEMPLATE_TYPE_FORM = "templateTypes/TemplateTypeForm";
	private static final String TEMPLATE_TYPE_LIST = "templateTypes/TemplateTypeList";
	@Autowired
	private TemplateTypeDAO templateTypeDao;

	@RequestMapping("/")
	public String getListaTemplateType(ModelMap model) {
		List<TemplateType> listTemplateTypes = templateTypeDao.list();
		model.addAttribute("listTemplateTypes", listTemplateTypes);
		return TEMPLATE_TYPE_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newTemplateType(ModelMap model) {
		model.addAttribute("templateType", new TemplateType());
		return TEMPLATE_TYPE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editTemplateType(ModelMap model,@PathVariable String id) {
		int templateTypeId =  Integer.parseInt(id);
		TemplateType templateType = templateTypeDao.get(templateTypeId);
		model.addAttribute("templateType", templateType);
		return TEMPLATE_TYPE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteTemplateType(@PathVariable String id) {
		int templateTypeId = Integer.parseInt(id);
		templateTypeDao.delete(templateTypeId);
		return REDIRECT_TEMPLATE_TYPES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveTemplateType(ModelMap model, @Valid @ModelAttribute TemplateType templateType, Errors erros) {
		if (erros.hasErrors()) {
			return TEMPLATE_TYPE_FORM;
		}
		templateTypeDao.saveOrUpdate(templateType);
		return REDIRECT_TEMPLATE_TYPES;
	}

}
