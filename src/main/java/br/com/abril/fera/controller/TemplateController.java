package br.com.abril.fera.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.abril.fera.dao.ProductDAO;
import br.com.abril.fera.dao.SourceDAO;
import br.com.abril.fera.dao.TemplateDAO;
import br.com.abril.fera.dao.TemplateTypeDAO;
import br.com.abril.fera.exception.editorial.GomeException;
import br.com.abril.fera.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Template;
import br.com.abril.fera.model.editorial.Materia;
import br.com.abril.fera.model.editorial.ResultadoBuscaMateria;
import br.com.abril.fera.service.edtorial.Editorial;
import br.com.abril.fera.service.staticengine.StaticEngineMateria;
import br.com.abril.fera.support.date.DateUtils;
import br.com.abril.fera.support.errors.TemplateErrors;
import br.com.abril.fera.support.factory.FileFactory;

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

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/")
	public String list(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		List<Template> listTemplates = templateDao.listByProduct(product);
		model.addAttribute("listTemplates", listTemplates);
		return TEMPLATE_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newTemplate(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		model.addAttribute("template", new Template());
		listaSelects(model,product);
		return TEMPLATE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editTemplate(ModelMap model, @PathVariable String id,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		int templateId = Integer.parseInt(id);
		Template template = templateDao.get(templateId);
		model.addAttribute("template", template);
		listaSelects(model,product);
		return TEMPLATE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteTemplate(@PathVariable String id) {
		int templateId = Integer.parseInt(id);
		templateDao.delete(templateId);
		return REDIRECT_TEMPLATES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveTemplate(ModelMap model, @Valid @ModelAttribute Template template, Errors errors,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		template.setProduct(product);
		validateTemplateTypeId(template, errors);
		try {
	    staticEngine.validate(template.getDocumentDraft());
    } catch (IOException e) {
    	listaSelects(model,product);
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
			try{
				materia = editorial.getMateriaId(materia.getId(),true);
			

			String path = fileFactory.generatePathOfDirectoryTemplate(template.getProduct().getPath(), template.getPath());
			
				String modelo = getTemplateDocument(template);
				Map<String, Object> conteudo = getConteudo(materia);
				staticEngine.process(modelo, conteudo, path);
			} catch (IllegalArgumentException e){
				model.addAttribute("template_errors", new TemplateErrors( messageSource.getMessage("template.falha.template.not.public.header",null, null) ,String.format(messageSource.getMessage("template.falha.template.not.public",null, null) , template.getId(), template.getName())));
				model.addAttribute("template", template);
				return TEMPLATE_RE;
			} catch (GomeException e){
				logger.debug("[unProccessSource] Recurso solicitado não está mais disponível : {}", new Object[] {materia.getId() });
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
			try {
				materia = editorial.getMateriaId(materia.getId(),true);
			 

				String path = fileFactory.generatePathOfDirectoryTemplate(template.getProduct().getPath(), template.getPath());
				String modelo = getTemplateDocument(template);
				Map<String, Object> conteudo = getConteudo(materia);
				staticEngine.process(modelo, conteudo, path);
			} catch (IllegalArgumentException e){
				model.addAttribute("template_errors", new TemplateErrors( messageSource.getMessage("template.falha.template.not.public.header",null, null) ,String.format(messageSource.getMessage("template.falha.template.not.public",null, null) , template.getId(), template.getName())));
				model.addAttribute("template", template);
				return TEMPLATE_UN;
			} catch (GomeException e){
				logger.debug("[unProccessSource] Recurso solicitado não está mais disponível : {}", new Object[] {materia.getId() });
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

	private void listaSelects(ModelMap model, Product product) {
		model.addAttribute("listType", templateTypeDao.list());
		model.addAttribute("listSource", sourceDAO.listByProduct(product));
  }

	private void validateTemplateTypeId(Template template, Errors errors) {
	  Boolean validateTemplateType = (template.getType() == null || template.getType().getId() == null);
		if (validateTemplateType) {
			errors.rejectValue("type", "validate.templatetype.fail.mandatory_field");
		}
  }

}
