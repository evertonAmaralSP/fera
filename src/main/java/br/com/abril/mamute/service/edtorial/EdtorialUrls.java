package br.com.abril.mamute.service.edtorial;

import br.com.abril.mamute.config.SystemConfiguration;

public class EdtorialUrls {

	private EdtorialUrls() {
	}

	private static final String EDTORIAL_BASE_URI = SystemConfiguration.getPropertyAsString(SystemConfiguration.EDTORIAL_BASE_URI);



	public static final String DATA_DISPONIBILIZACAO = "data_disponibilizacao";

	public static final String BUSCA_PATH = "/busca";
	public static final String ID_PATH = "/id";

	public static final String MATERIAS_PATH = "/materias";

	public static final String MATERIA_ID = EDTORIAL_BASE_URI + MATERIAS_PATH + ID_PATH;


	public static final String BUSCA_ULTIMAS_MATEIAS = EDTORIAL_BASE_URI + MATERIAS_PATH + BUSCA_PATH;

	public static String filter(String filtro, String param){
		if(filtro.matches("([/?].*)?")){
			filtro = filtro + "?" + "order="+param;
		} else {
			filtro = filtro + "&" + "order="+param;
		}

		return filtro;
	}
}
