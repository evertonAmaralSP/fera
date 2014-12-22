package br.com.abril.mamute.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;

import br.com.abril.mamute.dao.TemplateTypeDAO;
import br.com.abril.mamute.model.TemplateType;

@RunWith(PowerMockRunner.class)
public class TemplateTypeControllerTest {

	@Mock
	private TemplateTypeDAO templateTypeDao;
	@Mock
	private ModelMap modelMap;

	@InjectMocks
	private TemplateTypeController templateTypeController;

	MockMvc mockMvc;

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
		                .andExpect(MockMvcResultMatchers.status().isOk())
		                .andExpect(MockMvcResultMatchers.model().attribute("listTemplateTypes", list))
		                .andExpect(MockMvcResultMatchers.view().name("templateTypes/TemplateTypeList"));;
		                
	}

}
