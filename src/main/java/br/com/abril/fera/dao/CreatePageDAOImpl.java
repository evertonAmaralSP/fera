package br.com.abril.fera.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.fera.model.CreatePage;

@Repository
public class CreatePageDAOImpl implements CreatePageDAO {

	@Autowired
  private MongoOperations operations;
	
	@Override
  public List<CreatePage> list() {
		return operations.findAll(CreatePage.class);
  }

	@Override
  public CreatePage get(String id) {
		Query query = new Query(); 
		query.addCriteria(Criteria.where("id").is(id));
		return operations.findOne(query, CreatePage.class);
	  
  }

	@Override
  public void saveOrUpdate(CreatePage createpage) {
		if (!operations.collectionExists(CreatePage.class)) {
			operations.createCollection(CreatePage.class);
		}
		operations.save(createpage);
  }

	@Override
  public void delete(String id) {
		CreatePage createpage = get(id);
		operations.remove(createpage);
	  
  }

	@Override
	public List<CreatePage> listByProductId(Integer id) {
		Query query = new Query(); 
		query.addCriteria(Criteria.where("productId").is(id));
		return operations.find(query, CreatePage.class);
  }

}
