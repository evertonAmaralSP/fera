package br.com.abril.mamute.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.abril.mamute.support.tipos.TipoPageEnum;

@Document(collection = "Componente")
public class CreatePage {
	
	@Id
	private String id;
	private Integer productId;
	private Integer templateLayoutId;
	private String name;
	private String description;
	private TipoPageEnum type;
	private GrupoPage grupoPage;
	private Integer version;
	private MetaDocument document;
	private MetaDocument documentDraft;
	private List<ScriptTag> scriptTags;
	private List<LinkTag> linkTags;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public TipoPageEnum getType() {
		return type;
	}
	public void setType(TipoPageEnum type) {
		this.type = type;
	}
	public GrupoPage getGrupoPage() {
		return grupoPage;
	}
	public void setGrupoPage(GrupoPage grupoPage) {
		this.grupoPage = grupoPage;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public MetaDocument getDocument() {
		return document;
	}
	public void setDocument(MetaDocument document) {
		this.document = document;
	}
	public MetaDocument getDocumentDraft() {
		return documentDraft;
	}
	public void setDocumentDraft(MetaDocument documentDraft) {
		this.documentDraft = documentDraft;
	}
	public Integer getTemplateLayoutId() {
		return templateLayoutId;
	}
	public void setTemplateLayoutId(Integer templateLayoutId) {
		this.templateLayoutId = templateLayoutId;
	}
	public List<ScriptTag> getScriptTags() {
		return scriptTags;
	}
	public void setScriptTags(List<ScriptTag> scriptTags) {
		this.scriptTags = scriptTags;
	}
	public List<LinkTag> getLinkTags() {
		return linkTags;
	}
	public void setLinkTags(List<LinkTag> linkTags) {
		this.linkTags = linkTags;
	}
	

}
