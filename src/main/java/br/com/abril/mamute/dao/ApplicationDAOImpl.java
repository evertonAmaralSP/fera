package br.com.abril.mamute.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.mamute.model.Application;

@Repository
public class ApplicationDAOImpl implements ApplicationDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public ApplicationDAOImpl() {

	}

	public ApplicationDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Application> list() {
		@SuppressWarnings("unchecked")
		List<Application> listApplication = (List<Application>) sessionFactory.getCurrentSession()
				.createCriteria(Application.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listApplication;
	}

	@Override
	@Transactional
	public void saveOrUpdate(Application application) {
		sessionFactory.getCurrentSession().saveOrUpdate(application);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Application applicationToDelete = new Application();
		applicationToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(applicationToDelete);
	}

	@Override
	@Transactional
	public Application get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Application.class);
		criteria.add(Restrictions.eq("id",id));
		Application application = (Application) criteria.setResultTransformer(distinctRootEntity).uniqueResult();

    return application;
	}
}