package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Product;

public interface ProductDAO {
	public List<Product> list();

	public Product get(int id);

	public void saveOrUpdate(Product product);

	public void delete(int id);
}