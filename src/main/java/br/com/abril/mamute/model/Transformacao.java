package br.com.abril.mamute.model;

import java.util.List;

public class Transformacao {
	private String origem;
	private List<Link> link;

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}
	
	public String linkExtreno(String marca){
		for (Link l : link) {
			if(l.getRel().equals("externo"))
				return l.getHref().replace("{marca}", marca); 
    }
		return null;
	}

	public List<Link> getLink() {
		return link;
	}

	public void setLink(List<Link> link) {
		this.link = link;
	}
}
