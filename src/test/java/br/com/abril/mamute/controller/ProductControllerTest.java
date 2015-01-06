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
import br.com.abril.mamute.model.Product;

public class ProductControllerTest {

	private static final String REDIRECT_MARCAS = "redirect:/marcas/";
	private static final String MARCA_LIST = "marcas/ProductList";
	private static final String MARCA_FORM = "marcas/ProductForm";
	@Mock
	private ProductDAOImpl productDao;
	@Mock
	private ModelMap modelMap;
	@Mock
	private Errors errors;

	@InjectMocks
	private ProductController productController;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	}
	@Test
	public void testListaProduct() throws Exception {
		final ArrayList<Product> list = new ArrayList<Product>();
		when(productDao.list()).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/marcas/"))
		                .andExpect(status().isOk())
		                .andExpect(model().attribute("listProducts", list))
		                .andExpect(view().name(MARCA_LIST));;
	}
	@Test
	public void testNewProduct() throws Exception {
		modelMap.addAttribute("product",new Product());

		mockMvc.perform(MockMvcRequestBuilders.get("/marcas/new").sessionAttr("modelMap", modelMap))
			.andExpect(status().isOk())
	    .andExpect(model().attributeExists("product"))
	    .andExpect(model().attribute("product",new Product()))
	    .andExpect(view().name(MARCA_FORM));;
	}

	@Test
	public void testEditProduct() throws Exception {

		when(productDao.get(any(Integer.class))).thenReturn(createProductCompleto());
		modelMap.addAttribute("product",createProductCompleto());

		mockMvc.perform(MockMvcRequestBuilders.get("/marcas/1/edit").sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("product"))
      .andExpect(model().attribute("product",createProductCompleto()))
      .andExpect(view().name(MARCA_FORM));;
	}

	@Test
	public void testDeleteProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/marcas/1/delete"))
	    .andExpect(status().is3xxRedirection())
	    .andExpect(view().name(REDIRECT_MARCAS));
	}

	@Test
	public void testSaveProductPreenchidoCorretamente() throws Exception {
		final Product product = createProductCompleto();
		modelMap.addAttribute("product", product);
		Mockito.doCallRealMethod().when(productDao).saveOrUpdate(product);

		mockMvc.perform(MockMvcRequestBuilders.post("/marcas/save")
			.param("name", "productTeste")
			.param("path", "/pathProduct")
			.sessionAttr("errors", errors))
			.andExpect(status().is3xxRedirection())
	    .andExpect(view().name(REDIRECT_MARCAS));

		Mockito.verify(productDao).saveOrUpdate(any(Product.class));
	}

	@Test
	public void testSaveProductNaoPreenchido() throws Exception {
		final Product product = createProductCompleto();
		modelMap.addAttribute("product", product);

		mockMvc.perform(MockMvcRequestBuilders.post("/marcas/save")
			.sessionAttr("errors", errors))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("product", "name"))
			.andExpect(model().attributeHasFieldErrors("product", "path"))
			.andExpect(model().errorCount(2))
	    .andExpect(view().name(MARCA_FORM));

	}

	private Product createProductCompleto() {
	  Product product = new Product();
		product.setId(1);
		product.setName("productTeste");
		product.setPath("/pathProduct");
	  return product;
  }

}
