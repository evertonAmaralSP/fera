package br.com.abril.fera.model;

public class LinkTag {

	private String rel;
	private String type;
	private String href;
	private Integer position;
	
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public LinkTag(String rel, String type, String href) {
	  super();
	  this.rel = rel;
	  this.type = type;
	  this.href = href;
  }
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}

	
}
