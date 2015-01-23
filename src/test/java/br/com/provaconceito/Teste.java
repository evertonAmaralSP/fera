package br.com.provaconceito;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.abril.fera.support.tipos.TipoComponenteEnum;

public class Teste {

	
//	@Test
	public void data(){
		Date now = new GregorianCalendar(2014, Calendar.DECEMBER, 11, 0, 0, 0).getTime();
		Date maior = new GregorianCalendar(2014, Calendar.DECEMBER, 12, 0, 0, 0).getTime();
		Date menor = new GregorianCalendar(2014, Calendar.DECEMBER, 10, 0, 0, 0).getTime();

		assertTrue(now.after(menor));
		assertTrue(now.before(maior));
	}
	
	
//	@Test
	public void listEnum(){
		List<TipoComponenteEnum> list = new ArrayList<TipoComponenteEnum>(Arrays.asList(TipoComponenteEnum.values()));
		for (TipoComponenteEnum tipoComponenteEnum : list) {
	    System.out.println(tipoComponenteEnum);
    }
		TipoComponenteEnum obj = TipoComponenteEnum.get("manual");
		System.out.println(obj);
	}
	
}
