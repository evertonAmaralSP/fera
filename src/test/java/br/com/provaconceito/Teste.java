package br.com.provaconceito;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
}
