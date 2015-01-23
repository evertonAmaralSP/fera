package br.com.abril.fera.support.factory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;

import br.com.abril.fera.model.editorial.Materia;
import br.com.abril.fera.model.editorial.ResultadoBuscaMateria;
import br.com.abril.fera.support.factory.ModelFactory;
import br.com.abril.fera.support.json.JsonUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@RunWith(PowerMockRunner.class)
public class ModelFactoryTest {

	@Mock
	private JsonUtil jsonUtil;

	@Mock
	private Logger logger;

	@InjectMocks
	private ModelFactory modelFactory;

	@Test
	public void testMateriaDeveRetornarUmObjetoMateriaQuandoReceberUmJsonValido() {
		String jsonAsString = "{\"titulo\":\"Conheca cinco agencias que fazem cruzeiros pelo Caribe\",\"subtitulo\":\"Veja as melhores opcoes para embarcar em um cruzeiro no Caribe\",\"meta_description\":\"Pacotes de agencias que realizam cruzeiros pelo Caribe, com destinos como Porto Rico, D\",\"status\":\"disponivel\",\"autor\":\"Redacao Viagem e Turismo\",\"chapeu\":\"AGENCIA - VIAGEM E TURISMO - EDICAO 230 - DEZEMBRO/2014\",\"fonte\":\"VIAGEM E TURISMO\",\"marca\":\"viajeaqui\",\"slug\":\"conheca-cinco-agencias-que-fazem-cruzeiros-pelo-caribe\",\"categorias\":[\"Servico e Roteiro\",\"Servico e Roteiro::Viagem\"],\"editorias\":[\"Materias\"],\"rotulos_controlados\":[],\"em_revisao\":null,\"corpo\":\"<p>\r\n\tO paraiso caribenho esta a um navio de distancia! Cinco agencias te levam em roteiros que passam pelas Ilhas Virgens Britanicas, Republica Dominicana, St. Maarten e outros destinos para curtir as aguas cristalinas do Caribe.</p>\r\n\",\"id\":\"http://editorial.api.abril.com.br/materias/id/547e50316b6c1236ad0001d0\",\"customizada\":false,\"tipo_recurso\":\"materia\",\"editoria_padrao\":\"Materias\",\"descricao_conteudo\":\"Conheca cinco agencias que fazem cruzeiros pelo Caribe\",\"idioma\":\"pt\",\"tags\":[\"cruzeiros\",\"Caribe\",\"MSC\",\"Seabourn\",\"Carnival Breeze\",\"Celebrity Summit\",\"Saint Maarten\",\"Bonaire\",\"Curacao\",\"Ilhas Virgens\",\"Florida\",\"Bahamas\",\"Porto Rico\",\"Republica Dominicana\",\"Regent\"],\"link\":[{\"href\":\"http://editorial.api.abril.com.br/materias/id/547e50316b6c1236ad0001d0\",\"rel\":\"self\",\"type\":\"application/json\"},{\"href\":\"http://editorial.api.abril.com.br/materias\",\"rel\":\"materias\",\"type\":\"application/json\"}],\"impresso\":{\"edicao\":\"230\",\"pagina_inicial\":null,\"data_de_edicao\":\"2014-12-01\",\"capa\":false,\"pagina_final\":null},\"criacao\":{\"usuario\":\"Renata Hirota\",\"data\":\"2014-12-02T23:50:09Z\"},\"ultima_atualizacao\":{\"usuario\":\"Renata Hirota\",\"data\":\"2014-12-03T00:20:35Z\"},\"disponibilizacao\":{\"usuario\":\"Renata Hirota\",\"data\":\"2014-12-02T23:50:09Z\"},\"conteudos_relacionados\":[],\"conteudos_embutidos\":[]}";
		JsonObject materiaAsJson = new Gson().fromJson(jsonAsString, JsonObject.class);

		Mockito.when(jsonUtil.getJsonHandler()).thenReturn(new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create());

		Materia materia = modelFactory.materia(materiaAsJson);

		Assert.assertNotNull(materia);
		Assert.assertEquals("conheca-cinco-agencias-que-fazem-cruzeiros-pelo-caribe", materia.getSlug());
	}

	@Test(expected=IllegalStateException.class)
	public void testMateriaDeveLancarIllegalStateExceptionQuandoJsonNaoTiverIdMateria() {
		String jsonAsString = "{\"chave\":\"valor\"}";
		JsonObject materiaAsJson = new Gson().fromJson(jsonAsString, JsonObject.class);

		Mockito.when(jsonUtil.getJsonHandler()).thenReturn(new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create());

		modelFactory.materia(materiaAsJson);
	}

	@Test
	public void testResultadoBuscaMateriaDeveRetornarUmObjetoResultadoBuscaMateriaQuandoReceberUmJsonValido() {
		String jsonAsString = "{\"titulo\":\"Dominio Editorial - Resultado de busca\",\"total_resultados\":1,\"pagina_atual\":1,\"itens_por_pagina\":10,\"query\":\"marca=viajeaqui\",\"link\":[{\"rel\":\"self\",\"href\":\"http://editorial.api.abril.com.br/materias/busca?marca=viajeaqui\",\"type\":\"application/json\"},{\"rel\":\"search\",\"href\":\"http://editorial.api.abril.com.br/materias/busca/descritor\",\"type\":\"application/opensearchdescription+xml\"},{\"rel\":\"ultima\",\"href\":\"http://editorial.api.abril.com.br/materias/busca?marca=viajeaqui&pw=625\",\"type\":\"application/json\"},{\"rel\":\"proxima\",\"href\":\"http://editorial.api.abril.com.br/materias/busca?marca=viajeaqui&pw=2\",\"type\":\"application/json\"}],\"resultado\":[{\"slug\":\"roteiro-rodoviario-serra-gaucha\",\"fonte\":\"GUIA QUATRO RODAS\",\"titulo\":\"Confira um roteiro de carro pela Serra Gaucha\",\"chapeu\":\"Roteiro Rodoviario\",\"subtitulo\":\"Cidadezinhas charmosas, vinicolas, restaurantes e belas paisagens: eis o cenario que acompanha a travessia pelo interior do Rio Grande do Sul\",\"id\":\"http://editorial.api.abril.com.br/materias/id/4e7915a775c627222d000012\",\"data_ultima_atualizacao\":\"2014-12-09T23:03:33Z\",\"tipo_recurso\":\"materia\",\"idioma\":\"pt\",\"customizada\":false,\"data_criacao\":\"2011-09-20T22:37:28Z\",\"data_disponibilizacao\":\"2011-09-20T22:37:28Z\",\"link\":{\"href\":\"http://editorial.api.abril.com.br/materias/id/4e7915a775c627222d000012\",\"rel\":\"materia\",\"type\":\"application/json\"}}]}";
		JsonObject resultadoBuscaUsuarioAsJson = new Gson().fromJson(jsonAsString, JsonObject.class);

		Mockito.when(jsonUtil.getJsonHandler()).thenReturn(new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create());

		ResultadoBuscaMateria resultadoBuscaMateria = modelFactory.listaConteudos(resultadoBuscaUsuarioAsJson);

		Assert.assertNotNull(resultadoBuscaMateria);
		Assert.assertEquals(1, resultadoBuscaMateria.getResultado().length);
		Assert.assertEquals(new Integer(1), resultadoBuscaMateria.getTotalResultados());
		Assert.assertEquals(new Integer(10), resultadoBuscaMateria.getItensPorPagina());
		Assert.assertEquals(new Integer(1), resultadoBuscaMateria.getPaginaAtual());
	}

}
