package br.com.abril.mamute.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;

import br.com.abril.mamute.dao.TemplateTypeDAOImpl;
import br.com.abril.mamute.model.TemplateType;

@RunWith(PowerMockRunner.class)
public class TemplateTypeControllerTest {

	private static final String REDIRECT_TEMPLATE_TYPES = "redirect:/templateTypes/";
	private static final String TEMPLATE_TYPE_LIST = "templateTypes/TemplateTypeList";
	private static final String TEMPLATE_TYPE_FORM = "templateTypes/TemplateTypeForm";
	@Mock
	private TemplateTypeDAOImpl templateTypeDao;
	@Mock
	private ModelMap modelMap;
	@Mock
	private Errors errors;

	@InjectMocks
	private TemplateTypeController templateTypeController;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(templateTypeController).build();
	}
	@Test
	public void testListaTemplateType() throws Exception {
		final ArrayList<TemplateType> list = new ArrayList<TemplateType>();
		when(templateTypeDao.list()).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/templateTypes/"))
		                .andExpect(status().isOk())
		                .andExpect(model().attribute("listTemplateTypes", list))
		                .andExpect(view().name(TEMPLATE_TYPE_LIST));;
	}
	@Test
	public void testNewTemplateType() throws Exception {
		modelMap.addAttribute("templateType",new TemplateType());

		mockMvc.perform(MockMvcRequestBuilders.get("/templateTypes/new").sessionAttr("modelMap", modelMap))
			.andExpect(status().isOk())
	    .andExpect(model().attributeExists("templateType"))
	    .andExpect(model().attribute("templateType",new TemplateType()))
	    .andExpect(view().name(TEMPLATE_TYPE_FORM));;
	}

	@Test
	public void testEditTemplateType() throws Exception {

		when(templateTypeDao.get(any(Integer.class))).thenReturn(createTemplateTypeCompleto());
		modelMap.addAttribute("templateType",createTemplateTypeCompleto());

		mockMvc.perform(MockMvcRequestBuilders.get("/templateTypes/1/edit").sessionAttr("modelMap", modelMap))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("templateType"))
      .andExpect(model().attribute("templateType",createTemplateTypeCompleto()))
      .andExpect(view().name(TEMPLATE_TYPE_FORM));;
	}

	@Test
	public void testDeleteTemplateType() throws Exception {

	mockMvc.perform(MockMvcRequestBuilders.get("/templateTypes/1/delete"))
    .andExpect(status().is3xxRedirection())
    .andExpect(view().name(REDIRECT_TEMPLATE_TYPES));
	}

	@Test
	public void testSaveTemplateTypePreenchidoCorretamente() throws Exception {
		final TemplateType templateType = createTemplateTypeCompleto();
		modelMap.addAttribute("templateType", templateType);
		Mockito.doCallRealMethod().when(templateTypeDao).saveOrUpdate(templateType);

		mockMvc.perform(MockMvcRequestBuilders.post("/templateTypes/save")
			.param("name", "testeMamute")
			.sessionAttr("errors", errors))
			.andExpect(status().is3xxRedirection())
	    .andExpect(view().name(REDIRECT_TEMPLATE_TYPES));

		Mockito.verify(templateTypeDao).saveOrUpdate(any(TemplateType.class));
	}

	@Test
	public void testSaveTemplateTypeNaoPreenchido() throws Exception {
		final TemplateType templateType = createTemplateTypeCompleto();
		modelMap.addAttribute("templateType", templateType);

		mockMvc.perform(MockMvcRequestBuilders.post("/templateTypes/save")
			.sessionAttr("errors", errors))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("templateType", "name"))
			.andExpect(model().errorCount(1))
	    .andExpect(view().name(TEMPLATE_TYPE_FORM));

	}

	private TemplateType createTemplateTypeCompleto() {
	  TemplateType templateType = new TemplateType();
		templateType.setId(1);
		templateType.setName("templateTeste");
	  return templateType;
  }

}
