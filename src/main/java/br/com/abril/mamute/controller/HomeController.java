package br.com.abril.mamute.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.model.editorial.Materia;
import br.com.abril.mamute.model.editorial.ResultadoBuscaMateria;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.pooling.ManagerPooling;
import br.com.abril.mamute.service.staticengine.StaticEngineMateria;
import br.com.abril.mamute.support.factory.FileFactory;


/**
 * Handles requests for the product home page.
 */
@Controller
@SessionAttributes({"listaMarcas","useMarca"})
public class HomeController {

	@Autowired
	private StaticEngineMateria staticEngine;

	@Autowired
	private TemplateDAO templateDao;

	@Autowired
	private Editorial editorial;
	
	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private ManagerPooling managerPooling;
	
	@Autowired
	private FileFactory fileFactory;

	@RequestMapping("/")
	public String home(ModelMap model) {
		Product product = productDAO.get(1);
		model.addAttribute("useMarca", product);
		List<Product> listaMarcas = productDAO.list();
		model.addAttribute("listaMarcas", listaMarcas);
		return "home";
	}
	
	@RequestMapping("/selectMarca/{id}")
	public String list(ModelMap model,@PathVariable int id) {
		Product product = productDAO.get(id);
		model.addAttribute("useMarca", product);
		return "redirect:/";
	}

	@RequestMapping("/generate")
	public ModelAndView generateTemplate() throws Exception {
		Template template = templateDao.get(1);
		String modelo = template.getDocument();
		Materia materia = new Materia();
		materia.setSlug("teste-slug");
		materia.setCorpo("Miolo");
		Map<String, Object> conteudo = getConteudo(materia);

		String path = fileFactory.generatePathOfDirectoryTemplate(template.getProduct().getPath(),template.getPath());
		staticEngine.process(modelo, conteudo, path );
		ModelAndView model = new ModelAndView("home");
		return model;
	}


	@RequestMapping("/editorial")
	public ModelAndView testeEditorial() throws Exception {

		Materia materia = editorial.getMateriaIdHash("547e50316b6c1236ad0001d0");
		ModelAndView model = new ModelAndView("editorial");
		model.addObject("materia", materia);
		return model;
	}


	@RequestMapping(value = "/editorial/{id}", method = RequestMethod.GET)
	public ModelAndView testeEditorialId(HttpServletRequest request,@PathVariable String id) throws Exception {

		Materia materia = editorial.getMateriaIdHash(id);
		ModelAndView model = new ModelAndView("editorial");
		model.addObject("materia", materia);
		return model;
	}

	@RequestMapping(value = "/generateMateria/{id}", method = RequestMethod.GET)
	public ModelAndView generateMateriaId(HttpServletRequest request,@PathVariable String id) throws Exception {
		Template template = templateDao.get(3);
		String modelo = template.getDocument();
		String path = fileFactory.generatePathOfDirectoryTemplate(template.getProduct().getPath(),template.getPath());

		Materia materia = editorial.getMateriaIdHash(id);
		Map<String, Object> conteudo = getConteudo(materia);
		staticEngine.process(modelo, conteudo, path );


		ModelAndView model = new ModelAndView("home");
		return model;
	}

	@RequestMapping(value = "/gerarLista", method = RequestMethod.GET)
	public ModelAndView excuteList() throws Exception {

		managerPooling.processPoolings();

		ModelAndView model = new ModelAndView("home");
		return model;
	}

	@RequestMapping(value = "/ultimasNoticias/{marca}", method = RequestMethod.GET)
	public ModelAndView listaUltimasNoticias(HttpServletRequest request,@PathVariable String marca) throws Exception {

		ResultadoBuscaMateria resultadoBuscaConteudo = editorial.getListaUltimasNoticias(marca);
		ModelAndView model = new ModelAndView("listaMaterias");
		model.addObject("list", resultadoBuscaConteudo.getResultado());
		model.addObject("marca", marca);
		return model;
	}


	private Map<String, Object> getConteudo(Object obj) {
	  Map<String, Object> conteudo = new HashMap<String, Object>();
	  conteudo.put("materia", obj);
	  return conteudo;
  }

}
