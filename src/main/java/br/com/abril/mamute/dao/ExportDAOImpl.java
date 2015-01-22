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
import br.com.abril.mamute.model.Export;

@Repository
public class ExportDAOImpl implements ExportDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public ExportDAOImpl() {

	}

	public ExportDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Export> list() {
		@SuppressWarnings("unchecked")
		List<Export> listExport = (List<Export>) sessionFactory.getCurrentSession()
				.createCriteria(Export.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listExport;
	}

	@Override
	@Transactional
	public void saveOrUpdate(Export export) {
		sessionFactory.getCurrentSession().saveOrUpdate(export);
	}

	@Override
	@Transactional
	public void delete(int id) {
		Export exportToDelete = new Export();
		exportToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(exportToDelete);
	}

	@Override
	@Transactional
	public Export get(int id) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Export.class);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.add(Restrictions.eq("id",id));
		Export export = (Export) criteria.setResultTransformer(distinctRootEntity).uniqueResult();
    return export;
	}

	@Override
	@Transactional
  public List<Export> listExportActivesByProduct(Product product) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Export.class);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.add(Restrictions.eq("active",true));
		criteria.add(Restrictions.eq("product.id",product.getId()));
		@SuppressWarnings("unchecked")
    List<Export> listExport = (List<Export>) criteria.setResultTransformer(distinctRootEntity).list();

		return listExport;
  }
	@Override
	@Transactional
  public List<Export> listExportActives() {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Export.class);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.add(Restrictions.eq("active",true));
		@SuppressWarnings("unchecked")
    List<Export> listExport = (List<Export>) criteria.setResultTransformer(distinctRootEntity).list();

		return listExport;
  }
	@Override
	@Transactional
	public List<Export> listByProduct(Product product) {
		ResultTransformer distinctRootEntity = Criteria.DISTINCT_ROOT_ENTITY;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Export.class);
		criteria.add(Restrictions.eq("product.id",product.getId()));
		@SuppressWarnings("unchecked")
		List<Export> listExport = (List<Export>) criteria.setResultTransformer(distinctRootEntity).list();

		return listExport;
	}
}