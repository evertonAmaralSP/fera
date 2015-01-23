package br.com.abril.fera.dao;

import java.util.List;

import br.com.abril.fera.model.Export;
import br.com.abril.fera.model.Product;

public interface ExportDAO {
	public List<Export> list();

	public List<Export> listExportActives();

	public List<Export> listExportActivesByProduct(Product product);

	public List<Export> listByProduct(Product product);

	public Export get(int id);

	public void saveOrUpdate(Export export);

	public void delete(int id);
}