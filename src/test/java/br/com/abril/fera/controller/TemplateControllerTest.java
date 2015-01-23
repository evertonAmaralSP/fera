package br.com.abril.fera.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;

import br.com.abril.fera.controller.TemplateController;
import br.com.abril.fera.dao.ProductDAO;
import br.com.abril.fera.dao.SourceDAO;
import br.com.abril.fera.dao.TemplateDAOImpl;
import br.com.abril.fera.dao.TemplateTypeDAO;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Source;
import br.com.abril.fera.model.Template;
import br.com.abril.fera.model.TemplateType;
import br.com.abril.fera.model.editorial.Materia;
import br.com.abril.fera.model.editorial.ResultadoBuscaMateria;
import br.com.abril.fera.service.edtorial.Editorial;
import br.com.abril.fera.service.staticengine.StaticEngineMateria;
import br.com.abril.fera.support.factory.FileFactory;

@RunWith(PowerMockRunner.class)
public class TemplateControllerTest {

	private static final String REDIRECT_TEMPLATES = "redirect:/templates/";
	private static final String TEMPLATE_FORM = "templates/TemplateForm";
	private static final String TEMPLATE_LIST = "templates/TemplateList";
	private static final String TEMPLATE_RE = "templates/TemplateRe";
	private static final String TEMPLATE_UN = "templates/TemplateUn";
	@Mock
	private TemplateDAOImpl templateDao;
	@Mock
	private ProductDAO productDao;
	@Mock
	private TemplateTypeDAO templateTypeDao;
	@Mock
	private SourceDAO sourceDAO;

	@Mock
	private ModelMap modelMap;
	@Mock
	private Errors errors;
	@InjectMocks
	private TemplateController templateController;
	
	private MockMvc mockMvc;

