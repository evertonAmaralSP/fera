package br.com.abril.fera.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.abril.fera.dao.ExportDAO;
import br.com.abril.fera.dao.ProductDAO;
import br.com.abril.fera.model.Export;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.support.aws.AwsFileFactory;
import br.com.abril.fera.support.tipos.TipoExportEnum;

/**
 * Handles requests for the product home page.
 */
@Controller
@RequestMapping("/exports")
public class ExportController {

	private static final String REDIRECT_EXPORTS = "redirect:/exports/";
	private static final String EXPORT_LIST = "exports/ExportList";
	private static final String EXPORT_FORM = "exports/ExportForm";

	@Autowired
	private ExportDAO exportDao;
	@Autowired
	private ProductDAO productDao;
	@Autowired
	AwsFileFactory awsFileFactory;
	
	@RequestMapping("/")
	public String list(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		List<Export> listExports = exportDao.listByProduct(product);
		model.addAttribute("listExports", listExports);
		return EXPORT_LIST;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newExport(ModelMap model) {
		model.addAttribute("listType", listExportTypeItens());
		model.addAttribute("export", new Export());
		return EXPORT_FORM;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editExport(ModelMap model,@PathVariable int id) {
		Export export = exportDao.get(id);
		model.addAttribute("listType", listExportTypeItens());
		model.addAttribute("export", export);
		return EXPORT_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteExport(@PathVariable int id) {
		exportDao.delete(id);
		return REDIRECT_EXPORTS;
	}
	
	@RequestMapping(value = "/{id}/push", method = RequestMethod.GET)
	public String puthFileExport(@PathVariable int id,HttpServletRequest request) {
		Export export = exportDao.get(id);
		try {
	    awsFileFactory.uploadFile(export);
    } catch (IOException e) {
	    e.printStackTrace();
    }
		return REDIRECT_EXPORTS;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveExport(Model model, @Valid @ModelAttribute Export export, Errors errors,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		export.setProduct(product);

		if (errors.hasErrors()) {
			model.addAttribute("listType", listExportTypeItens());
			return EXPORT_FORM;
		}
		exportDao.saveOrUpdate(export);
		return REDIRECT_EXPORTS;
	}
	

	private ArrayList<TipoExportEnum> listExportTypeItens() {
		return new ArrayList<TipoExportEnum>(Arrays.asList(TipoExportEnum.values()));
	}

}
