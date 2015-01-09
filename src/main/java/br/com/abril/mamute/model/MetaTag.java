package br.com.abril.mamute.model;

import org.springframework.util.StringUtils;

public class MetaTag {
	
	private String name;
	private String property;
	private String content;

	private String htmlMetaTagProperty;
	private String htmlMetaTagName;
	
	public MetaTag() {
		this.htmlMetaTagProperty = "<meta property=\"%s\" content=\"%s\" >";
		this.htmlMetaTagName = "<meta name=\"%s\" content=\"%s\" >";
	}
	
	public String htmlMeta() throws IllegalArgumentException {
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(content)) throw new IllegalArgumentException();
		return String.format(htmlMetaTagName, name,content);
	}
	
	public String htmlMetaProperty()  throws IllegalArgumentException {
		if(StringUtils.isEmpty(property) || StringUtils.isEmpty(content)) throw new IllegalArgumentException();
		return String.format(htmlMetaTagProperty, property,content);
	}
	
	

}
