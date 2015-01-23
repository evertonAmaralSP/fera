package br.com.abril.fera.dao;

import java.util.List;

import br.com.abril.fera.model.TemplateType;

public interface TemplateTypeDAO {
	public List<TemplateType> list();

	public TemplateType get(int id);

	public void saveOrUpdate(TemplateType templateType);

	public void delete(int id);
}