package br.com.abril.mamute.model.editorial;

import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

public class ResultadoBuscaMateria extends RestModel {

	@SerializedName("total_resultados")
	private Integer totalResultados;

	@SerializedName("itens_por_pagina")
	private Integer itensPorPagina;

	@SerializedName("pagina_atual")
	private Integer paginaAtual;

	private String query;

	private Materia[] resultado;

	public Integer getTotalResultados() {
		return totalResultados;
	}

	public void setTotalResultados(Integer totalResultados) {
		this.totalResultados = totalResultados;
	}

	public Integer getItensPorPagina() {
		return itensPorPagina;
	}

	public void setItensPorPagina(Integer itensPorPagina) {
		this.itensPorPagina = itensPorPagina;
	}

	public Integer getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Materia[] getResultado() {
		return resultado;
	}

	public void setResultado(Materia[] newResultado) {
		this.resultado = (newResultado != null ? Arrays.copyOf(newResultado, newResultado.length) : null);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResultadoBuscaProduto [");
		if (totalResultados != null) {
			builder.append("totalResultados=").append(totalResultados).append(", ");
		}
		if (itensPorPagina != null) {
			builder.append("itensPorPagina=").append(itensPorPagina).append(", ");
		}
		if (paginaAtual != null) {
			builder.append("paginaAtual=").append(paginaAtual).append(", ");
		}
		if (query != null) {
			builder.append("query=").append(query);
		}
		builder.append("]");
		return builder.toString();
	}

}
