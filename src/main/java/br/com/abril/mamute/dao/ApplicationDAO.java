package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Application;

public interface ApplicationDAO {
	public List<Application> list();

	public Application get(int id);

	public void saveOrUpdate(Application application);

	public void delete(int id);
}