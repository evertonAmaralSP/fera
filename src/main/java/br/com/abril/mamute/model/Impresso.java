package br.com.abril.mamute.model;

import com.google.gson.annotations.SerializedName;

public class Impresso {
	private String edicao;
	@SerializedName("pagina_inicial")
	private String paginaInicial;
	@SerializedName("data_de_edicao")
	private String dataDeEdicao;
	private String capa;
	@SerializedName("pagina_final")
	private String paginaFinal;
	public String getEdicao() {
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	public String getPaginaInicial() {
		return paginaInicial;
	}
	public void setPaginaInicial(String paginaInicial) {
		this.paginaInicial = paginaInicial;
	}
	public String getDataDeEdicao() {
		return dataDeEdicao;
	}
	public void setDataDeEdicao(String dataDeEdicao) {
		this.dataDeEdicao = dataDeEdicao;
	}
	public String getCapa() {
		return capa;
	}
	public void setCapa(String capa) {
		this.capa = capa;
	}
	public String getPaginaFinal() {
		return paginaFinal;
	}
	public void setPaginaFinal(String paginaFinal) {
		this.paginaFinal = paginaFinal;
	}

}
