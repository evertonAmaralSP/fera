package br.com.abril.fera.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.abril.fera.dao.ProductDAO;
import br.com.abril.fera.dao.TemplateDAO;
import br.com.abril.fera.dao.UploadDAO;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Upload;
import br.com.abril.fera.support.errors.FeraErrors;
import br.com.abril.fera.support.factory.FileFactory;
import br.com.abril.fera.support.slug.SlugUtil;

/**
 * Handles requests for the product home page.
 */
@Controller
@RequestMapping("/marcas")
public class ProductController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String MARCAS_UPLOAD = "marcas/ProductUpload";
	private static final String MARCAS_LISTA_ARQUIVOS = "marcas/listaArquivos";
	private static final String MARCA_LIST = "marcas/ProductList";
//	private static final String REDIRECT_MARCAS = "redirect:/marcas/";
	private static final String MARCA_ADMIN_LIST = "admin/marcas/ProductList";
	private static final String MARCA_ADMIN_FORM = "admin/marcas/ProductForm";
	private static final String REDIRECT_MARCAS_ADMIN = "redirect:/marcas/admin/";
	private static final String REDIRECT_MARCAS_UPLOAD = "redirect:/marcas/%s/upload";

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private TemplateDAO templateDAO;
	
	@Autowired
	private FeraErrors feraErrors;

	@Autowired
	private SlugUtil slugUtil;

	@Autowired
	private UploadDAO uploadDAO;
	
	@Autowired
	private FileFactory fileFactory;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping("/")
	public String list(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		List<Product> listProducts = new ArrayList<Product>();
		listProducts.add(product);
		model.addAttribute("listProducts", listProducts);
		return MARCA_LIST;
	}
	
	@RequestMapping("/admin")
	public String listAdmin(ModelMap model) {
		List<Product> listProducts = productDAO.list();
		model.addAttribute("listProducts", listProducts);
		return MARCA_ADMIN_LIST;
	}

	@RequestMapping(value = "/admin/new", method = RequestMethod.GET)
	public String newProduct(ModelMap model) {
		model.addAttribute("product", new Product());
		return MARCA_ADMIN_FORM;
	}

	@RequestMapping(value = "/{id}/admin/edit", method = RequestMethod.GET)
	public String editProduct(ModelMap model, @PathVariable String id) {
		int productId = Integer.parseInt(id);
		Product product = productDAO.get(productId);
		model.addAttribute("product", product);
		return MARCA_ADMIN_FORM;
	}

	@RequestMapping(value = "/{id}/admin/delete", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable String id) {
		int productId = Integer.parseInt(id);
		productDAO.delete(productId);
		return REDIRECT_MARCAS_ADMIN;
	}

	@RequestMapping(value = "/admin/save", method = RequestMethod.POST)
	public String saveProduct(Model model, @Valid @ModelAttribute Product product, Errors errors) {

		if (errors.hasErrors()) {
			return MARCA_ADMIN_FORM;
		}

		productDAO.saveOrUpdate(product);

		return REDIRECT_MARCAS_ADMIN;
	}

	/**
	 * Lista de arquivos gerados pelo template de uma marca.
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}/listExports", method = RequestMethod.GET)
	public String listFile(ModelMap model, @PathVariable int id) throws Exception {
		Product product = productDAO.get(id);
		List<String> listFiles = fileFactory.getListStringFilesInProduct(product);
		model.addAttribute("listFiles", listFiles);

		return MARCAS_LISTA_ARQUIVOS;
	}

	@RequestMapping(value = "/{id}/upload")
	public String singleUpload(ModelMap model, @PathVariable int id) {
		Product product = productDAO.getIdJoinUpload(id);
		model.addAttribute("product", product);
		
		return MARCAS_UPLOAD;
	}
	
	@RequestMapping(value = "/{id}/upload/{idUpload}/delete")
	public String deleteFile(ModelMap model, @PathVariable int id, @PathVariable int idUpload) {
		Product product = productDAO.getIdJoinUpload(id);
		Upload upload = uploadDAO.get(idUpload);
		if (fileFactory.excluir(upload.getPath(), upload.getName())) {
			uploadDAO.delete(idUpload);
		} else {
			feraErrors.clean();
			feraErrors.addError(getMessageSource("product.falha.file.not.delete.error"), String.format(getMessageSource("product.falha.file.not.delete.text"),upload.getName()));
			model.addAttribute("feraErrors", feraErrors);
		}
		
		model.addAttribute("product", product);
		
		return String.format(REDIRECT_MARCAS_UPLOAD,id);
	}

	@RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
	public String singleSave(ModelMap model, @RequestParam("file") MultipartFile file, @PathVariable int id) {
		Product product = productDAO.getIdJoinUpload(id);
		model.addAttribute("product", product);
		
		if (file.isEmpty() || !fileFactory.validateFileTypes(file)) {
			feraErrors.clean();
			if (file.isEmpty()) {
				feraErrors.addError(getMessageSource("product.falha.file.empty.error"), getMessageSource("product.falha.file.empty.text"));
				logger.error(getMessageSource("product.falha.file.empty.text"));
			} else {
				feraErrors.addError(getMessageSource("product.falha.file.not.valide.error"), getMessageSource("product.falha.file.not.valide.text"));
				logger.error(getMessageSource("product.falha.file.not.valide.text"));
			}
			model.addAttribute("feraErrors", feraErrors);
			return MARCAS_UPLOAD;
		}
		logger.debug("File Description: {} ", new Object[] { file.getName() });
		
		String path = fileFactory.generatePathOfDirectoryProduct(product.getPath());
		path = extractPathUpload(file, path);
		String fileName = file.getOriginalFilename();
		
		try {

			fileFactory.salvarArquivoPathProduct(path, file, fileName);
			
			Upload upload = new Upload(product,fileName,path,file.getContentType());
			uploadDAO.saveOrUpdate(upload);

			return String.format(REDIRECT_MARCAS_UPLOAD,id);
		} catch (Exception e) {
			feraErrors.clean();
			feraErrors.addError(getMessageSource("global.inesperado.error"), getMessageSource("global.inesperado.text"));
			
			logger.error("You failed to upload {} : {} ", new Object[] { fileName,e.getMessage() });
			return MARCAS_UPLOAD;
		}
	 
	}

	private String getMessageSource(String key) {
	  return messageSource.getMessage(key,null, null);
  }

	private String extractPathUpload(MultipartFile file, String path) {
	  if("text/css".equalsIgnoreCase(file.getContentType())){
	  	path=path+"/stylesheets";
	  } else if("product/x-javascript".equalsIgnoreCase(file.getContentType()) || "text/javascript".equalsIgnoreCase(file.getContentType()) ){
	  	path=path+"/javascripts";
	  } else if(file.getContentType().matches("(image\\/)*+(jpeg|gif|png|ico|x-ico|vnd.microsoft.icon)")){
	  	path=path+"/images";
	  } else {
	  	path=path+"/includes";
	  }
	  return path;
  }

}
