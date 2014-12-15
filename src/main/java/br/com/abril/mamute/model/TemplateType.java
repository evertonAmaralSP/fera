package br.com.abril.mamute.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name = "TEMPLATE_TYPES")
public class TemplateType {


	private static final String NOT_BLANK_MESSAGE = "{validate.mandatory_field}";
	private static final String TAMANHO_NOME_EXCEDIDO = "{validate.name.fail.length_exceeded}";
	private static final String TAMANHO_DESCRICAO_EXCEDIDO = "{validate.description.fail.length_exceeded}";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	@Length(max = 45, message = TAMANHO_NOME_EXCEDIDO)
	private String name;
	@Length(max = 150, message = TAMANHO_DESCRICAO_EXCEDIDO)
	private String description;
	private Date createdAt;
	private Date updatedAt;
	@Transient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
	private List<Template> templates;

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


	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	@Override
  public String toString() {
	  return "TemplateType [id=" + id + ", name=" + name + "]";
  }





}
