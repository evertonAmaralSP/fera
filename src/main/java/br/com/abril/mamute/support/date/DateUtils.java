package br.com.abril.mamute.support.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static final String FORMAT_PT_BR = "dd/MM/yyyy";

	public static String format(Date date, String format) {

		SimpleDateFormat fmt = new SimpleDateFormat(format);
		String dateFormatted = fmt.format(date);
		return dateFormatted;
	}

	public static Date formataData(String data, String format) {
		if (data == null || data.equals(""))
			return null;

		Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			date = formatter.parse(data);
		} catch (ParseException e) {
			
		}
		return date;
	}

	public static Date formataData(String data) {
		return formataData(data,FORMAT_PT_BR);
	}
	public static String format(Date date) {
		return format(date,FORMAT_PT_BR);
	}
}
