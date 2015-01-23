package br.com.abril.fera.dao;

import java.util.List;

import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Upload;

public interface UploadDAO {
	public List<Upload> list();
	
	public List<Upload> listByProduct(Product product);

	public Upload get(int id);

	public void saveOrUpdate(Upload upload);

	public void delete(int id);
}