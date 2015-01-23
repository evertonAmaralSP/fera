package br.com.abril.fera.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Upload;

@Repository
public class UploadDAOImpl implements UploadDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public UploadDAOImpl() {

	}

	public UploadDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Upload> list() {
		@SuppressWarnings("unchecked")
		List<Upload> listUpload = (List<Upload>) sessionFactory.getCurrentSession()
				.createCriteria(Upload.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listUpload;
	}

	@Override
	@Transactional
	public void saveOrUpdate(Upload upload) {
		sessionFactory.getCurrentSession().saveOrUpdate(upload);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Upload uploadToDelete = new Upload();
		uploadToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(uploadToDelete);
	}

	@Override
	@Transactional
	public Upload get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Upload.class);
		criteria.add(Restrictions.eq("id",id));
		Upload upload = (Upload) criteria.setResultTransformer(distinctRootEntity).uniqueResult();
    return upload;
	}
	
	@Override
	@Transactional
	public List<Upload> listByProduct(Product product) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Upload.class);
		criteria.add(Restrictions.eq("product.id",product.getId()));
		@SuppressWarnings("unchecked")
		List<Upload> listUpload = (List<Upload>) criteria.setResultTransformer(distinctRootEntity).list();

		return listUpload;
	}
	
}