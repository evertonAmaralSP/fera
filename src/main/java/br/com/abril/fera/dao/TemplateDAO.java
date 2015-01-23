package br.com.abril.fera.dao;

import java.util.List;

import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Template;

public interface TemplateDAO {
	public List<Template> list();
	
	public List<Template> listByProduct(Product product);

	public Template get(int id);

	public void saveOrUpdate(Template template);

	public void delete(int id);
}