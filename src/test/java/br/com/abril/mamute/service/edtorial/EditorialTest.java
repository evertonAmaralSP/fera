package br.com.abril.mamute.service.edtorial;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import br.com.abril.mamute.exception.editorial.InternalServerErrorException;
import br.com.abril.mamute.exception.editorial.ServiceUnavailableException;
import br.com.abril.mamute.model.Conteudo;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.support.factory.HttpClientFactory;
import br.com.abril.mamute.support.factory.ModelFactory;
import br.com.abril.mamute.support.json.JsonUtil;
import br.com.abril.mamute.support.tipos.TipoRecursoEnum;

import com.google.gson.JsonObject;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
public class EditorialTest {
	@Mock
	private HttpClientFactory httpClientFactory;
	@Mock
	private JsonUtil jsonUtil;
	@Mock
	private ModelFactory modelFactory;
	@Mock
	private Logger logger;
	@InjectMocks
	private Editorial editorial;
	
	private CloseableHttpClient httpClient = null;
	private CloseableHttpResponse response = null;

	@Before
	public void before() {
		httpClient = mock(CloseableHttpClient.class);
		response = mock(CloseableHttpResponse.class);
	}

	@Test
	public void testListaUltimasNoticiasExistente()  throws Exception {
		HttpEntity entity = mock(HttpEntity.class);
		String inputStream = "{\"titulo\":\"Dominio Editorial - Resultado de busca\",\"total_resultados\":1,\"pagina_atual\":1,\"itens_por_pagina\":10,\"query\":\"marca=viajeaqui\",\"link\":[{\"rel\":\"self\",\"href\":\"http://editorial.api.abril.com.br/materias/busca?marca=viajeaqui\",\"type\":\"application/json\"},{\"rel\":\"search\",\"href\":\"http://editorial.api.abril.com.br/materias/busca/descritor\",\"type\":\"application/opensearchdescription+xml\"},{\"rel\":\"ultima\",\"href\":\"http://editorial.api.abril.com.br/materias/busca?marca=viajeaqui&pw=625\",\"type\":\"application/json\"},{\"rel\":\"proxima\",\"href\":\"http://editorial.api.abril.com.br/materias/busca?marca=viajeaqui&pw=2\",\"type\":\"application/json\"}],\"resultado\":[{\"slug\":\"roteiro-rodoviario-serra-gaucha\",\"fonte\":\"GUIA QUATRO RODAS\",\"titulo\":\"Confira um roteiro de carro pela Serra Gaucha\",\"chapeu\":\"Roteiro Rodoviario\",\"subtitulo\":\"Cidadezinhas charmosas, vinicolas, restaurantes e belas paisagens: eis o cenario que acompanha a travessia pelo interior do Rio Grande do Sul\",\"id\":\"http://editorial.api.abril.com.br/materias/id/4e7915a775c627222d000012\",\"data_ultima_atualizacao\":\"2014-12-09T23:03:33Z\",\"tipo_recurso\":\"materia\",\"idioma\":\"pt\",\"customizada\":false,\"data_criacao\":\"2011-09-20T22:37:28Z\",\"data_disponibilizacao\":\"2011-09-20T22:37:28Z\",\"link\":{\"href\":\"http://editorial.api.abril.com.br/materias/id/4e7915a775c627222d000012\",\"rel\":\"materia\",\"type\":\"application/json\"}}]}";
		ResultadoBuscaMateria resultado = new ResultadoBuscaMateria();
		Materia[] materias = {createMateria()};
		resultado.setResultado(materias);
		
		when(httpClientFactory.createHttpClient()).thenReturn(httpClient);
		when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));
		when(entity.getContent()).thenReturn(IOUtils.toInputStream(inputStream));
		when(response.getEntity()).thenReturn(entity);
		when(modelFactory.listaConteudos(any(JsonObject.class))).thenReturn(resultado);
		when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
		
		ResultadoBuscaMateria lista = editorial.getListaUltimasNoticias("viajeaqui");
		Assert.assertNotNull(lista);
		Assert.assertTrue(lista.getResultado().length!=0);
		
	}	
	
	@Test
	public void testGetMateriaExistente()  throws Exception {
		HttpEntity entity = mock(HttpEntity.class);
		String inputStream = "{\"titulo\":\"Conheca cinco agencias que fazem cruzeiros pelo Caribe\",\"subtitulo\":\"Veja as melhores opcoes para embarcar em um cruzeiro no Caribe\",\"meta_description\":\"Pacotes de agencias que realizam cruzeiros pelo Caribe, com destinos como Porto Rico, D\",\"status\":\"disponivel\",\"autor\":\"Redacao Viagem e Turismo\",\"chapeu\":\"AGENCIA - VIAGEM E TURISMO - EDICAO 230 - DEZEMBRO/2014\",\"fonte\":\"VIAGEM E TURISMO\",\"marca\":\"viajeaqui\",\"slug\":\"conheca-cinco-agencias-que-fazem-cruzeiros-pelo-caribe\",\"categorias\":[\"Servico e Roteiro\",\"Servico e Roteiro::Viagem\"],\"editorias\":[\"Materias\"],\"rotulos_controlados\":[],\"em_revisao\":null,\"corpo\":\"<p>\r\n\tO paraiso caribenho esta a um navio de distancia! Cinco agencias te levam em roteiros que passam pelas Ilhas Virgens Britanicas, Republica Dominicana, St. Maarten e outros destinos para curtir as aguas cristalinas do Caribe.</p>\r\n\",\"id\":\"http://editorial.api.abril.com.br/materias/id/547e50316b6c1236ad0001d0\",\"customizada\":false,\"tipo_recurso\":\"materia\",\"editoria_padrao\":\"Materias\",\"descricao_conteudo\":\"Conheca cinco agencias que fazem cruzeiros pelo Caribe\",\"idioma\":\"pt\",\"tags\":[\"cruzeiros\",\"Caribe\",\"MSC\",\"Seabourn\",\"Carnival Breeze\",\"Celebrity Summit\",\"Saint Maarten\",\"Bonaire\",\"Curacao\",\"Ilhas Virgens\",\"Florida\",\"Bahamas\",\"Porto Rico\",\"Republica Dominicana\",\"Regent\"],\"link\":[{\"href\":\"http://editorial.api.abril.com.br/materias/id/547e50316b6c1236ad0001d0\",\"rel\":\"self\",\"type\":\"application/json\"},{\"href\":\"http://editorial.api.abril.com.br/materias\",\"rel\":\"materias\",\"type\":\"application/json\"}],\"impresso\":{\"edicao\":\"230\",\"pagina_inicial\":null,\"data_de_edicao\":\"2014-12-01\",\"capa\":false,\"pagina_final\":null},\"criacao\":{\"usuario\":\"Renata Hirota\",\"data\":\"2014-12-02T23:50:09Z\"},\"ultima_atualizacao\":{\"usuario\":\"Renata Hirota\",\"data\":\"2014-12-03T00:20:35Z\"},\"disponibilizacao\":{\"usuario\":\"Renata Hirota\",\"data\":\"2014-12-02T23:50:09Z\"},\"conteudos_relacionados\":[{\"slug\": \"o-natal-chegou-veja-5-pacotes-para-curtir-as-festas-no-sul\",\"id\": \"http://editorial.api.abril.com.br/materias/id/547e161cb0c7144d12000018\",\"tipo_recurso\": \"materia\",\"titulo\": \"O Natal chegou! Veja 5 pacotes para curtir as festas no Sul\",\"link\": {\"href\": \"http://editorial.api.abril.com.br/materias/id/547e161cb0c7144d12000018\",\"rel\": \"materia\",\"type\": \"application/json\"}}],\"conteudos_embutidos\":[]}";
		Materia materia = createMateria();
		
		when(httpClientFactory.createHttpClient()).thenReturn(httpClient);
		when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK"));
		when(entity.getContent()).thenReturn(IOUtils.toInputStream(inputStream));
		when(response.getEntity()).thenReturn(entity);
		when(modelFactory.materia(any(JsonObject.class))).thenReturn(materia);
		when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
		when(jsonUtil.fromString(any(String.class))).thenReturn(new JsonObject());
		
		Materia materiaRest = editorial.getMateriaIdHash("A1B2C3D4E5");
		Assert.assertNotNull(materiaRest);
		Mockito.verify(httpClientFactory, Mockito.times(1)).createHttpClient();
		Mockito.verify(response, Mockito.times(1)).getStatusLine();
	}

	private Materia createMateria() {
	  Materia materia = new Materia();
		materia.setTitulo("oi");
		ArrayList<Conteudo> listaConteudo = createListaConteudo();
		materia.setConteudos(listaConteudo);
	  return materia;
  }

	private ArrayList<Conteudo> createListaConteudo() {
	  ArrayList<Conteudo> listaConteudo = new ArrayList<Conteudo>();
		Conteudo contudoMateria = createConteudoMateria();
		listaConteudo.add(contudoMateria);
	  return listaConteudo;
  }

	private Conteudo createConteudoMateria() {
	  Conteudo contudoMateria = new Conteudo();
		contudoMateria.setTipoRecurso(TipoRecursoEnum.MATERIA.toString());
	  return contudoMateria;
  }
	@Test(expected = InternalServerErrorException.class)
	public void testGetMateriaRespondendo500() throws Exception {
		when(httpClientFactory.createHttpClient()).thenReturn(httpClient);
		when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR, "SERVER ERROR"));
		when(httpClient.execute(any(HttpGet.class))).thenReturn(response);

		editorial.getMateriaIdHash("A1B2C3D4E5");
	}
	
	@Test(expected = ServiceUnavailableException.class)
	public void testEditorialIndisponivel() throws Exception {
		when(httpClientFactory.createHttpClient()).thenReturn(httpClient);
		when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
		when(httpClient.execute(any(HttpGet.class))).thenReturn(response);

		editorial.getMateriaIdHash("A1B2C3D4E5");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetMateriaIdNull() throws Exception {

		editorial.getMateriaIdHash(null);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testListaUltimasNoticiasMarcaNull() throws Exception {

		editorial.getListaUltimasNoticias(null);
	}
	
}













