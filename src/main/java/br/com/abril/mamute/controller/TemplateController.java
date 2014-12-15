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

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.model.Template;

/**
 * Handles requests for the product home page.
 */
@Controller
@RequestMapping("/templates")
public class TemplateController {

	private static final String TEMPLATE_FORM = "templates/TemplateForm";
	private static final String TEMPLATE_LIST = "templates/TemplateList";
	@Autowired
	private TemplateDAO templateDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private TemplateTypeDAO templateTypeDao;

	@RequestMapping("/")
	public String handleRequest(ModelMap model) {
		List<Template> listTemplates = templateDao.list();
		model.addAttribute("listTemplates", listTemplates);
		return TEMPLATE_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newTemplate(ModelMap model) {
		model.addAttribute("template", new Template());
		model.addAttribute("listProduct", productDao.list());
		model.addAttribute("listType", templateTypeDao.list());
		return TEMPLATE_FORM;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editTemplate(ModelMap model, HttpServletRequest request) {
		int templateId = Integer.parseInt(request.getParameter("id"));
		Template template = templateDao.get(templateId);
		model.addAttribute("template", template);
		model.addAttribute("listProduct", productDao.list());
		model.addAttribute("listType", templateTypeDao.list());
		return TEMPLATE_FORM;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteTemplate(HttpServletRequest request) {
		int templateId = Integer.parseInt(request.getParameter("id"));
		templateDao.delete(templateId);
		return "redirect:/templates/";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveTemplate(Model model, @Valid @ModelAttribute Template template, Errors errors) {
		
		Boolean validateMarca = template.getProduct().getId() != null;
		if (!validateMarca) {
			errors.rejectValue("product", "validate.product.fail.mandatory_field");
		}
		Boolean validateTemplateType = template.getType().getId() != null;
		if (!validateTemplateType) {
			errors.rejectValue("type", "validate.templatetype.fail.mandatory_field");
		}
		if (errors.hasErrors()) {
			return TEMPLATE_FORM;
		}
		
		templateDao.saveOrUpdate(template);
		return "redirect:/templates/";
	}

}
