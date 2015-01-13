package br.com.abril.mamute.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.abril.mamute.dao.ComponenteDAOImpl;
import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.model.Componente;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.support.errors.MamuteErrors;
import br.com.abril.mamute.support.factory.FileFactory;
import br.com.abril.mamute.support.tipos.TipoComponenteEnum;

@Controller
@RequestMapping("/componentes")
public class ComponenteController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String COMPONENTE_LIST = "componentes/ComponenteList";
	private static final String COMPONENTE_FORM = "componentes/ComponenteForm";
	private static final String REDIRECT_COMPONENTES = "redirect:/componentes/";

	@Autowired
	private ComponenteDAOImpl componenteDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private TemplateDAO templateDAO;

	@Autowired
	private MamuteErrors mamuteErrors;
	@Autowired
	private FileFactory fileFactory;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping("/")
	public String showListComponente(ModelMap model) {
		List<Componente> list = componenteDAO.list();
		model.addAttribute("listComponentes", list);
		return COMPONENTE_LIST;
	}

	@RequestMapping("/new")
	public String newComponente(ModelMap model) {
		selectPageBasic(model);
		model.addAttribute("componente", new Componente());
		return COMPONENTE_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editComponente(ModelMap model, @PathVariable String id) {
		Componente componente = componenteDAO.get(id);
		selectPageBasic(model);
		model.addAttribute("componente", componente);
		return COMPONENTE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteComponente(@PathVariable String id) {
		componenteDAO.delete(id);
		return REDIRECT_COMPONENTES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveComponente(ModelMap model, @Valid @ModelAttribute Componente componente, @RequestParam("files") List<MultipartFile> files, Errors errors) {

		if (errors.hasErrors()) {
			selectPageBasic(model);
			return COMPONENTE_FORM;
		}
		for (MultipartFile file : files) {
			if (!file.isEmpty()  && !fileFactory.validateFileJavascript(file)) {
				mamuteErrors.clean();
				mamuteErrors.addError(getMessageSource("product.falha.file.not.valide.error"), getMessageSource("product.falha.file.not.valide.js.text"));
				logger.error(getMessageSource("product.falha.file.not.valide.text"));
				model.addAttribute("mamuteErrors", mamuteErrors);
				selectPageBasic(model);
				return COMPONENTE_FORM;
			}
		}

		validadeComponenteId(componente);
		componenteDAO.saveOrUpdate(componente);

		Product product = productDAO.get(componente.getProductId());
		String path = fileFactory.generatePathOfDirectoryProduct(product.getPath());
		String pathRelative = "/componentes/" + componente.getId();
		path = path + pathRelative;
		List<String> listJavaScriptsComponente = null;
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				if (CollectionUtils.isEmpty(listJavaScriptsComponente)) {
					listJavaScriptsComponente = new ArrayList<String>();
				}
				String fileName = file.getOriginalFilename();
				try {
					fileFactory.salvarArquivoPathProduct(path, file, fileName);
					listJavaScriptsComponente.add(pathRelative + "/" + fileName);
				} catch (Exception e) {
					mamuteErrors.clean();
					mamuteErrors.addError(getMessageSource("global.inesperado.error"), getMessageSource("global.inesperado.text"));
					logger.error("You failed to upload {} : {} ", new Object[] { fileName,e.getMessage() });
					return COMPONENTE_FORM;
				}
				
			}
			if (!CollectionUtils.isEmpty(listJavaScriptsComponente)) {
				componente.setScripts(listJavaScriptsComponente);
				componenteDAO.saveOrUpdate(componente);
			}
		}
		
		

		return REDIRECT_COMPONENTES;
	}

	private void validadeComponenteId(Componente componente) {
		if (StringUtils.isEmpty(componente.getId()))
			componente.setId(null);
	}

	private String getMessageSource(String key) {
		return messageSource.getMessage(key, null, null);
	}

	private void selectPageBasic(ModelMap model) {
		model.addAttribute("listProduct", productDAO.list());
		model.addAttribute("listTemplate", templateDAO.list());
		model.addAttribute("listType", listComponenteTypeItens());
	}

	private ArrayList<TipoComponenteEnum> listComponenteTypeItens() {
		return new ArrayList<TipoComponenteEnum>(Arrays.asList(TipoComponenteEnum.values()));
	}
}
