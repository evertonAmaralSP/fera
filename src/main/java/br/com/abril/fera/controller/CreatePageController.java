package br.com.abril.fera.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.abril.fera.dao.CreatePageDAOImpl;
import br.com.abril.fera.dao.ProductDAO;
import br.com.abril.fera.dao.TemplateDAO;
import br.com.abril.fera.model.CreatePage;
import br.com.abril.fera.model.GroupPage;
import br.com.abril.fera.model.Product;
import br.com.abril.fera.model.Template;
import br.com.abril.fera.support.errors.FeraErrors;
import br.com.abril.fera.support.factory.FileFactory;
import br.com.abril.fera.support.tipos.TipoPageEnum;

@Controller
@RequestMapping("/createpages")
public class CreatePageController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String CREATEPAGE_LIST = "createpages/CreatePageList";
	private static final String CREATEPAGE_FORM = "createpages/CreatePageForm";
	private static final String REDIRECT_CREATEPAGES = "redirect:/createpages/";

	@Autowired
	private CreatePageDAOImpl createpageDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private TemplateDAO templateDAO;

	@Autowired
	private FeraErrors feraErrors;
	@Autowired
	private FileFactory fileFactory;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping("/")
	public String showListCreatePage(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		List<CreatePage> list = createpageDAO.listByProductId(product.getId());
		model.addAttribute("listCreatePages", list);
		return CREATEPAGE_LIST;
	}

	@RequestMapping("/new")
	public String newCreatePage(ModelMap model,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		model.addAttribute("createpage", new CreatePage());
		selectPageBasic(model,product);
		return CREATEPAGE_FORM;
	}


	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editCreatePage(ModelMap model, @PathVariable String id,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");
		CreatePage createpage = createpageDAO.get(id);
		model.addAttribute("createpage", createpage);
		selectPageBasic(model,product);
		return CREATEPAGE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteCreatePage(@PathVariable String id) {
		createpageDAO.delete(id);
		return REDIRECT_CREATEPAGES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveCreatePage(ModelMap model, @Valid @ModelAttribute CreatePage createpage, @RequestParam("files") List<MultipartFile> files, Errors errors,HttpServletRequest request) {
		Product product = (Product) request.getSession().getAttribute("useMarca");

		if (errors.hasErrors()) {
			selectPageBasic(model,product);
			return CREATEPAGE_FORM;
		}

//		validadeCreatePageId(createpage);
//		createpageDAO.saveOrUpdate(createpage);
//
//		Product product = productDAO.get(createpage.getProductId());
//		String path = fileFactory.generatePathOfDirectoryProduct(product.getPath());
//		String pathRelative = "/createpages/" + createpage.getId();
//		path = path + pathRelative;


		return REDIRECT_CREATEPAGES;
	}
	
	
	@RequestMapping(value="/layout/{id}", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
	public @ResponseBody String layoutId(@PathVariable int id) throws Exception {
		Template template = templateDAO.get(id);
		return template.getDocument();
	}
	@RequestMapping(value="/componente/{id}", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
	public @ResponseBody String componenteId(@PathVariable int id) throws Exception {
		Template template = templateDAO.get(id);
		return template.getDocument();
	}
	

	private void validadeCreatePageId(CreatePage createpage) {
		if (StringUtils.isEmpty(createpage.getId()))
			createpage.setId(null);
	}

	private String getMessageSource(String key) {
		return messageSource.getMessage(key, null, null);
	}

	private void selectPageBasic(ModelMap model, Product product) {
		model.addAttribute("listTemplateLayout", templateDAO.listByProduct(product));
		model.addAttribute("listType", listPageTypeItens());
		model.addAttribute("listGroupPage", new ArrayList<GroupPage>());
	}

	private ArrayList<TipoPageEnum> listPageTypeItens() {
		return new ArrayList<TipoPageEnum>(Arrays.asList(TipoPageEnum.values()));
	}

}
