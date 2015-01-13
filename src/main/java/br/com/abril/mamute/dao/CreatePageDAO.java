package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.CreatePage;

public interface CreatePageDAO {
	
	public List<CreatePage> list();

	public CreatePage get(String id);

	public void saveOrUpdate(CreatePage createpage);

	public void delete(String id);
	
}
