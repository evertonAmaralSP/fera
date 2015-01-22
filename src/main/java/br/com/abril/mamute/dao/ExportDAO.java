package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Product;
import br.com.abril.mamute.model.Export;

public interface ExportDAO {
	public List<Export> list();

	public List<Export> listExportActives();

	public List<Export> listExportActivesByProduct(Product product);

	public List<Export> listByProduct(Product product);

	public Export get(int id);

	public void saveOrUpdate(Export export);

	public void delete(int id);
}