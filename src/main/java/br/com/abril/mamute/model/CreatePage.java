package br.com.abril.mamute.model;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CreatePage")
public class CreatePage {
	

	private static final String NOT_BLANK_MESSAGE = "{validate.mandatory_field}";
	private static final String NOT_BLANCK_PRODUCT = "{validate.product.fail.mandatory_field}";
	private static final String TAMANHO_NOME_EXCEDIDO = "{validate.name.fail.length_exceeded}";
	private static final String TAMANHO_DESCRICAO_EXCEDIDO = "{validate.description.fail.length_exceeded}";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Length(max = 45, message = TAMANHO_NOME_EXCEDIDO)
	private String name;
	@Length(max = 150, message = TAMANHO_DESCRICAO_EXCEDIDO)
	private String description;
	@NotNull(message = NOT_BLANCK_PRODUCT)
	private Integer productId;
	@NotNull(message = NOT_BLANK_MESSAGE)
	private Integer templateLayoutId;
	@NotNull(message = NOT_BLANK_MESSAGE)
	private String type;
	private String groupPage;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGroupPage() {
		return groupPage;
	}
	public void setGroupPage(String groupPage) {
		this.groupPage = groupPage;
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
