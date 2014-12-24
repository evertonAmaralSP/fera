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

import br.com.abril.mamute.model.Source;

@Repository
public class SourceDAOImpl implements SourceDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public SourceDAOImpl() {

	}

	public SourceDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Source> list() {
		@SuppressWarnings("unchecked")
		List<Source> listSource = (List<Source>) sessionFactory.getCurrentSession()
				.createCriteria(Source.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listSource;
	}

	@Override
	@Transactional
	public void saveOrUpdate(Source source) {
		sessionFactory.getCurrentSession().saveOrUpdate(source);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Source sourceToDelete = new Source();
		sourceToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(sourceToDelete);
	}

	@Override
	@Transactional
	public Source get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.setFetchMode("template", FetchMode.JOIN);
		criteria.add(Restrictions.eq("id",id));
		Source source = (Source) criteria.setResultTransformer(distinctRootEntity).uniqueResult();
    return source;
	}

	@Override
	@Transactional
  public List<Source> listSourceActives() {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Source.class);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.setFetchMode("templates", FetchMode.JOIN);
		criteria.add(Restrictions.eq("active",true));
		@SuppressWarnings("unchecked")
    List<Source> listSource = (List<Source>) criteria.setResultTransformer(distinctRootEntity).list();

	return listSource;

  }
}