package br.com.abril.mamute.service.pooling;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.model.editorial.DataEditoria;
import br.com.abril.mamute.model.editorial.Materia;
import br.com.abril.mamute.model.editorial.ResultadoBuscaMateria;
import br.com.abril.mamute.service.edtorial.Editorial;
import br.com.abril.mamute.service.staticengine.StaticEngineMateria;
import br.com.abril.mamute.support.factory.FileFactory;

@RunWith(PowerMockRunner.class)
public class ManagerPoolingTest {
	@Mock
	SourceDAO sourceDAO;
	@Mock
	Editorial editorial;
	@Mock
	private Logger logger;
	@Mock
	private FileFactory fileFactory;
	@Mock
	private StaticEngineMateria staticEngine;
	@InjectMocks
	private ManagerPooling managerPooling;
	@Mock
	private TemplateDAO templateDAO;


	@Test
	public void testProcessPoolingComDataUltimaAtualizacaoNull() throws Exception {

		Source source = createSource();
		ArrayList<Source> listSource = createListSource(source);
		
		ResultadoBuscaMateria buscaMateria = createResultadoBuscaMateria();
		
		Mockito.when(sourceDAO.listSourceActives()).thenReturn(listSource);
		Mockito.when(editorial.getListaInSource(source.getSource())).thenReturn(buscaMateria);
		Mockito.when(editorial.getMateriaId("http://teste.api/materia/id/"+HASH_ID1,true)).thenReturn(createMateria(HASH_ID1,10,12));
		Mockito.when(editorial.getMateriaId("http://teste.api/materia/id/"+HASH_ID2,true)).thenReturn(createMateria(HASH_ID2,11,0));
		Map<String, Object> map = getConteudoTest(buscaMateria.getResultado()[0]);
		Mockito.doCallRealMethod().when(fileFactory).createDiretorio(any(String.class));

		Mockito.doCallRealMethod().when(staticEngine).process(documentTest,map ,pathTest);

		
		managerPooling.processPoolings();

		final Template template = source.getTemplates().get(0);
		assertTrue(template.getLastUpdateDateUpdatePooling().equals(primeiraDataTest));
	}

	private ArrayList<Source> createListSource(Source source) {
	  ArrayList<Source> listSource = new ArrayList<Source>();
		listSource.add(source);
	  return listSource;
  }

	@Test
	public void testProcessPoolingComDataUltimaAtualizacaoMenor() throws Exception {

		int day = 10;
		ArrayList<Source> listSource = new ArrayList<Source>();
		Source source = createSource(day);
		listSource.add(source);
		ResultadoBuscaMateria buscaMateria = createResultadoBuscaMateria();
		Mockito.when(sourceDAO.listSourceActives()).thenReturn(listSource);
		Mockito.when(editorial.getListaInSource(source.getSource())).thenReturn(buscaMateria);
		Mockito.when(editorial.getMateriaId("http://teste.api/materia/id/"+HASH_ID1,true)).thenReturn(createMateria(HASH_ID1,10,12));
		Mockito.when(editorial.getMateriaId("http://teste.api/materia/id/"+HASH_ID2,true)).thenReturn(createMateria(HASH_ID2,11,0));
		Map<String, Object> map = getConteudoTest(buscaMateria.getResultado()[0]);
		Mockito.doCallRealMethod().when(staticEngine).process(documentTest,map ,pathTest);

		managerPooling.processPoolings();
		final Template template = source.getTemplates().get(0);
		assertTrue(template.getLastUpdateDateUpdatePooling().equals(primeiraDataTest));
	}

