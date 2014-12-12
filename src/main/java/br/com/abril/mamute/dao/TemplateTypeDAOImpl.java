package br.com.abril.mamute.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.mamute.model.TemplateType;

@Repository
public class TemplateTypeDAOImpl implements TemplateTypeDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public TemplateTypeDAOImpl() {

	}

	public TemplateTypeDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<TemplateType> list() {
		@SuppressWarnings("unchecked")
		List<TemplateType> listTemplateType = (List<TemplateType>) sessionFactory.getCurrentSession()
				.createCriteria(TemplateType.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listTemplateType;
	}

	@Override
	@Transactional
	public void saveOrUpdate(TemplateType templateType) {
		sessionFactory.getCurrentSession().saveOrUpdate(templateType);
	}

	@Override
	@Transactional
	public void delete(int id) {
		TemplateType templateTypeToDelete = new TemplateType();
		templateTypeToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(templateTypeToDelete);
	}

	@Override
	@Transactional
	public TemplateType get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TemplateType.class);
		criteria.add(Restrictions.eq("id",id));
		TemplateType templateType = (TemplateType) criteria.setResultTransformer(distinctRootEntity).uniqueResult();

    return templateType;
	}
}