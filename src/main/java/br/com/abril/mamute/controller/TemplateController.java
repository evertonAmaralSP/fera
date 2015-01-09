package br.com.abril.mamute.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.staticengine.StaticEngineMateria;
import br.com.abril.mamute.support.date.DateUtils;
import br.com.abril.mamute.support.errors.TemplateErrors;
import br.com.abril.mamute.support.factory.FileFactory;

/**
 * Handles requests for the product home page.
 */
@Controller
@RequestMapping("/templates")
public class TemplateController {

	private static final String REDIRECT_TEMPLATES = "redirect:/templates/";
	private static final String TEMPLATE_FORM = "templates/TemplateForm";
	private static final String TEMPLATE_LIST = "templates/TemplateList";
	private static final String TEMPLATE_RE = "templates/TemplateRe";
	private static final String TEMPLATE_UN = "templates/TemplateUn";

	@Autowired
	private TemplateDAO templateDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	private SourceDAO sourceDAO;
	@Autowired
	private TemplateTypeDAO templateTypeDao;
	@Autowired
	private FileFactory fileFactory;
	@Autowired
	private Editorial editorial;
	@Autowired
	private StaticEngineMateria staticEngine;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/")
	public String handleRequest(ModelMap model) {
		List<Template> listTemplates = templateDao.list();
		model.addAttribute("listTemplates", listTemplates);
		return TEMPLATE_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newTemplate(ModelMap model) {
		model.addAttribute("template", new Template());
		listaSelects(model);
		return TEMPLATE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editTemplate(ModelMap model, @PathVariable String id) {
		int templateId = Integer.parseInt(id);
		Template template = templateDao.get(templateId);
		model.addAttribute("template", template);
		listaSelects(model);
		return TEMPLATE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteTemplate(@PathVariable String id) {
		int templateId = Integer.parseInt(id);
		templateDao.delete(templateId);
		return REDIRECT_TEMPLATES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveTemplate(ModelMap model, @Valid @ModelAttribute Template template, Errors errors) {

		validadeProductId(template, errors);
		validateTemplateTypeId(template, errors);

		try {
	    staticEngine.validate(template.getDocumentDraft());
    } catch (IOException e) {
    	listaSelects(model);
    	model.addAttribute("template_errors", new TemplateErrors( messageSource.getMessage("template.falha.template.sintax",null, null), e.getMessage().replaceAll("(\n)", "<br />")));
    	return TEMPLATE_FORM;
    }

		if (errors.hasErrors()) {
			return TEMPLATE_FORM;
		}
		if(template.isPublicar()){
			template.setDocument(template.getDocumentDraft());	
		} else if(!StringUtils.isEmpty(template.getId())) {
			Template templateAntigo = templateDao.get(template.getId());
			template.setDocument(templateAntigo.getDocument());
		}
		
		templateDao.saveOrUpdate(template);
		return REDIRECT_TEMPLATES;
	}

	private void validateTemplateTypeId(Template template, Errors errors) {
	  Boolean validateTemplateType = (template.getType() == null || template.getType().getId() == null);
		if (validateTemplateType) {
			errors.rejectValue("type", "validate.templatetype.fail.mandatory_field");
		}
  }

	private void validadeProductId(Template template, Errors errors) {
	  Boolean validateMarca = (template.getProduct() == null || template.getProduct().getId() == null);
		if (validateMarca) {
			errors.rejectValue("product", "validate.product.fail.mandatory_field");
		}
  }

	@RequestMapping(value = "/{id}/re", method = RequestMethod.GET)
	public String reShowSource(ModelMap model, @PathVariable String id) {
		int templateId = Integer.parseInt(id);
		Template template = templateDao.get(templateId);
		model.addAttribute("template", template);

		return TEMPLATE_RE;
	}

	@RequestMapping(value = "/{id}/re", method = RequestMethod.POST)
	public String reproccessTemplate(ModelMap model, @PathVariable String id, HttpServletRequest request) throws ComunicacaoComEditorialException {
		long tempoInicioBuscaMaterias = System.currentTimeMillis();
		String dataRetroativa = request.getParameter("dataRetroativa");

		int templateId = Integer.parseInt(id);
		Template template = templateDao.get(templateId);
		Date data = DateUtils.formataData(dataRetroativa);

		ResultadoBuscaMateria resultadoBuscaConteudo = editorial.getListaRetroativaPorData(template.getSource().getSource(), data);
		List<Materia> listaMateria = editorial.listaMaterias(resultadoBuscaConteudo);

		long totalBuscaMaterias = System.currentTimeMillis() - tempoInicioBuscaMaterias;

		long tempoInicioProcessamentoMaterias = System.currentTimeMillis();

		for (Materia materia : listaMateria) {
			materia = editorial.getMateriaId(materia.getId(),true);

			String path = fileFactory.generatePathOfDirectoryTemplate(template.getProduct().getPath(), template.getPath());
			
			try {
				String modelo = getTemplateDocument(template);
				Map<String, Object> conteudo = getConteudo(materia);
				staticEngine.process(modelo, conteudo, path);
			} catch (IllegalArgumentException e){
				model.addAttribute("template_errors", new TemplateErrors( messageSource.getMessage("template.falha.template.not.public.header",null, null) ,String.format(messageSource.getMessage("template.falha.template.not.public",null, null) , template.getId(), template.getName())));
				model.addAttribute("template", template);
				return TEMPLATE_RE;
			}
			
			
			
		}
		long totalProcessamentoMaterias = System.currentTimeMillis() - tempoInicioProcessamentoMaterias;
		model.addAttribute("template", template);
		model.addAttribute("resultado", resultadoBuscaConteudo);
		model.addAttribute("lista", listaMateria);
		model.addAttribute("tempoConsultaEditorial", totalBuscaMaterias);
		model.addAttribute("tempoProcessamentoArquivos", totalProcessamentoMaterias);
		model.addAttribute("dataRetroativa", dataRetroativa);
		return TEMPLATE_RE;
	}

	@RequestMapping(value = "/{id}/un", method = RequestMethod.GET)
	public String unShowSource(ModelMap model, @PathVariable String id) {
		int templateId = Integer.parseInt(id);
		Template template = templateDao.get(templateId);
		model.addAttribute("template", template);

		return TEMPLATE_UN;
	}

	@RequestMapping(value = "/{id}/un", method = RequestMethod.POST)
	public String unProccessSource(ModelMap model, @PathVariable String id, HttpServletRequest request) throws ComunicacaoComEditorialException {
		long tempoInicioBuscaMaterias = System.currentTimeMillis();
		String slug = request.getParameter("slug");
		int templateId = Integer.parseInt(id);
		Template template = templateDao.get(templateId);

		ResultadoBuscaMateria resultadoBuscaConteudo = editorial.getListaConsultaSlug(template.getSource().getSource(), slug);
		List<Materia> listaMateria = editorial.listaMaterias(resultadoBuscaConteudo);
		long totalBuscaMaterias = System.currentTimeMillis() - tempoInicioBuscaMaterias;
		long tempoInicioProcessamentoMaterias = System.currentTimeMillis();
		for (Materia materia : listaMateria) {
			materia = editorial.getMateriaId(materia.getId(),true);

			String path = fileFactory.generatePathOfDirectoryTemplate(template.getProduct().getPath(), template.getPath());
			try {
				String modelo = getTemplateDocument(template);
				Map<String, Object> conteudo = getConteudo(materia);
				staticEngine.process(modelo, conteudo, path);
			} catch (IllegalArgumentException e){
				model.addAttribute("template_errors", new TemplateErrors( messageSource.getMessage("template.falha.template.not.public.header",null, null) ,String.format(messageSource.getMessage("template.falha.template.not.public",null, null) , template.getId(), template.getName())));
				model.addAttribute("template", template);
				return TEMPLATE_UN;
			}
			
		}
		long totalProcessamentoMaterias = System.currentTimeMillis() - tempoInicioProcessamentoMaterias;
		model.addAttribute("template", template);
		model.addAttribute("resultado", resultadoBuscaConteudo);
		model.addAttribute("lista", listaMateria);
		model.addAttribute("tempoConsultaEditorial", totalBuscaMaterias);
		model.addAttribute("tempoProcessamentoArquivos", totalProcessamentoMaterias);
		return TEMPLATE_UN;
	}

	private String getTemplateDocument(Template template) {
		if (template == null || StringUtils.isEmpty(template.getDocument())) {
			throw new IllegalArgumentException();
		}
		return template.getDocument();
	}

	private Map<String, Object> getConteudo(Object obj) {
		Map<String, Object> conteudo = new HashMap<String, Object>();
		conteudo.put("materia", obj);
		return conteudo;
	}

	private void listaSelects(ModelMap model) {
	  model.addAttribute("listProduct", productDao.list());
		model.addAttribute("listType", templateTypeDao.list());
		model.addAttribute("listSource", sourceDAO.list());
  }

}