	@Test
	public void testProcessPoolingComDataUltimaAtualizacaoMaior() throws Exception {

		int day = 12;
		ArrayList<Source> listSource = new ArrayList<Source>();
		Source source = createSource(day);
		listSource.add(source);
		ResultadoBuscaMateria buscaMateria = createResultadoBuscaMateria();
		Mockito.when(sourceDAO.listSourceActives()).thenReturn(listSource);
		Mockito.when(editorial.getListaInSource(source.getSource())).thenReturn(buscaMateria);
		Mockito.when(editorial.getMateriaId("http://teste.api/materia/id/"+HASH_ID1,true)).thenReturn(createMateria(HASH_ID1,10,12));
		Mockito.when(editorial.getMateriaId("http://teste.api/materia/id/"+HASH_ID2,true)).thenReturn(createMateria(HASH_ID2,11,0));
		Map<String, Object> map = getConteudoTest(buscaMateria.getResultado()[0]);

		Mockito.doCallRealMethod().when(staticEngine).process(documentTest,map ,pathTest);

		managerPooling.processPoolings();

		final Template template = source.getTemplates().get(0);
		assertTrue(template.getLastUpdateDateUpdatePooling().equals(createDate(day, 0)));
	}

	private ResultadoBuscaMateria createResultadoBuscaMateria() {
	  ResultadoBuscaMateria buscaMateria = new ResultadoBuscaMateria();
	  Materia[] materias = new Materia[] {createMateria(HASH_ID1,10,12),createMateria(HASH_ID2,11,0)};
	  buscaMateria.setResultado(materias);
	  return buscaMateria;
  }

	private Materia createMateria(String hashId, int day, int hour) {
	  Materia materia = new Materia();
	  materia.setId("http://teste.api/materia/id/" + hashId);
	  materia.setCorpo("essa materia foi escrita para teste");
		materia.setDataDisponibilizacao(createDate(day, hour));
		materia.setDisponibilizacao(createDisponibilizacao(day, hour));
		return materia;
  }

	private DataEditoria createDisponibilizacao(int day, int hour) {
	  DataEditoria disponibilizacao = new DataEditoria() ;
		disponibilizacao.setData(createDate(day, hour));
	  return disponibilizacao;
  }

	private Date createDate(int day, int hour) {
	  return new GregorianCalendar(2014, Calendar.DECEMBER, day, hour, 0, 0).getTime();
  }

	private Source createSource() {
	  Source source = new Source();
	  source.setId(1);
	  source.setName("materia-teste");
	  source.setSource("url-teste");
	  source.setActive(true);
	  source.setTemplates(createListTemplate());

	  return source;
  }
	
	private Source createSource(int day) {
	  Source source = createSource();
	  source.setTemplates(createListTemplate(day));

	  return source;
  }
	
	private List<Template> createListTemplate(){
		List<Template> list = new ArrayList<Template>();
		list.add(createTemplate());
		return list;
	}

	
	private List<Template> createListTemplate(int day){
		List<Template> list = new ArrayList<Template>();
		list.add(createTemplate(day));
		return list;
	}
	private Template createTemplate() {
		Template template = new Template();
		template.setProduct(createProduct());
		template.setId(1);
		template.setName("template");
		template.setDocument(documentTest);
		template.setPath("template");
		template.setId(1);
	  return template;
  }
	private Template createTemplate(int day) {
		Template template = createTemplate();
		template.setLastUpdateDateUpdatePooling(createDate(day,0));
	  return template;
  }

	private Product createProduct() {
	  Product product = new Product();
	  product.setId(1);
	  product.setName("applicacao-teste");
	  product.setPath("product-teste");
	  return product;
  }

	private Map<String, Object> getConteudoTest(Object obj) {
	  Map<String, Object> conteudo = new HashMap<String, Object>();
	  conteudo.put("materia", obj);
	  return conteudo;
  }

	private String documentTest = "<html>${materia.id}</html>";
	//TODO: MELHORAR TESTE LER DO ARQUIVO PROPERTIES SE TESTES
	private String pathTest = "/tmp/applicacao-teste/template";

  private Date primeiraDataTest = createDate(11, 0);

	private static final String HASH_ID2 = "def";
	private static final String HASH_ID1 = "acb";
	
}
