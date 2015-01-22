package br.com.abril.mamute.model.editorial;

import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import com.google.gson.annotations.SerializedName;

public class Imagem extends RestModel {

	private String id;
	private String slug;
	private String titulo;
	@SerializedName("tipo_recurso")
	private String tipoRecurso;
	@SerializedName("descricao_conteudo")
	private String descricaoConteudo;
	@SerializedName("meta_description")
	private String metaDescription;
	private String status;
	private String descricao;
	private String credito;
	private String fonte;
	private String marca;
	private List<String> categorias;
	private List<String> editorias;
	@SerializedName("rotulos_controlados")
	private List<String> rotulosControlados;
	private List<String> tags;
	private DataEditoria criacao;
	@SerializedName("ultima_atualizacao")
	private DataEditoria ultimaAtualizacao;
	private DataEditoria disponibilizacao;
	@SerializedName("data_disponibilizacao")
	private Date dataDisponibilizacao;
	private Transformacoes transformacoes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}
	public String getDescricaoConteudo() {
		return descricaoConteudo;
	}
	public void setDescricaoConteudo(String descricaoConteudo) {
		this.descricaoConteudo = descricaoConteudo;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getCredito() {
		return credito;
	}
	public String creditoTextPure() {
		if(!StringUtils.isEmpty(credito)){
			Document doc = Jsoup.parse(credito);
			return doc.text();
		}
		return "";
	}
	public void setCredito(String credito) {
		this.credito = credito;
	}
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public List<String> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}
	public List<String> getEditorias() {
		return editorias;
	}
	public void setEditorias(List<String> editorias) {
		this.editorias = editorias;
	}
	public List<String> getRotulosControlados() {
		return rotulosControlados;
	}
	public void setRotulosControlados(List<String> rotulosControlados) {
		this.rotulosControlados = rotulosControlados;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public DataEditoria getCriacao() {
		return criacao;
	}
	public void setCriacao(DataEditoria criacao) {
		this.criacao = criacao;
	}
	public DataEditoria getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}
	public void setUltimaAtualizacao(DataEditoria ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}
	public DataEditoria getDisponibilizacao() {
		return disponibilizacao;
	}
	public void setDisponibilizacao(DataEditoria disponibilizacao) {
		this.disponibilizacao = disponibilizacao;
	}
	public Date getDataDisponibilizacao() {
		return dataDisponibilizacao;
	}
	public void setDataDisponibilizacao(Date dataDisponibilizacao) {
		this.dataDisponibilizacao = dataDisponibilizacao;
	}
	public Transformacoes getTransformacoes() {
		return transformacoes;
	}
	public void setTransformacoes(Transformacoes transformacoes) {
		this.transformacoes = transformacoes;
	}
	
}
