package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Componente;

public interface ComponenteDAO {
	
	public List<Componente> list();

	public Componente get(String id);

	public void saveOrUpdate(Componente componente);

	public void delete(String id);

	List<Componente> listByProductId(Integer id);
	
}
