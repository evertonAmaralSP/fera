package br.com.abril.mamute.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TEMPLATES")
public class Template {

	private static final String NOT_BLANK_MESSAGE = "{validate.mandatory_field}";
	private static final String TAMANHO_NOME_EXCEDIDO = "{validate.name.fail.length_exceeded}";
	private static final String TAMANHO_PATH_EXCEDIDO = "{validate.path.fail.length_exceeded}";
	private static final String TAMANHO_DESCRICAO_EXCEDIDO = "{validate.description.fail.length_exceeded}";
	private static final String NOT_BLANCK_PRODUCT = "{validate.product.fail.mandatory_field}";
	private static final String NOT_BLANCK_TEMPLATE_TYPE = "{validate.templatetype.fail.mandatory_field}";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Length(max = 45, message = TAMANHO_NOME_EXCEDIDO)
	private String name;
	@Length(max = 150, message = TAMANHO_DESCRICAO_EXCEDIDO)
	private String description;

	@NotNull(message = NOT_BLANCK_PRODUCT)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@NotNull(message = NOT_BLANCK_TEMPLATE_TYPE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private TemplateType type;
	private Integer master_id;

	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Length(max = 120, message = TAMANHO_PATH_EXCEDIDO)
	private String path;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	private String document;
	private Date createdAt;
	private Date updatedAt;
	@OneToMany(targetEntity=Source.class, fetch = FetchType.LAZY, mappedBy = "template")
	private List<Source> sources;

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

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}


}
