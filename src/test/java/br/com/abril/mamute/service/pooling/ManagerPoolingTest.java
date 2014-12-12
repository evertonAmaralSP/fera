package br.com.abril.mamute.service.pooling;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import br.com.abril.mamute.dao.SourceDAO;
import br.com.abril.mamute.model.Application;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.model.Source;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.service.StaticEngine;
import br.com.abril.mamute.service.edtorial.Editorial;

@RunWith(PowerMockRunner.class)
public class ManagerPoolingTest {
	@Mock
	SourceDAO sourceDAO;
	@Mock
	Editorial editorial;
	@Mock
	private Logger logger;
	@Mock
	private StaticEngine staticEngine;
	@InjectMocks
	private ManagerPooling managerPooling;


	@Test
	public void testProcessPoolingComDataUltimaAtualizacaoNull() {

		ArrayList<Source> listSource = new ArrayList<Source>();
		Source source = createSource();
		listSource.add(source);
		ResultadoBuscaMateria buscaMateria = createResultadoBuscaMateria();
		Mockito.when(sourceDAO.listSourceActives()).thenReturn(listSource);
		Mockito.when(editorial.getListaInSource(source.getSource())).thenReturn(buscaMateria);
		Map<String, Object> map = getConteudoTest(buscaMateria.getResultado()[0]);
//		Mockito.when(staticEngine.process(documentTest,map ,pathTest)).thenReturn(true);
		Mockito.doCallRealMethod().when(staticEngine).process(documentTest,map ,pathTest);

		managerPooling.processPoolings();

		assertTrue(source.getLastUpdateDatePooling().equals(primeiraDataTest));
	}

	@Test
	public void testProcessPoolingComDataUltimaAtualizacaoMenor() {

		int day = 10;
		ArrayList<Source> listSource = new ArrayList<Source>();
		Source source = createSource(day);
		listSource.add(source);
		ResultadoBuscaMateria buscaMateria = createResultadoBuscaMateria();
		Mockito.when(sourceDAO.listSourceActives()).thenReturn(listSource);
		Mockito.when(editorial.getListaInSource(source.getSource())).thenReturn(buscaMateria);
		Map<String, Object> map = getConteudoTest(buscaMateria.getResultado()[0]);
//	Mockito.when(staticEngine.process(documentTest,map ,pathTest)).thenReturn(true);
	Mockito.doCallRealMethod().when(staticEngine).process(documentTest,map ,pathTest);

		managerPooling.processPoolings();

		assertTrue(source.getLastUpdateDatePooling().equals(primeiraDataTest));
	}

	@Test
	public void testProcessPoolingComDataUltimaAtualizacaoMaior() {

		int day = 12;
		ArrayList<Source> listSource = new ArrayList<Source>();
		Source source = createSource(day);
		listSource.add(source);
		ResultadoBuscaMateria buscaMateria = createResultadoBuscaMateria();
		Mockito.when(sourceDAO.listSourceActives()).thenReturn(listSource);
		Mockito.when(editorial.getListaInSource(source.getSource())).thenReturn(buscaMateria);
		Map<String, Object> map = getConteudoTest(buscaMateria.getResultado()[0]);
//	Mockito.when(staticEngine.process(documentTest,map ,pathTest)).thenReturn(true);
	Mockito.doCallRealMethod().when(staticEngine).process(documentTest,map ,pathTest);

		managerPooling.processPoolings();

		assertTrue(source.getLastUpdateDatePooling().equals(createDate(day, 0)));
	}

	private ResultadoBuscaMateria createResultadoBuscaMateria() {
	  ResultadoBuscaMateria buscaMateria = new ResultadoBuscaMateria();
	  Materia[] materias = new Materia[] {createMateria("acb",10,12),createMateria("def",11,0)};
	  buscaMateria.setResultado(materias);
	  return buscaMateria;
  }

	private Materia createMateria(String hashId, int day, int hour) {
	  Materia materia = new Materia();
	  materia.setId("http://teste.api/materia/id/" + hashId);
	  materia.setCorpo("essa materia foi escrita para teste");
		materia.setDataDisponibilizacao(createDate(day, hour));
		return materia;
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
	  source.setTemplate(createTemplate());

	  return source;
  }

	private Source createSource(int day) {
	  Source source = createSource();
	  source.setLastUpdateDatePooling(createDate(day,0));
	  return source;
  }

	private Template createTemplate() {
		Template template = new Template();
		template.setApplication(createApplication());
		template.setId(1);
		template.setName("template");
		template.setDocument(documentTest);
		template.setPath("template");
		template.setId(1);
	  return template;
  }

	private Application createApplication() {
	  Application application = new Application();
	  application.setId(1);
	  application.setName("applicacao-teste");
	  application.setPath("application-teste");
	  return application;
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
}
