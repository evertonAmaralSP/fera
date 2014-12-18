package br.com.abril.mamute.service.edtorial;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import br.com.abril.mamute.config.SystemConfiguration;

public class EdtorialUrls {

	private EdtorialUrls() {
	}

	private static final String EDITORIAL_BASE_URI = SystemConfiguration.getPropertyAsString(SystemConfiguration.EDITORIAL_BASE_URI);
	public static final String NUMERO_ITEM_POR_PAGINA = SystemConfiguration.getPropertyAsString(SystemConfiguration.EDITORIAL_NUMERO_ITEM_POR_PAGINA);

	public static final String DATA_DISPONIBILIZACAO = "data_disponibilizacao";
	public static final String DATA_DISPONIBILIZACAO_INICIO = "data_disponibilizacao_inicio";

	public static final String BUSCA_PATH = "/busca";
	public static final String ID_PATH = "/id"; 
	
	public static final String MATERIAS_PATH = "/materias";
	public static final String PER_PAGE = "per_page";
	
	public static final String MATERIA_ID = EDITORIAL_BASE_URI + MATERIAS_PATH + ID_PATH;


	public static final String BUSCA_ULTIMAS_MATEIAS = EDITORIAL_BASE_URI + MATERIAS_PATH + BUSCA_PATH;

	public static String filterOrder(String url, String param) throws URISyntaxException {
		 return filterParam(url,"order",param);
	}
	public static String filterParam(String url,String param, String value) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url).addParameter(param, value);
		URI uri = uriBuilder.build();
		
		return uri.toString();
	}
	
	public static String paramEstruturado(String url, String value) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		uriBuilder.setPath( uriBuilder.getPath() + "/" + value);

		return uriBuilder.toString();
	}
	@SuppressWarnings("deprecation")
  public static String paramQuery(String url, String value) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		uriBuilder.setQuery(value);  
		return uriBuilder.toString();
	}
	
//	public static String buscaParamQuery(String url, String param) throws URISyntaxException {
//		UriBuilder uri = UriBuilder.fromPath(url).`
//
//		return uriBuilder.toString();
//	}
}
