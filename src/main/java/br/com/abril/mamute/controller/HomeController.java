package br.com.abril.mamute.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.config.SystemConfiguration;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.service.StaticEngine;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.pooling.ManagerPooling;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {


	private static final String DIR_TMP = SystemConfiguration.getPropertyAsString(SystemConfiguration.DIR_TMP);
	@Autowired
	private StaticEngine staticEngine;

	@Autowired
	private TemplateDAO templateDao;

	@Autowired
	private Editorial editorial;

	@Autowired
	ManagerPooling managerPooling;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		ModelAndView model = new ModelAndView("home");
		return model;
	}

	@RequestMapping("/generate")
	public ModelAndView generateTemplate() throws Exception {
		Template template = templateDao.get(1);
		String modelo = template.getDocument();
		Materia materia = new Materia();
		materia.setSlug("teste-slug");
		materia.setCorpo("Miolo");
		Map<String, Object> conteudo = getConteudo(materia);

		String path = generatePathTemplate(template);
		staticEngine.process(modelo, conteudo, path );
		ModelAndView model = new ModelAndView("home");
		return model;
	}

	private String generatePathTemplate(Template template) {
	  String path = DIR_TMP+template.getApplication().getPath()+"/"+template.getPath();
	  return path;
  }

	@RequestMapping("/editorial")
	public ModelAndView testeEditorial() throws Exception {

		Materia materia = editorial.getMateria("547e50316b6c1236ad0001d0");
		ModelAndView model = new ModelAndView("editorial");
		model.addObject("materia", materia);
		return model;
	}


	@RequestMapping(value = "/editorial/{id}", method = RequestMethod.GET)
	public ModelAndView testeEditorialId(HttpServletRequest request,@PathVariable String id) throws Exception {

		Materia materia = editorial.getMateria(id);
		ModelAndView model = new ModelAndView("editorial");
		model.addObject("materia", materia);
		return model;
	}

	@RequestMapping(value = "/generateMateria/{id}", method = RequestMethod.GET)
	public ModelAndView generateMateriaId(HttpServletRequest request,@PathVariable String id) throws Exception {
		Template template = templateDao.get(2);
		String modelo = template.getDocument();
		String path = generatePathTemplate(template);

		Materia materia = editorial.getMateria(id);
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
		return model;
	}


	private Map<String, Object> getConteudo(Object obj) {
	  Map<String, Object> conteudo = new HashMap<String, Object>();
	  conteudo.put("materia", obj);
	  return conteudo;
  }

}
