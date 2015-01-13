package br.com.abril.mamute.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;

import br.com.abril.mamute.dao.ProductDAOImpl;
import br.com.abril.mamute.dao.SourceDAOImpl;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Source;

public class SourceControllerTest {


	private static final String REDIRECT_SOURCES = "redirect:/sources/";
	private static final String SOURCE_LIST = "sources/SourceList";
	private static final String SOURCE_FORM = "sources/SourceForm";

	@Mock
	private SourceDAOImpl sourceDao;
	@Mock
	private ProductDAOImpl productDAO;
	@Mock
	private ModelMap modelMap;
	@Mock
	private Errors errors;

	@InjectMocks
	private SourceController sourceController;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sourceController).build();
	}
	@Test
	public void testListaSource() throws Exception {
		final ArrayList<Source> list = new ArrayList<Source>();
		when(sourceDao.list()).thenReturn(list);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/sources/"))
		                .andExpect(status().isOk())
		                .andExpect(model().attribute("listSources", list))
		                .andExpect(view().name(SOURCE_LIST));;
	}
	@Test
	public void testNewSource() throws Exception {
	modelMap.addAttribute("source",new Source());
		mockMvc.perform(MockMvcRequestBuilders.get("/sources/new").sessionAttr("modelMap", modelMap))
			.andExpect(status().isOk())
	    .andExpect(model().attributeExists("source"))
	    .andExpect(model().attribute("source",new Source()))
	    .andExpect(view().name(SOURCE_FORM));;
	}

	@Test
	public void testEditSource() throws Exception {

		when(sourceDao.get(any(Integer.class))).thenReturn(createSourceCompleto());
		modelMap.addAttribute("source",createSourceCompleto());

		mockMvc.perform(MockMvcRequestBuilders.get("/sources/1/edit").sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("source"))
      .andExpect(model().attribute("source",createSourceCompleto()))
      .andExpect(view().name(SOURCE_FORM));;
	}

	@Test
	public void testDeleteSource() throws Exception {

	mockMvc.perform(MockMvcRequestBuilders.get("/sources/1/delete"))
    .andExpect(status().is3xxRedirection())
    .andExpect(view().name(REDIRECT_SOURCES));
	}

	@Test
	public void testSaveSourcePreenchidoCorretamente() throws Exception {
		final Source source = createSourceCompleto();
		modelMap.addAttribute("source", source);
		Mockito.doCallRealMethod().when(sourceDao).saveOrUpdate(source);

		mockMvc.perform(MockMvcRequestBuilders.post("/sources/save")
			.param("name", "testeMamute")
			.param("product.id", "1")
			.param("source", "http://teste")
			.sessionAttr("errors", errors))
			.andExpect(status().is3xxRedirection())
	    .andExpect(view().name(REDIRECT_SOURCES));

		Mockito.verify(sourceDao).saveOrUpdate(any(Source.class));
	}

	@Test
	public void testSaveSourceNaoPreenchido() throws Exception {
		final Source source = createSourceCompleto();
		modelMap.addAttribute("source", source);

		mockMvc.perform(MockMvcRequestBuilders.post("/sources/save")
			.sessionAttr("errors", errors))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("source", "name"))
			.andExpect(model().errorCount(3))
	    .andExpect(view().name(SOURCE_FORM));

	}


	private Source createSourceCompleto() {
	  Source source = new Source();
		source.setId(1);
		source.setName("sourceTeste");
		source.setProduct(createProduct(1));
	  return source;
  }

	private Product createProduct(int idProduct) {
	  Product product = new Product();
	  product.setId(idProduct);
	  product.setName("produto");
	  product.setPath("/pathProduct");
	  return product;
  }
}
