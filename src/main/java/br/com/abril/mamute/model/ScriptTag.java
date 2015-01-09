package br.com.abril.mamute.model;

public class ScriptTag {

	
	private String type;
	private String src;
	private Integer position;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public ScriptTag(String type, String src) {
	  super();
	  this.type = type;
	  this.src = src;
  }
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	
}
