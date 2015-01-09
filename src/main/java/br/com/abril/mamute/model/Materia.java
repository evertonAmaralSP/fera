package br.com.abril.mamute.model;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.gson.annotations.SerializedName;

public class Materia extends RestModel {

	private String titulo;
	private String subtitulo;
	@SerializedName("meta_description")
	private String metaDescription;
	private String status;
	private String autor;
	private String chapeu;
	private String fonte;
	private String marca;
	private String slug;
	@SerializedName("em_revisao")
	private String emRevisao;
	private String corpo;
	private String id;
	private String customizada;
	@SerializedName("tipo_recurso")
	private String tipoRecurso;
	@SerializedName("editoria_padrao")
	private String editoriaPadrao;
	@SerializedName("descricao_conteudo")
	private String descricaoConteudo;
	private String idioma;
	private Impresso impresso;
	private DataEditoria criacao;
	@SerializedName("ultima_atualizacao")
	private DataEditoria ultimaAtualizacao;
	private DataEditoria disponibilizacao;
	@SerializedName("data_disponibilizacao")
	private Date dataDisponibilizacao;
	private List<String> categorias;
	private List<String> editorias;
	@SerializedName("rotulos_controlados")
	private List<String> rotulosControlados;
	private List<String> tags;
	@SerializedName("conteudos_relacionados")
	private List<Conteudo> conteudos;
	private List<Materia> materiasRelacionadas;
	private Imagem imagem;
	private GaleriasMultimidia galeriasMultimidia;
	

	public String descricaoSubTitle(){
		int maxCaracter = 98;
		if (StringUtils.isEmpty(subtitulo)) return "";
		if (subtitulo.length() <= maxCaracter) maxCaracter = subtitulo.length();
		return subtitulo.substring(0,maxCaracter).replaceAll("\\<.*?>","") + "...";
	}
	public String capa190x250(){
		if(imagem != null) {
			return imagem.getTransformacoes().getCapa190x250().linkExtreno(marca);
		} 
		return "";
	}
	
	public String listaDestinos80(){
		if(imagem != null) {
			return imagem.getTransformacoes().getListaDestinos80().linkExtreno(marca);
		} 
		return "";
	}
	public String descricaoTrunc(){
		int maxCaracter = 290;
		if (StringUtils.isEmpty(corpo)) return "";
		if (corpo.length() <= maxCaracter) maxCaracter = corpo.length();
		return corpo.substring(0,maxCaracter).replaceAll("\\<.*?>","") + "...";
	}
	
	public String getImagemId(){
		if(imagem != null) {
			return imagem.getId();
		} else if(galeriasMultimidia != null && !CollectionUtils.isEmpty(galeriasMultimidia.getMidias())) {
			final Midia midia = galeriasMultimidia.getMidias().get(0);
			return midia.getId();
		}
		return "";
	}
	
	public String thumbnail60x60(){
		if(imagem != null) {
			return imagem.getTransformacoes().getThumbnail60x60().linkExtreno(marca);
		} else if(galeriasMultimidia != null && !CollectionUtils.isEmpty(galeriasMultimidia.getMidias())) {
			final Midia midia = galeriasMultimidia.getMidias().get(0);
			return midia.thumbnail60x60(marca);
		}
		return "";
	}
	public String thumbnail240x240(){
		if(imagem != null) {
			return imagem.getTransformacoes().getThumbnail240x240().linkExtreno(marca);
		} else if(galeriasMultimidia != null && !CollectionUtils.isEmpty(galeriasMultimidia.getMidias())) {
			final Midia midia = galeriasMultimidia.getMidias().get(0);
			return midia.thumbnail240x240(marca);
		}
		return "";
	}
	public String categoriasInLine(){
		return converterListInLine(categorias);
	}
	public String rotulosControladosInLine(){
		return converterListInLine(rotulosControlados);
	}
	public String editoriasInLine(){
		return converterListInLine(editorias);
	}
	public String tagsInLine(){
		return converterListInLine(tags);
	}

	private String converterListInLine(List<String> collection) {
	  if (CollectionUtils.isEmpty(collection)) return "";
		StringBuilder collectionInLine = new StringBuilder();
		for (String item : collection) {
			if(StringUtils.isEmpty(collectionInLine)) {
				collectionInLine.append(item); 
			} else { 
				collectionInLine.append(", ").append(item);
			}
    }
	  return collectionInLine.toString();
  }
	
	public String getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
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
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getChapeu() {
		return chapeu;
	}
	public void setChapeu(String chapeu) {
		this.chapeu = chapeu;
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
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getEmRevisao() {
		return emRevisao;
	}
	public void setEmRevisao(String emRevisao) {
		this.emRevisao = emRevisao;
	}
	public String getCorpo() {
		return corpo;
	}
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomizada() {
		return customizada;
	}
	public void setCustomizada(String customizada) {
		this.customizada = customizada;
	}
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}
	public String getEditoriaPadrao() {
		return editoriaPadrao;
	}
	public void setEditoriaPadrao(String editoriaPadrao) {
		this.editoriaPadrao = editoriaPadrao;
	}
	public String getDescricaoConteudo() {
		return descricaoConteudo;
	}
	public void setDescricaoConteudo(String descricaoConteudo) {
		this.descricaoConteudo = descricaoConteudo;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public Impresso getImpresso() {
		return impresso;
	}
	public void setImpresso(Impresso impresso) {
		this.impresso = impresso;
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
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getDataDisponibilizacao() {
		return dataDisponibilizacao;
	}
	public void setDataDisponibilizacao(Date dataDisponibilizacao) {
		this.dataDisponibilizacao = dataDisponibilizacao;
	}
	@Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + ((id == null) ? 0 : id.hashCode());
	  return result;
  }
	@Override
  public boolean equals(Object obj) {
	  if (this == obj)
		  return true;
	  if (obj == null)
		  return false;
	  if (getClass() != obj.getClass())
		  return false;
	  Materia other = (Materia) obj;
	  if (id == null) {
		  if (other.id != null)
			  return false;
	  } else if (!id.equals(other.id))
		  return false;
	  return true;
  }
	public List<Conteudo> getConteudos() {
		return conteudos;
	}
	public void setConteudos(List<Conteudo> conteudos) {
		this.conteudos = conteudos;
	}
	public List<Materia> getMateriasRelacionadas() {
		return materiasRelacionadas;
	}
	public void setMateriasRelacionadas(List<Materia> materiasRelacionadas) {
		this.materiasRelacionadas = materiasRelacionadas;
	}
	public Imagem getImagem() {
		return imagem;
	}
	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}
	public GaleriasMultimidia getGaleriasMultimidia() {
		return galeriasMultimidia;
	}
	public void setGaleriasMultimidia(GaleriasMultimidia galeriasMultimidia) {
		this.galeriasMultimidia = galeriasMultimidia;
	}



}
