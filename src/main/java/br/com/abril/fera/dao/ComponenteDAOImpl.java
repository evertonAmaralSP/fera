package br.com.abril.fera.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.fera.model.Componente;

@Repository
public class ComponenteDAOImpl implements ComponenteDAO {

	@Autowired
  private MongoOperations operations;
	
	@Override
  public List<Componente> list() {
		return operations.findAll(Componente.class);
  }
	
	@Override
  public List<Componente> listByProductId(Integer id) {
		Query query = new Query(); 
		query.addCriteria(Criteria.where("productId").is(id));
		return operations.find(query, Componente.class);
	  
  }

	@Override
  public Componente get(String id) {
		Query query = new Query(); 
		query.addCriteria(Criteria.where("id").is(id));
		return operations.findOne(query, Componente.class);
	  
  }

	@Override
  public void saveOrUpdate(Componente componente) {
		if (!operations.collectionExists(Componente.class)) {
			operations.createCollection(Componente.class);
		}
		operations.save(componente);
  }

	@Override
  public void delete(String id) {
		Componente componente = get(id);
		operations.remove(componente);
	  
  }

}
