package br.com.abril.fera.dao;

import java.util.List;

import br.com.abril.fera.model.CreatePage;

public interface CreatePageDAO {
	
	public List<CreatePage> list();

	public CreatePage get(String id);

	public void saveOrUpdate(CreatePage createpage);

	public void delete(String id);

	public List<CreatePage> listByProductId(Integer id);
	
}
