package br.com.abril.mamute.dao;

import java.util.List;

import br.com.abril.mamute.model.Upload;

public interface UploadDAO {
	public List<Upload> list();

	public Upload get(int id);

	public void saveOrUpdate(Upload upload);

	public void delete(int id);
}