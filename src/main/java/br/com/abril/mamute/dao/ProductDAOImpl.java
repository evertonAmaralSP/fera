package br.com.abril.mamute.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.mamute.model.Product;

@Repository
public class ProductDAOImpl implements ProductDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public ProductDAOImpl() {

	}

	public ProductDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Product> list() {
		@SuppressWarnings("unchecked")
		List<Product> listProduct = (List<Product>) sessionFactory.getCurrentSession()
				.createCriteria(Product.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listProduct;
	}

	@Override
	@Transactional
	public void saveOrUpdate(Product product) {
		sessionFactory.getCurrentSession().saveOrUpdate(product);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Product productToDelete = new Product();
		productToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(productToDelete);
	}

	@Override
	@Transactional
	public Product get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("id",id));
		criteria.setFetchMode("templates", FetchMode.JOIN);
		Product product = (Product) criteria.setResultTransformer(distinctRootEntity).uniqueResult();

    return product;
	}
	
	@Override
	@Transactional
	public Product getIdJoinUpload(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("id",id));
		criteria.setFetchMode("uploads", FetchMode.JOIN);
		Product product = (Product) criteria.setResultTransformer(distinctRootEntity).uniqueResult();

    return product;
	}
	
}