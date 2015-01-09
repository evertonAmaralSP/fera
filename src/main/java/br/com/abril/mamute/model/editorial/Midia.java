package br.com.abril.mamute.model.editorial;

import org.springframework.util.StringUtils;

import com.google.gson.annotations.SerializedName;

public class Midia {
	private static final String ALEXANDRIA = "alexandria";
	private static final String THUMBNAIL_240X240 = "D5mu";
	private static final String FOTO_GALERIA_MATERIA_620 = "5tY2z";
	private static final String THUMB_GALERIA_72 = "5pLtx";

	private String id;
	private String slug;
	private String titulo;
	@SerializedName("tipo_recurso")
	private String tipoRecurso;
	private String descricao;
	private String credito;
	private String preview;
	private Link link;
	
	public String fotoGaleriaMateria620(String marca){
		return replaceUrlImage(THUMBNAIL_240X240, FOTO_GALERIA_MATERIA_620,marca);
	}
  public String thumbGaleria72(String marca){
		return replaceUrlImage(THUMBNAIL_240X240, THUMB_GALERIA_72,marca);
  }
  public String thumbnail60x60(String marca){
		return replaceUrlImage(THUMBNAIL_240X240, FOTO_GALERIA_MATERIA_620,marca);
	}
  public String thumbnail240x240(String marca){
		return replaceUrlMarca(marca,preview);
  }
  
  public String replaceUrlImage(String marcacaoAtual, String marcacaoNova, String marca){
		String url = preview.replace(marcacaoAtual, marcacaoNova);
		url = replaceUrlMarca(marca, url);
		return url;
  }
	private String replaceUrlMarca(String marca, String url) {
	  url = StringUtils.isEmpty(marca) ? url : url.replace(ALEXANDRIA, marca) ;
	  return url;
  }
	
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
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
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
	public void setCredito(String credito) {
		this.credito = credito;
	}
	public String getPreview() {
		return preview;
	}
	public void setPreview(String preview) {
		this.preview = preview;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
}
