package br.com.abril.mamute.model.editorial;

import java.util.Arrays;


public abstract class RestModel {

	private Link[] links;

	public Link[] getLinks() {
		return links;
	}
	public Link[] getLink() {
		return links;
	}

	public void setLinks(Link[] newLinks) {
		this.links = (newLinks != null ? Arrays.copyOf(newLinks, newLinks.length) : null);
	}

	public void setLink(Link[] newLinks) {
		this.links = (newLinks != null ? Arrays.copyOf(newLinks, newLinks.length) : null);
	}

	public Link getLinkByRel(String rel){
		Link linkfound = null;
		if(links != null && links.length > 0){
			for (Link link : links) {
				if(rel.equalsIgnoreCase(link.getRel())){
					linkfound = link;
					break;
				}
			}
		}
		return linkfound;
	}

}
