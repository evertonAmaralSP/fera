package br.com.abril.mamute.support.factory;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.mamute.model.Link;
import br.com.abril.mamute.model.Materia;
import br.com.abril.mamute.model.ResultadoBuscaMateria;
import br.com.abril.mamute.support.json.JsonUtil;
import br.com.abril.mamute.support.log.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class ModelFactory {

	@Log
	private Logger logger;

	@Autowired
	private JsonUtil jsonUtil;


	public Materia materia(JsonObject jsonObject) {
		if (!jsonObject.has("tipo_recurso")) {
			logger.error("O JSON de materia nao possui a chave 'materia'");
			throw new IllegalStateException("O JSON de materia nao possui a chave 'materia'");
		}

		if (!jsonObject.has("link")) {
			logger.error("O JSON de materia nao possui a chave 'link'");
			throw new IllegalStateException("O JSON de materia nao possui a chave 'link'");
		}

		JsonElement materiaElement = jsonObject.getAsJsonObject();
		JsonElement linkElement = jsonObject.get("link");

		final Gson jsonHandler = jsonUtil.getJsonHandler();

		Materia materia = jsonHandler.fromJson(materiaElement, Materia.class);
		materia.setLinks(jsonHandler.fromJson(linkElement, Link[].class));

		return materia;
	}

	public ResultadoBuscaMateria listaConteudos(JsonObject jsonObject) {

		if (!jsonObject.has("link")) {
			logger.error("O JSON de materia nao possui a chave 'link'");
			throw new IllegalStateException("O JSON de materia nao possui a chave 'link'");
		}

		JsonElement linkElement = jsonObject.get("link");


		Gson jsonHandler = jsonUtil.getJsonHandler();
		ResultadoBuscaMateria resultadoBuscaConteudo = jsonHandler.fromJson(jsonObject, ResultadoBuscaMateria.class);
		resultadoBuscaConteudo.setLinks(jsonHandler.fromJson(linkElement, Link[].class));

		return resultadoBuscaConteudo;
	}


}
