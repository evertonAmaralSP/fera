package br.com.abril.fera.service.edtorial;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.com.abril.fera.exception.editorial.GomeException;
import br.com.abril.fera.exception.editorial.InternalServerErrorException;
import br.com.abril.fera.exception.editorial.ServiceUnavailableException;
import br.com.abril.fera.exception.editorial.base.ComunicacaoComEditorialException;
import br.com.abril.fera.model.editorial.Conteudo;
import br.com.abril.fera.model.editorial.GaleriasMultimidia;
import br.com.abril.fera.model.editorial.Imagem;
import br.com.abril.fera.model.editorial.Materia;
import br.com.abril.fera.model.editorial.ResultadoBuscaMateria;
import br.com.abril.fera.support.date.DateUtils;
import br.com.abril.fera.support.factory.HttpClientFactory;
import br.com.abril.fera.support.factory.ModelFactory;
import br.com.abril.fera.support.json.JsonUtil;
import br.com.abril.fera.support.tipos.TipoRecursoEnum;

@Component
public class Editorial {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private HttpClientFactory httpClientFactory;
	@Autowired
	private JsonUtil jsonUtil;
	@Autowired
	private ModelFactory modelFactory;
	@Autowired
	private EdtorialUrls edtorialUrls;

	public Materia getMateriaIdHash(String idHash) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(idHash))
			throw new IllegalArgumentException("Atributo idHash não pode ser vazio.");
    try {
    	String url = edtorialUrls.paramEstruturado(edtorialUrls.MATERIA_ID, idHash);
	    return getMateriaId(url);
    } catch (URISyntaxException e) {
    	 logger.error("[getMateriaIdHash] erro Uri Syntax: {}", new Object[] {e.getMessage() });
    }
		return null;
	}
	public Materia getMateriaId(String url,boolean eager) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(url))
			throw new IllegalArgumentException("Atributo url não pode ser vazio.");
		
		final Materia materiaId = getMateriaId(url);
		if(eager) {
			instrumentarMateriasRelacionadas(materiaId);
		}
		return materiaId;
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
		  String url = edtorialUrls.filterParam(edtorialUrls.getBuscaUltimasMateias(), "marca", marca);
		  url = edtorialUrls.filterParam(url,edtorialUrls.PER_PAGE , edtorialUrls.NUMERO_ITEM_POR_PAGINA);
		  url = edtorialUrls.filterOrder(url, edtorialUrls.DATA_DISPONIBILIZACAO);
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
		  url = edtorialUrls.filterParam(url,edtorialUrls.PER_PAGE , edtorialUrls.NUMERO_ITEM_POR_PAGINA);
		  url = edtorialUrls.filterParam(url,edtorialUrls.DATA_ULTIMA_DISPONIBILIZACAO_INICIO , DateUtils.format(date));
		  url = edtorialUrls.filterOrder(url, edtorialUrls.DATA_DISPONIBILIZACAO);
		  System.out.println(url);
		  return getResultadoBuscaMateria(url);
    } catch (URISyntaxException e) {
	    logger.error("[getListaRetroativaPorData] erro Uri Syntax: {}", new Object[] {e.getMessage() });
    }
		return null;
	}
	public ResultadoBuscaMateria getListaConsultaSlug(String url,String slug) throws ComunicacaoComEditorialException {
		if (StringUtils.isEmpty(url))
			throw new IllegalArgumentException("Atributo url não pode ser vazio.");

		try {
		  url = edtorialUrls.filterParam(url,edtorialUrls.SLUG , slug);
		  return getResultadoBuscaMateria(url);
    } catch (URISyntaxException e) {
	    logger.error("[getListaConsultaSlug] erro Uri Syntax: {}", new Object[] {e.getMessage() });
    }
		return null;
	}
	public ResultadoBuscaMateria getResultadoBuscaMateria(String url) throws ComunicacaoComEditorialException {
	  String jsonString = buscaUrlRest(url);
	  ResultadoBuscaMateria listaConteudos = modelFactory.listaConteudos(jsonUtil.fromString(jsonString));
	  return listaConteudos;
  }

	public ResultadoBuscaMateria getListaInSource(String urlSearch, Date ultimaAtualizacao) throws ComunicacaoComEditorialException, URISyntaxException {
		if(ultimaAtualizacao != null ){
			urlSearch = edtorialUrls.filterParam(urlSearch, edtorialUrls.DATA_ULTIMA_DISPONIBILIZACAO_INICIO , DateUtils.format(ultimaAtualizacao));
			urlSearch = edtorialUrls.filterParam(urlSearch,edtorialUrls.PER_PAGE , edtorialUrls.NUMERO_ITEM_POR_PAGINA);
		}		
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
			case HttpStatus.SC_GONE:
				logger.error("[buscaMateriaId] servidor Editoria API indisponivel (410)");
				throw new GomeException();
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
	public List<Materia> listaMaterias(ResultadoBuscaMateria resultadoBuscaConteudo) throws ComunicacaoComEditorialException {
		List<Materia> l = new ArrayList<Materia>();
	  
		if(temPaginacaoNoResultado(resultadoBuscaConteudo) ){
			String linkUltimaPagina = recuperarLinkUltimaPagina(resultadoBuscaConteudo);
			int ultimaPagina = recuperarNumeroUltimaPagina(linkUltimaPagina);
			for (int index = ultimaPagina; index >= 2 ; index--) {
				String url = generateUrlPaginada(resultadoBuscaConteudo.getQuery(), index);
		    
				ResultadoBuscaMateria resultado = getResultadoBuscaMateria(url);
		    addListaMateria(l, resultado.getResultado());
	    } 
		}
	  addListaMateria(l, resultadoBuscaConteudo.getResultado());
	  return l;
  }
	private void addListaMateria(List<Materia> l, Materia[] materias) {
	  for (Materia m : materias) {
	    l.add(m);
	  }
  }
	private String generateUrlPaginada(String query, int index) throws ComunicacaoComEditorialException {
	  String url = edtorialUrls.getBuscaUltimasMateias();
	  try {
	    url = edtorialUrls.paramQuery(url, query);
	    url = edtorialUrls.filterParam(url, "pw", index+"");
	  } catch (URISyntaxException e) {
	    logger.error("[listaMaterias] erro ao recuperar query compor query uri: {}", new Object[] { e.getMessage() });
	    throw new ComunicacaoComEditorialException();
    }
	  return url;
  }
	private boolean temPaginacaoNoResultado(ResultadoBuscaMateria resultadoBuscaConteudo) {
	  return resultadoBuscaConteudo.getTotalResultados().intValue() > resultadoBuscaConteudo.getItensPorPagina().intValue();
  }
	private String recuperarLinkUltimaPagina(ResultadoBuscaMateria resultadoBuscaConteudo) {
		if (resultadoBuscaConteudo.getLinkByRel("ultima")!=null)
	    return resultadoBuscaConteudo.getLinkByRel("ultima").getHref();
    else
	    return null;
  }
	private int recuperarNumeroUltimaPagina(String linkUltimaPagina) throws ComunicacaoComEditorialException {
	  int ultimaPagina = 1;
	  List<NameValuePair> listaParam;
    try {
	    listaParam = new URIBuilder(linkUltimaPagina).getQueryParams();
	    for (NameValuePair nameValuePair : listaParam) {
		    if("pw".equals(nameValuePair.getName())) {
		    	return ultimaPagina = Integer.parseInt(nameValuePair.getValue());
		    }
		  }
    } catch (URISyntaxException e) {
	    logger.error("[listaMaterias] erro ao recuperar query ultimas pagina: {}", new Object[] { e.getMessage() });
	    throw new ComunicacaoComEditorialException();
    }
	  
	  return ultimaPagina;
  }

	private Materia parseMateria(String jsonString) throws ComunicacaoComEditorialException  {
		Materia materia = modelFactory.materia(jsonUtil.fromString(jsonString));
		
		if (!CollectionUtils.isEmpty(materia.getConteudos())) {
			for (Conteudo conteudo : materia.getConteudos()) {
				if (conteudo.getTipoRecurso().equals(TipoRecursoEnum.IMAGEM.toString())) {
					String descricao = conteudo.getDescricao();
					String json = buscaUrlRest(conteudo.getId());
					Imagem imagem = modelFactory.imagem(jsonUtil.fromString(json));
					imagem.setDescricao(descricao);
					materia.setImagem(imagem);
				}
				if (conteudo.getTipoRecurso().equals(TipoRecursoEnum.GALERIA_MULTIMIDIA.toString())) {
					String json = buscaUrlRest(conteudo.getId());
					GaleriasMultimidia galeriasMultimidia = modelFactory.galeriasMultimidia(jsonUtil.fromString(json));
					materia.setGaleriasMultimidia(galeriasMultimidia);
				}
			}
		}
		return materia;
	}
	private void instrumentarMateriasRelacionadas(Materia materia)  throws ComunicacaoComEditorialException {
		List<Materia> listaMateriasRelacionadas = new ArrayList<Materia>();
		List<Conteudo> conteudos = materia.getConteudos();
		if (!CollectionUtils.isEmpty(conteudos)) {
			for (Conteudo conteudo : conteudos) {
				if (conteudo.getTipoRecurso().equals(TipoRecursoEnum.MATERIA.toString())) {
					Materia materiaTmp = getMateriaId(conteudo.getId());
					listaMateriasRelacionadas.add(materiaTmp);
				}
			}
		}
		materia.setMateriasRelacionadas(listaMateriasRelacionadas);
	}
}
