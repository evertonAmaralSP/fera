package br.com.abril.mamute.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import br.com.abril.mamute.support.tipos.TipoComponenteEnum;

@Controller
@RequestMapping("/componentes")
public class ComponenteController {
	private static final String COMPONENTE_LIST = "componentes/ComponenteList";
	private static final String COMPONENTE_FORM = "componentes/ComponenteForm";
	private static final String REDIRECT_COMPONENTES = "redirect:/componentes/";
	
	@Autowired
	private ComponenteDAOImpl componenteDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private TemplateDAO templateDAO; 
	
	@RequestMapping("/")
	public String showListComponente(ModelMap model) {
		List<Componente> list = componenteDAO.list();
		model.addAttribute("listComponentes",list);
		return COMPONENTE_LIST;
	}
	
	@RequestMapping("/new")
	public String newComponente(ModelMap model) {
		model.addAttribute("listProduct", productDAO.list());
		model.addAttribute("listTemplate", templateDAO.list());
		model.addAttribute("listType", listComponenteTypeItens());
		model.addAttribute("componente", new Componente());
		return COMPONENTE_FORM;
	}
	
	private ArrayList<TipoComponenteEnum> listComponenteTypeItens() {
		return new ArrayList<TipoComponenteEnum>(Arrays.asList(TipoComponenteEnum.values()));
  }
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editComponente(ModelMap model, @PathVariable String id) {
		Componente componente = componenteDAO.get(id);
		model.addAttribute("listProduct", productDAO.list());
		model.addAttribute("listTemplate", templateDAO.list());
		model.addAttribute("listType", listComponenteTypeItens());
		model.addAttribute("componente", componente);
		return COMPONENTE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteComponente(@PathVariable String id) {
		componenteDAO.delete(id);
		return REDIRECT_COMPONENTES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveComponente(Model model, @Valid @ModelAttribute Componente componente, @RequestParam("files") List<MultipartFile> files, Errors errors) {

//		List<MultipartFile> files = file.getFiles();
//    	for(MultipartFile f:files){
//    		
//    	}
    		
    	
		if (errors.hasErrors()) {
			model.addAttribute("listProduct", productDAO.list());
			model.addAttribute("listTemplate", templateDAO.list());
			model.addAttribute("listType", listComponenteTypeItens());
			return COMPONENTE_FORM;
		}
		validadeComponenteId(componente);
		componenteDAO.saveOrUpdate(componente);

		return REDIRECT_COMPONENTES;
	}

	private void validadeComponenteId(Componente componente) {
		if(StringUtils.isEmpty(componente.getId())) componente.setId(null);
  }
	
}
