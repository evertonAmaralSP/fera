package br.com.abril.mamute.service.edtorial;

import java.io.IOException;
import java.nio.charset.Charset;

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

import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.support.factory.HttpClientFactory;
import br.com.abril.mamute.support.factory.ModelFactory;
import br.com.abril.mamute.support.json.JsonUtil;

@Component
public class Editorial {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClientFactory httpClientFactory;
	@Autowired
	private JsonUtil jsonUtil;
	@Autowired
	private ModelFactory modelFactory;

	public Materia getMateria(String id){

		CloseableHttpClient httpClient = httpClientFactory.createHttpClient();
		String jsonString=null;
		try {
			HttpGet get = new HttpGet(EdtorialUrls.MATERIA_ID + "/" + id);
			get.setHeader(HTTP.CONTENT_TYPE, HttpClientFactory.APPLICATION_JSON_UTF8);
			CloseableHttpResponse response = httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();

			switch (statusCode) {
			case HttpStatus.SC_OK:
				logger.debug("[buscaMateriaId] Request : {} e Response: {} ", new Object[]{ EdtorialUrls.MATERIA_ID + "/" + id,response.getEntity().getContent() });
				 jsonString = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
				 return modelFactory.materia(jsonUtil.fromString(jsonString));
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				logger.error("[buscaMateriaId] erro interno na busca de materia (500) (id={} )",new Object[]{ id });
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				logger.error("[buscaMateriaId] servidor Editoria API indisponivel (503)");
			default:
				logger.error("[buscaMateriaId] erro nao identificado. HTTP Status code:{}",new Object[]{ statusCode });
			}

		} catch (IOException e) {
			logger.error("[buscaMateriaId] erro nao identificado: {}",new Object[]{ e.getMessage() });
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("[buscaMateriaId] erro ao fechar conexao HTTP: {}",new Object[]{ e.getMessage() });
			}
		}
		return null;
	}

	public ResultadoBuscaMateria getListaUltimasNoticias(String marca) {
		CloseableHttpClient httpClient = httpClientFactory.createHttpClient();
		String jsonString=null;
		try {
			String urlSearch = EdtorialUrls.filter(EdtorialUrls.BUSCA_ULTIMAS_MATEIAS + "?" + marca,EdtorialUrls.DATA_DISPONIBILIZACAO);
			HttpGet get = new HttpGet(urlSearch);
			get.setHeader(HTTP.CONTENT_TYPE, HttpClientFactory.APPLICATION_JSON_UTF8);
			CloseableHttpResponse response = httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();

			switch (statusCode) {
			case HttpStatus.SC_OK:
				logger.debug("[buscaUltimasMateria] Request : {} e Response: {} ", new Object[]{ urlSearch, response.getEntity().getContent() });
				 jsonString = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
				 return modelFactory.listaConteudos(jsonUtil.fromString(jsonString));
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				logger.error("[buscaUltimasMateria] erro interno na busca ultimas materia (500) (marca={} )",new Object[]{ marca });
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				logger.error("[buscaUltimasMateria] servidor Editoria API indisponivel (503)");
			default:
				logger.error("[buscaUltimasMateria] erro nao identificado. HTTP Status code:{}",new Object[]{ statusCode });
			}
		} catch (IOException e) {
			logger.error("[buscaUltimasMateria] erro nao identificado: {}",new Object[]{ e.getMessage() });
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("[buscaUltimasMateria] erro ao fechar conexao HTTP: {}",new Object[]{ e.getMessage() });
			}

		}
		return null;

  }


	public ResultadoBuscaMateria getListaInSource(String urlSearch) {
		CloseableHttpClient httpClient = httpClientFactory.createHttpClient();
		String jsonString=null;
		try {
			HttpGet get = new HttpGet(urlSearch);
			get.setHeader(HTTP.CONTENT_TYPE, HttpClientFactory.APPLICATION_JSON_UTF8);
			CloseableHttpResponse response = httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();

			switch (statusCode) {
			case HttpStatus.SC_OK:
				logger.debug("[buscaUltimasMateria] Request : {} e Response: {} ", new Object[]{ urlSearch, response.getEntity().getContent() });
				 jsonString = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
				 return modelFactory.listaConteudos(jsonUtil.fromString(jsonString));
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				logger.error("[buscaUltimasMateria] erro interno na busca url: {} (500)",new Object[]{ urlSearch });
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				logger.error("[buscaUltimasMateria] servidor Editoria API indisponivel (503)");
			default:
				logger.error("[buscaUltimasMateria] erro nao identificado. HTTP Status code:{}",new Object[]{ statusCode });
			}
		} catch (IOException e) {
			logger.error("[buscaUltimasMateria] erro nao identificado: {}",new Object[]{ e.getMessage() });
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("[buscaUltimasMateria] erro ao fechar conexao HTTP: {}",new Object[]{ e.getMessage() });
			}

		}
		return null;
	}

}
