package br.com.abril.mamute.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = "SOURCES")
public class Source {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;
	private String description;
	private String source;
	private Date lastUpdateDatePooling;
	private String lastUpdateIdPooling;
	private Boolean active;
	private Date created;
	private Date updated;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "templateId")
	private Template template;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	private Product product;


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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}	public Date getLastUpdateDatePooling() {
		return lastUpdateDatePooling;
	}
	public void setLastUpdateDatePooling(Date lastUpdateDatePooling) {
		this.lastUpdateDatePooling = lastUpdateDatePooling;
	}
	public String getLastUpdateIdPooling() {
		return lastUpdateIdPooling;
	}
	public void setLastUpdateIdPooling(String lastUpdateIdPooling) {
		this.lastUpdateIdPooling = lastUpdateIdPooling;
	}

	@PrePersist
	protected void onCreate() {
		created = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}


}
