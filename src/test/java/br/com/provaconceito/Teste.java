package br.com.provaconceito;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Teste {

	
	@Test
	public void data(){
		Date now = new GregorianCalendar(2014, Calendar.DECEMBER, 11, 0, 0, 0).getTime();
		Date maior = new GregorianCalendar(2014, Calendar.DECEMBER, 12, 0, 0, 0).getTime();
		Date menor = new GregorianCalendar(2014, Calendar.DECEMBER, 10, 0, 0, 0).getTime();

		assertTrue(now.after(menor));
		assertTrue(now.before(maior));
	}
	
	@Test
	public void maps(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("1", "Ola");

		System.out.println(map.get("1")!=null);
		System.out.println(map.get(null));
		System.out.println(map.get("2")!=null);
		
	}
}