	@Mock
	private StaticEngineMateria staticEngine;
	@Mock
	private MessageSource messageSource;
	@Mock
	private Editorial editorial;
	@Mock
	private FileFactory fileFactory;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(templateController).build();
	}
	
	@Test
	public void testListaTemplate() throws Exception {
		final ArrayList<Template> list = new ArrayList<Template>();
		when(templateDao.list()).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/"))
		                .andExpect(status().isOk())
		                .andExpect(model().attribute("listTemplates", list))
		                .andExpect(view().name(TEMPLATE_LIST));;
	}
	@Test
	public void testNewTemplate() throws Exception {
		loadSelectorTemplate();
		modelMap.addAttribute("template",new Template());

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/new").sessionAttr("modelMap", modelMap))
			.andExpect(status().isOk())
	    .andExpect(model().attributeExists("template"))
	    .andExpect(model().attribute("template",new Template()))
	    .andExpect(view().name(TEMPLATE_FORM));;
	}
	@Test
	public void testEditTemplate() throws Exception {
		final Template templateCompleto = createTemplateCompleto(1,1);

		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/1/edit").sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_FORM));;
	}

	@Test
	public void testDeleteTemplate() throws Exception {

	mockMvc.perform(MockMvcRequestBuilders.get("/templates/1/delete"))
    .andExpect(status().is3xxRedirection())
    .andExpect(view().name(REDIRECT_TEMPLATES));
	}

	@Test
	public void testSaveTemplatePreenchidoCorretamente() throws Exception {
		final Template template = createTemplateCompleto(1,1);
		modelMap.addAttribute("template", template);
		Mockito.doCallRealMethod().when(staticEngine).validate(any(String.class));
		Mockito.doCallRealMethod().when(templateDao).saveOrUpdate(template);
		mockMvc.perform(MockMvcRequestBuilders.post("/templates/save")
			.param("name", "testeTemplateMamute")
			.param("type.id", "1")
			.param("path", "/pathTemplate")
			.param("documentDraft", "<div>teste</div>")
			.sessionAttr("errors", errors))
			.andExpect(status().is3xxRedirection())
	    .andExpect(view().name(REDIRECT_TEMPLATES));

		Mockito.verify(templateDao).saveOrUpdate(any(Template.class));
	}

	@Test
	public void testSaveTemplateNameNaoPreenchido() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/templates/save")
			.param("type.id", "1")
			.param("path", "/pathTemplate")
			.param("documentDraft", "<div>teste</div>")
			.sessionAttr("errors", errors))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("template", "name"))
			.andExpect(model().errorCount(1))
	    .andExpect(view().name(TEMPLATE_FORM));

	}


	@Test
	public void testSaveTemplateProdutoNaoPreenchido() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.post("/templates/save")
			.param("name", "testeTemplateMamute")
			.param("path", "/pathTemplate")
			.param("documentDraft", "<div>teste</div>")
			.sessionAttr("errors", errors))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("template", "type"))
			.andExpect(model().errorCount(2))
	    .andExpect(view().name(TEMPLATE_FORM));
	}
	
	@Test
	public void testSaveTemplateDocumentError() throws Exception {
		final Template template = createTemplateCompleto(1,1);
		loadSelectorTemplate();
		modelMap.addAttribute("template", template);
		final IOException toBeThrown = new IOException("template error");
		Mockito.doThrow(toBeThrown).doCallRealMethod().when(staticEngine).validate(any(String.class));
		Mockito.doCallRealMethod().when(templateDao).saveOrUpdate(template);
		when(messageSource.getMessage("template.falha.template.sintax",null,null)).thenReturn("teste");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/templates/save")
			.param("name", "testeTemplateMamute")
			.param("product.id", "1")
			.param("type.id", "1")
			.param("path", "/pathTemplate")
			.param("documentDraft", "<div>teste</div>")
			.sessionAttr("errors", errors))
			.andExpect(view().name(TEMPLATE_FORM));

	}
	
	@Test
	public void testReShowSource() throws Exception {
		final Template templateCompleto = createTemplateCompleto(1,1);
		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/1/re").sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_RE));
	}
	
	@Test
	public void testUnShowSource() throws Exception {
		final Template templateCompleto = createTemplateCompleto(1,1);
		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/1/un").sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_UN));
	}
	
	@SuppressWarnings("unchecked")
  @Test
	public void testReproccessTemplateSemTemplatePublicado() throws Exception {
		Template templateCompleto = createTemplateCompleto(1,1);
		removerDocumentTemplate(templateCompleto);
		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		when(editorial.getListaRetroativaPorData(any(String.class),any(Date.class))).thenReturn(createResultadoBuscaMateria());
		when(editorial.listaMaterias(any(ResultadoBuscaMateria.class))).thenReturn(createListaMateria());
		when(editorial.getMateriaId(any(String.class))).thenReturn(createMateria(1));
		when(fileFactory.generatePathOfDirectoryTemplate(any(String.class),any(String.class))).thenReturn("/pathProduct/pathTemplate");
		when(messageSource.getMessage("template.falha.template.not.public",null,null)).thenReturn("teste: %s - %s");
		when(messageSource.getMessage("template.falha.template.not.public.header",null,null)).thenReturn("teste");
		
		Mockito.doCallRealMethod().when(staticEngine).process(any(String.class),anyMap(),any(String.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/templates/1/re")
			.param("dataRetroativa", "25/12/2014")
			.sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_RE));
	}

  @Test
	public void testReproccessTemplate() throws Exception {
		final Template templateCompleto = createTemplateCompleto(1,1);
		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		when(editorial.getListaRetroativaPorData(any(String.class),any(Date.class))).thenReturn(createResultadoBuscaMateria());
		when(editorial.listaMaterias(any(ResultadoBuscaMateria.class))).thenReturn(createListaMateria());
		when(editorial.getMateriaId(any(String.class))).thenReturn(createMateria(1));
		when(fileFactory.generatePathOfDirectoryTemplate(any(String.class),any(String.class))).thenReturn("/pathProduct/pathTemplate");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/templates/1/re")
			.param("dataRetroativa", "25/12/2014")
			.sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_RE));
	}
	
	
	@SuppressWarnings("unchecked")
  @Test
	public void testUnProccessSourceSemTemplatePublicado() throws Exception {
		Template templateCompleto = createTemplateCompleto(1,1);
		removerDocumentTemplate(templateCompleto);
		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		when(editorial.getListaConsultaSlug(any(String.class),any(String.class))).thenReturn(createResultadoBuscaMateria());
		when(editorial.listaMaterias(any(ResultadoBuscaMateria.class))).thenReturn(createListaMateria());
		when(editorial.getMateriaId(any(String.class))).thenReturn(createMateria(1));
		when(fileFactory.generatePathOfDirectoryTemplate(any(String.class),any(String.class))).thenReturn("/pathProduct/pathTemplate");
		when(messageSource.getMessage("template.falha.template.not.public",null,null)).thenReturn("teste: %s - %s");
		when(messageSource.getMessage("template.falha.template.not.public.header",null,null)).thenReturn("teste");
		
		Mockito.doCallRealMethod().when(staticEngine).process(any(String.class),anyMap(),any(String.class));

		mockMvc.perform(MockMvcRequestBuilders.post("/templates/1/un")
			.param("slug", "slug-teste")
			.sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_UN));
	}
	
  @Test
	public void testUnProccessSource() throws Exception {
		final Template templateCompleto = createTemplateCompleto(1,1);
		when(templateDao.get(any(Integer.class))).thenReturn(templateCompleto);
		modelMap.addAttribute("template",templateCompleto);

		when(editorial.getListaConsultaSlug(any(String.class),any(String.class))).thenReturn(createResultadoBuscaMateria());
		when(editorial.listaMaterias(any(ResultadoBuscaMateria.class))).thenReturn(createListaMateria());
		when(editorial.getMateriaId(any(String.class))).thenReturn(createMateria(1));
		when(fileFactory.generatePathOfDirectoryTemplate(any(String.class),any(String.class))).thenReturn("/pathProduct/pathTemplate");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/templates/1/un")
			.param("slug", "slug-teste")
			.sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("template"))
      .andExpect(model().attribute("template",templateCompleto))
      .andExpect(view().name(TEMPLATE_UN));
	}

