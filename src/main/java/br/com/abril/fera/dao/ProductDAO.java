package br.com.abril.fera.dao;

import java.util.List;

import br.com.abril.fera.model.Product;

public interface ProductDAO {
	public List<Product> list();

	public Product get(int id);

	public void saveOrUpdate(Product product);

	public void delete(int id);

	public Product getIdJoinUpload(int id);
}