package br.com.abril.fera.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TEMPLATES")
public class Template implements Serializable {

	/**
	 * 
	 */
  private static final long serialVersionUID = 1730857665779052176L;
	private static final String NOT_BLANK_MESSAGE = "{validate.mandatory_field}";
	private static final String TAMANHO_NOME_EXCEDIDO = "{validate.name.fail.length_exceeded}";
	private static final String TAMANHO_PATH_EXCEDIDO = "{validate.path.fail.length_exceeded}";
	private static final String TAMANHO_DESCRICAO_EXCEDIDO = "{validate.description.fail.length_exceeded}";
	private static final String NOT_BLANCK_TEMPLATE_TYPE = "{validate.templatetype.fail.mandatory_field}";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Length(max = 45, message = TAMANHO_NOME_EXCEDIDO)
	private String name;
	@Length(max = 150, message = TAMANHO_DESCRICAO_EXCEDIDO)
	private String description;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@NotNull(message = NOT_BLANCK_TEMPLATE_TYPE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private TemplateType type;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id" )
	private Source source;
	private Integer master_id;

	private Date lastUpdateDateAvailablePooling;
	private Date lastUpdateDateUpdatePooling;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Length(max = 120, message = TAMANHO_PATH_EXCEDIDO)
	private String path;
	private String document;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Column(name = "document_draft")
	private String documentDraft;
	private Date createdAt;
	private Date updatedAt;
	@Transient 
	private boolean publicar;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public TemplateType getType() {
		return type;
	}

	public void setType(TemplateType type) {
		this.type = type;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setMaster_id(Integer master_id) {
		this.master_id = master_id;
	}

	public Integer getMaster_id() {
		return master_id;
	}

	@Override
  public String toString() {
	  return "Template [id=" + id + ", name=" + name + ", description=" + description + ", product=" + product + ", type=" + type + ", master_id=" + master_id + ", path=" + path + ", document=" + document + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
  }

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Date getLastUpdateDateAvailablePooling() {
		return lastUpdateDateAvailablePooling;
	}

	public void setLastUpdateDateAvailablePooling(Date lastUpdateDateAvailablePooling) {
		this.lastUpdateDateAvailablePooling = lastUpdateDateAvailablePooling;
	}

	public Date getLastUpdateDateUpdatePooling() {
		return lastUpdateDateUpdatePooling;
	}

	public void setLastUpdateDateUpdatePooling(Date lastUpdateDateUpdatePooling) {
		this.lastUpdateDateUpdatePooling = lastUpdateDateUpdatePooling;
	}

	public String getDocumentDraft() {
		return documentDraft;
	}

	public void setDocumentDraft(String documentDraft) {
		this.documentDraft = documentDraft;
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
	  Template other = (Template) obj;
	  if (id == null) {
		  if (other.id != null)
			  return false;
	  } else if (!id.equals(other.id))
		  return false;
	  return true;
  }

	public boolean isPublicar() {
		return publicar;
	}

	public void setPublicar(boolean publicar) {
		this.publicar = publicar;
	}


}
