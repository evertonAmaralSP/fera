package br.com.abril.mamute.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import br.com.abril.mamute.dao.CreatePageDAOImpl;
import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.dao.TemplateDAO;
import br.com.abril.mamute.model.CreatePage;
import br.com.abril.mamute.model.GroupPage;
import br.com.abril.mamute.support.errors.MamuteErrors;
import br.com.abril.mamute.support.factory.FileFactory;
import br.com.abril.mamute.support.tipos.TipoPageEnum;

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
	private MamuteErrors mamuteErrors;
	@Autowired
	private FileFactory fileFactory;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping("/")
	public String showListCreatePage(ModelMap model) {
		List<CreatePage> list = createpageDAO.list();
		model.addAttribute("listCreatePages", list);
		return CREATEPAGE_LIST;
	}

	@RequestMapping("/new")
	public String newCreatePage(ModelMap model) {
		model.addAttribute("createpage", new CreatePage());
		selectPageBasic(model);
		return CREATEPAGE_FORM;
	}


	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String editCreatePage(ModelMap model, @PathVariable String id) {
		CreatePage createpage = createpageDAO.get(id);
		model.addAttribute("createpage", createpage);
		selectPageBasic(model);
		return CREATEPAGE_FORM;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String deleteCreatePage(@PathVariable String id) {
		createpageDAO.delete(id);
		return REDIRECT_CREATEPAGES;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveCreatePage(ModelMap model, @Valid @ModelAttribute CreatePage createpage, @RequestParam("files") List<MultipartFile> files, Errors errors) {

		if (errors.hasErrors()) {
			selectPageBasic(model);
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

	private void validadeCreatePageId(CreatePage createpage) {
		if (StringUtils.isEmpty(createpage.getId()))
			createpage.setId(null);
	}

	private String getMessageSource(String key) {
		return messageSource.getMessage(key, null, null);
	}

	private void selectPageBasic(ModelMap model) {
		model.addAttribute("listProduct", productDAO.list());
		model.addAttribute("listTemplateLayout", templateDAO.list());
		model.addAttribute("listType", listPageTypeItens());
		model.addAttribute("listGroupPage", new ArrayList<GroupPage>());
	}

	private ArrayList<TipoPageEnum> listPageTypeItens() {
		return new ArrayList<TipoPageEnum>(Arrays.asList(TipoPageEnum.values()));
	}

}
