package br.com.abril.mamute.service.parser.bean;

import com.google.gson.JsonObject;

public class ConteudoRelacionado {

	private JsonObject conteudoRelacionado;

	public ConteudoRelacionado(JsonObject conteudoRelacionado) {
		this.conteudoRelacionado = conteudoRelacionado;
	}

	public ConteudoRelacionado() {
		this(null);
	}

	public String getCredito() {
		if(conteudoRelacionado == null) return "";
		return conteudoRelacionado.get("credito").getAsString();
	}

}
