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

import br.com.abril.mamute.model.Template;

@Repository
public class TemplateDAOImpl implements TemplateDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public TemplateDAOImpl() {

	}

	public TemplateDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Template> list() {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Template.class);
		criteria.setFetchMode("application", FetchMode.JOIN);
		@SuppressWarnings("unchecked")
		List<Template> listTemplate = (List<Template>) criteria.setResultTransformer(distinctRootEntity).list();

		return listTemplate;
	}

	@Override
	@Transactional
	public void saveOrUpdate(Template template) {
		sessionFactory.getCurrentSession().saveOrUpdate(template);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Template templateToDelete = new Template();
		templateToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(templateToDelete);
	}

	@Override
	@Transactional
	public Template get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Template.class);
		criteria.setFetchMode("application", FetchMode.JOIN);
		criteria.setFetchMode("type", FetchMode.JOIN);
		criteria.add(Restrictions.eq("id",id));
		Template template = (Template) criteria.setResultTransformer(distinctRootEntity).uniqueResult();
		return template;
	}
}