/*
	@RequestMapping(value = "/{id}/un", method = RequestMethod.POST)
	public String unproccessSource(ModelMap model, @PathVariable String id, HttpServletRequest request) throws ComunicacaoComEditorialException {
		return TEMPLATE_UN;
	}
*/
	/* methodos auxiliares */

	private ArrayList<Materia> createListaMateria() {
	  ArrayList<Materia> materias = new ArrayList<Materia>();
	  materias.add(createMateria(1));
	  
		return materias;
  }

	private void removerDocumentTemplate(Template templateCompleto) {
	  templateCompleto.setDocument(null);
  }
	
	private Materia createMateria(int id) {
		Materia materia = new Materia();
		materia.setId(id+"");
	  return materia;
  }

	private ResultadoBuscaMateria createResultadoBuscaMateria() {
	  ResultadoBuscaMateria resultadoBuscaConteudo = new ResultadoBuscaMateria();
	  return resultadoBuscaConteudo;
  }

	private Template createTemplateCompleto(int idProduct,int idType) {
	  Template template = new Template();
	  template.setId(1);
	  template.setName("testeTemplateMamute");
	  template.setProduct(createProduct(idProduct));
	  template.setType(createType(idType));
	  template.setSource(createSource(1));
	  template.setPath("/pathTemplate");
	  template.setDocumentDraft("<div>teste</div>");
	  template.setDocument("<div>teste</div>");
		
	  return template;
  }

	private Source createSource(int idSource) {
		Source source = new Source();
		source.setId(idSource);
		source.setName("testeSource");
		source.setActive(true);
		source.setSource("http://teste/materias");
		source.setProduct(createProduct(1));
	  return source;
  }

	private TemplateType createType(int idType) {
	  TemplateType templateType = new TemplateType();
	  templateType.setId(idType);
	  templateType.setName("type");
	  return templateType;
  }

	private Product createProduct(int idProduct) {
	  Product product = new Product();
	  product.setId(idProduct);
	  product.setName("produto");
	  product.setPath("/pathProduct");
	  return product;
  }

	private void loadSelectorTemplate() {
	  final ArrayList<TemplateType> listTT = new ArrayList<TemplateType>();
		when(templateTypeDao.list()).thenReturn(listTT);
		final ArrayList<Source> listS = new ArrayList<Source>();
		when(sourceDAO.list()).thenReturn(listS);
		final ArrayList<Product> listP = new ArrayList<Product>();
		when(productDao.list()).thenReturn(listP);
  }
}
