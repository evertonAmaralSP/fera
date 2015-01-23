package br.com.abril.fera.support.errors;

public class TemplateErrors {
	
	private String titulo;
	private String corpo;
	
	
	public TemplateErrors(String titulo, String corpo) {
	  this.titulo = titulo;
	  this.corpo = corpo;
  }
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getCorpo() {
		return corpo;
	}
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}
	
	

}
