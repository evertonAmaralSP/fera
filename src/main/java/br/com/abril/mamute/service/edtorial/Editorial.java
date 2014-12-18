package br.com.abril.mamute.service.edtorial;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.com.abril.mamute.exception.editorial.InternalServerErrorException;
import br.com.abril.mamute.exception.editorial.ServiceUnavailableException;
import br.com.abril.mamute.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.mamute.model.Conteudo;
import br.com.abril.mamute.model.GaleriasMultimidia;
import br.com.abril.mamute.model.Imagem;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.support.date.DateUtils;
import br.com.abril.mamute.support.factory.HttpClientFactory;
import br.com.abril.mamute.support.factory.ModelFactory;
import br.com.abril.mamute.support.json.JsonUtil;
import br.com.abril.mamute.support.tipos.TipoRecursoEnum;

@Component
public class Editorial {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private HttpClientFactory httpClientFactory;
	@Autowired
	private JsonUtil jsonUtil;
	@Autowired
	private ModelFactory modelFactory;

	public Materia getMateriaIdHash(String idHash) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(idHash))
			throw new IllegalArgumentException("Atributo idHash não pode ser vazio.");
    try {
    	String url = EdtorialUrls.paramEstruturado(EdtorialUrls.MATERIA_ID, idHash);
	    return getMateriaId(url);
    } catch (URISyntaxException e) {
    	 logger.error("[getMateriaIdHash] erro Uri Syntax: {}", new Object[] {e.getMessage() });
    }
		return null;
	}
	public Materia getMateriaId(String url) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(url))
			throw new IllegalArgumentException("Atributo url não pode ser vazio.");
		String jsonString = buscaUrlRest(url);
		Materia materia = parseMateria(jsonString);
		return materia;
	}
	
	

	public ResultadoBuscaMateria getListaUltimasNoticias(String marca) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(marca))
			throw new IllegalArgumentException("Atributo marca não pode ser vazio.");

		try {
		  String url = EdtorialUrls.filterParam(EdtorialUrls.BUSCA_ULTIMAS_MATEIAS, "marca", marca);
		  url = EdtorialUrls.filterParam(url,EdtorialUrls.PER_PAGE , EdtorialUrls.NUMERO_ITEM_POR_PAGINA);
		  url = EdtorialUrls.filterOrder(url, EdtorialUrls.DATA_DISPONIBILIZACAO);
		  return getResultadoBuscaMateria(url);
    } catch (URISyntaxException e) {
	    logger.error("[getListaUltimasNoticias] erro Uri Syntax: {}", new Object[] {e.getMessage() });
    }
		return null;
	}
	
	public ResultadoBuscaMateria getListaRetroativaPorData(String url,Date date) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(url))
			throw new IllegalArgumentException("Atributo url não pode ser vazio.");

		try {
		  url = EdtorialUrls.filterParam(url,EdtorialUrls.PER_PAGE , EdtorialUrls.NUMERO_ITEM_POR_PAGINA);
		  url = EdtorialUrls.filterParam(url,EdtorialUrls.DATA_DISPONIBILIZACAO_INICIO , DateUtils.format(date));
		  url = EdtorialUrls.filterOrder(url, EdtorialUrls.DATA_DISPONIBILIZACAO);
		  return getResultadoBuscaMateria(url);
    } catch (URISyntaxException e) {
	    logger.error("[getListaUltimasNoticias] erro Uri Syntax: {}", new Object[] {e.getMessage() });
    }
		return null;
	}
	public ResultadoBuscaMateria getResultadoBuscaMateria(String url) throws ComunicacaoComEditorialException {
	  String jsonString = buscaUrlRest(url);
	  ResultadoBuscaMateria listaConteudos = modelFactory.listaConteudos(jsonUtil.fromString(jsonString));
	  return listaConteudos;
  }

	public ResultadoBuscaMateria getListaInSource(String urlSearch) throws ComunicacaoComEditorialException {
		String jsonString = buscaUrlRest(urlSearch);
		final ResultadoBuscaMateria listaConteudos = modelFactory.listaConteudos(jsonUtil.fromString(jsonString));
		return listaConteudos;
	}

	public String buscaUrlRest(String url) throws ComunicacaoComEditorialException {
		CloseableHttpClient httpClient = httpClientFactory.createHttpClient();
		try {
			HttpGet get = new HttpGet(url);
			get.setHeader(HTTP.CONTENT_TYPE, HttpClientFactory.APPLICATION_JSON_UTF8);
			CloseableHttpResponse response = httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case HttpStatus.SC_OK:
				logger.debug("[buscaMateriaId] Request : {} e Response: {} ", new Object[] { url, response.getEntity().getContent() });
				return IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				logger.error("[buscaMateriaId] erro interno na busca de materia (500) (id={} )", new Object[] { url });
				throw new InternalServerErrorException();
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				logger.error("[buscaMateriaId] servidor Editoria API indisponivel (503)");
				throw new ServiceUnavailableException();
			default:
				logger.error("[buscaMateriaId] erro nao identificado. HTTP Status code:{}", new Object[] { statusCode });
				throw new ComunicacaoComEditorialException();
			}
		} catch (IOException e) {
			logger.error("[buscaMateriaId] erro nao identificado: {}", new Object[] { e.getMessage() });
			throw new ComunicacaoComEditorialException();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("[buscaMateriaId] erro ao fechar conexao HTTP: {}", new Object[] { e.getMessage() });
			}
		}
	}

	private Materia parseMateria(String jsonString) throws ComunicacaoComEditorialException {
		Materia materia = modelFactory.materia(jsonUtil.fromString(jsonString));
		List<Conteudo> listaMateriasRelacionadas = new ArrayList<Conteudo>();
		List<Conteudo> conteudos = materia.getConteudos();
		if (!CollectionUtils.isEmpty(conteudos)) {
			for (Conteudo conteudo : conteudos) {
				if (conteudo.getTipoRecurso().equals(TipoRecursoEnum.MATERIA.toString())) {
					listaMateriasRelacionadas.add(conteudo);
				}
				if (conteudo.getTipoRecurso().equals(TipoRecursoEnum.IMAGEM.toString())) {
					String json = buscaUrlRest(conteudo.getId());
					Imagem imagem = modelFactory.imagem(jsonUtil.fromString(json));
					materia.setImagem(imagem);
				}
				if (conteudo.getTipoRecurso().equals(TipoRecursoEnum.GALERIA_MULTIMIDIA.toString())) {
					String json = buscaUrlRest(conteudo.getId());
					GaleriasMultimidia galeriasMultimidia = modelFactory.galeriasMultimidia(jsonUtil.fromString(json));
					materia.setGaleriasMultimidia(galeriasMultimidia);
				}
			}
		}
		materia.setMateriasRelacionadas(listaMateriasRelacionadas);
		return materia;
	}
}
