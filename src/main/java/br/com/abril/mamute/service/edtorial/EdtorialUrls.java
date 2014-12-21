package br.com.abril.mamute.service.edtorial;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class EdtorialUrls {


	@Value("${editorial.base.uri}")
	private String EDITORIAL_BASE_URI;
	@Value("${editorial.numero.item.por.pagina}")
	public String NUMERO_ITEM_POR_PAGINA;

	public final String DATA_DISPONIBILIZACAO = "data_disponibilizacao";
	public final String DATA_DISPONIBILIZACAO_INICIO = "data_disponibilizacao_inicio";
	public final String SLUG = "slug";

	public final String BUSCA_PATH = "/busca";
	public final String ID_PATH = "/id"; 
	
	public final String MATERIAS_PATH = "/materias";
	public final String PER_PAGE = "per_page";
	
	public final String MATERIA_ID = EDITORIAL_BASE_URI + MATERIAS_PATH + ID_PATH;


	public final String BUSCA_ULTIMAS_MATEIAS = EDITORIAL_BASE_URI + MATERIAS_PATH + BUSCA_PATH;

	public String filterOrder(String url, String param) throws URISyntaxException {
		 return filterParam(url,"order",param);
	}
	public String filterParam(String url,String param, String value) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url).addParameter(param, value);
		URI uri = uriBuilder.build();
		
		return uri.toString();
	}
	
	public String paramEstruturado(String url, String value) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		uriBuilder.setPath( uriBuilder.getPath() + "/" + value);

		return uriBuilder.toString();
	}
	@SuppressWarnings("deprecation")
  public String paramQuery(String url, String value) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		uriBuilder.setQuery(value);  
		return uriBuilder.toString();
	}
	public String getEDITORIAL_BASE_URI() {
		return EDITORIAL_BASE_URI;
	}
	public void setEDITORIAL_BASE_URI(String eDITORIAL_BASE_URI) {
		EDITORIAL_BASE_URI = eDITORIAL_BASE_URI;
	}
	public String getNUMERO_ITEM_POR_PAGINA() {
		return NUMERO_ITEM_POR_PAGINA;
	}
	public void setNUMERO_ITEM_POR_PAGINA(String nUMERO_ITEM_POR_PAGINA) {
		NUMERO_ITEM_POR_PAGINA = nUMERO_ITEM_POR_PAGINA;
	}
	
}
