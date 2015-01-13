package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Source;

public interface SourceDAO {
	public List<Source> list();

	public List<Source> listSourceActives();

	public List<Source> listSourceActivesByProduct(Product product);

	public List<Source> listByProduct(Product product);

	public Source get(int id);

	public void saveOrUpdate(Source source);

	public void delete(int id);
}