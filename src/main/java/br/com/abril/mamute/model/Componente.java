package br.com.abril.mamute.model;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Componentes")
public class Componente {

	private static final String NOT_BLANK_MESSAGE = "{validate.mandatory_field}";
	private static final String TAMANHO_NOME_EXCEDIDO = "{validate.name.fail.length_exceeded}";
	private static final String TAMANHO_DESCRICAO_EXCEDIDO = "{validate.description.fail.length_exceeded}";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
//	@NotBlank(message = NOT_BLANK_MESSAGE)
//	@Length(max = 45, message = TAMANHO_NOME_EXCEDIDO)
	private String name;
	@Length(max = 150, message = TAMANHO_DESCRICAO_EXCEDIDO)
	private String description;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	private String type;
	@NotBlank(message = NOT_BLANK_MESSAGE)
	private String icon;
	private Integer productId;
	@NotNull(message = NOT_BLANK_MESSAGE)
	private Integer templateId;
	private List<MetaTag> metaTags;
	private List<String> scripts;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public List<MetaTag> getMetaTags() {
		return metaTags;
	}
	public void setMetaTags(List<MetaTag> metaTags) {
		this.metaTags = metaTags;
	}
	public List<String> getScripts() {
		return scripts;
	}
	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}
