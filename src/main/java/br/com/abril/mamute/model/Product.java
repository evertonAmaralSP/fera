package br.com.abril.mamute.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "PRODUCTS")
public class Product {

	private static final String NOT_BLANK_MESSAGE = "{validacao.campo_obrigatorio}";
	private static final String TAMANHO_NOME_EXCEDIDO = "{product.fail.length_name_exceeded}";
	private static final String TAMANHO_PATH_EXCEDIDO = "{product.fail.length_path_exceeded}";

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@NotBlank(message = Product.NOT_BLANK_MESSAGE)
	@Length(max = 45, message = Product.TAMANHO_NOME_EXCEDIDO)
	private String name;

	@NotBlank(message = Product.NOT_BLANK_MESSAGE)
	@Length(max = 120, message = Product.TAMANHO_PATH_EXCEDIDO)
	private String path;
	private Date createdAt;
	private Date updatedAt;
	@OneToMany(targetEntity=Template.class, fetch = FetchType.LAZY, mappedBy = "product")
	private List<Template> templates;
	@OneToMany(targetEntity=Source.class, fetch = FetchType.LAZY, mappedBy = "product")
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

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Date();
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
  public String toString() {
	  return "Product [id=" + id + ", name=" + name + "]";
  }


}
