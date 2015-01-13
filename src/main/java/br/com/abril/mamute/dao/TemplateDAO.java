package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Template;

public interface TemplateDAO {
	public List<Template> list();
	
	public List<Template> listByProduct(Product product);

	public Template get(int id);

	public void saveOrUpdate(Template template);

	public void delete(int id);
}