package br.com.abril.mamute.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.config.SystemConfiguration;
import br.com.abril.mamute.dao.ApplicationDAO;
import br.com.abril.mamute.dao.UploadDAO;
import br.com.abril.mamute.model.Application;
import br.com.abril.mamute.model.Template;
import br.com.abril.mamute.model.Upload;
import br.com.abril.mamute.support.slug.SlugUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/marcas")
public class ApplicationController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String DIR_TMP = SystemConfiguration.getPropertyAsString(SystemConfiguration.DIR_TMP);

	@Autowired
	private ApplicationDAO applicationDao;

	@Autowired
	private SlugUtil slugUtil;

	@Autowired
	private UploadDAO uploadDAO;

	@RequestMapping("/")
	public ModelAndView handleRequest() throws Exception {
		List<Application> listApplications = applicationDao.list();
		ModelAndView model = new ModelAndView("marcas/ApplicationList");
		model.addObject("listApplications", listApplications);
		return model;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newApplication() {
		ModelAndView model = new ModelAndView("marcas/ApplicationForm");
		model.addObject("app", new Application());
		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editApplication(HttpServletRequest request) {
		int applicationId = Integer.parseInt(request.getParameter("id"));
		Application app = applicationDao.get(applicationId);
		ModelAndView model = new ModelAndView("marcas/ApplicationForm");
		model.addObject("app", app);
		return model;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteApplication(HttpServletRequest request) {
		int applicationId = Integer.parseInt(request.getParameter("id"));
		applicationDao.delete(applicationId);
		return new ModelAndView("redirect:/marcas/");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveApplication(@ModelAttribute Application app) {
		applicationDao.saveOrUpdate(app);
		return new ModelAndView("redirect:/marcas/");
	}

	/**
	 * Lista de arquivos gerados pelo template de uma marca.
	 *
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listExports/{id}", method = RequestMethod.GET)
	public ModelAndView listFile(HttpServletRequest request,@PathVariable String id) throws Exception {
		Application app = applicationDao.get(Integer.parseInt(id));

		List<String> listFiles = getFilesInApplication(app);
		ModelAndView model = new ModelAndView("marcas/listaArquivos");
		model.addObject("listFiles", listFiles);

		return model;
	}

	@RequestMapping(value = "/upload/{id}")
	public ModelAndView singleUpload(HttpServletRequest request,@PathVariable String id) {
		int applicationId = Integer.parseInt(id);
		Application app = applicationDao.get(applicationId);

		ModelAndView model = new ModelAndView("marcas/upload");
		model.addObject("app", app);
		return model;
	}

	@RequestMapping(value = "/upload/{id}/save", method = RequestMethod.POST)
	public @ResponseBody String singleSave(@RequestParam("file") MultipartFile file,@PathVariable String id) {

		int applicationId = Integer.parseInt(id);
		Application application = applicationDao.get(applicationId);

		String fileName = null;
		String path = DIR_TMP+ application.getPath();

		if (!file.isEmpty() && validateFileType(file)) {
			logger.debug("File Description: {} ", new Object[] { file.getName() });

			try {

				if("text/css".equalsIgnoreCase(file.getContentType())){
					path=path+"/stylesheets";
				} else if("application/x-javascript".equalsIgnoreCase(file.getContentType()) || "text/javascript".equalsIgnoreCase(file.getContentType()) ){
					path=path+"/javascripts";
					//file.getContentType().matches("(image\\/)*+(jpeg|gif|png)"
				} else if(file.getContentType().matches("(image\\/)*+(jpeg|gif|png)")){
					path=path+"/images";
				} else {
					path=path+"/includes";
				}

				fileName = file.getOriginalFilename();
//				String[] tokens = fileName.split("^([\\w]*)(\\.[\\w]*)$");
//				fileName = slugUtil.toSlug(tokens[0]);
//				String exencao = slugUtil.toSlug(tokens[1]);

				byte[] bytes = file.getBytes();
				FileUtils.forceMkdir(new File(path));
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(path + "/" + fileName )));
				buffStream.write(bytes);
				buffStream.close();
				Upload upload = new Upload();
				upload.setApplication(application);
				upload.setName(fileName);
				upload.setPath(path);
				upload.setType(file.getContentType());

				uploadDAO.saveOrUpdate(upload);

				return "You have successfully uploaded " + fileName;
			} catch (Exception e) {
				logger.error("You failed to upload {} : {} ", new Object[] { fileName,e.getMessage() });
				return "You failed to upload " + fileName + ": " + e.getMessage();
			}
		} else {
			logger.error("Unable to upload. File is valid");
			return "Unable to upload. File is valid.";
		}
	}

	private boolean validateFileType(MultipartFile file) {
		String[] typeValid = {"text/html", "text/x-server-parsed-html", "application/x-javascript", "text/javascript", "image/jpeg", "image/gif", "image/png", "text/css"};
		for (String type : typeValid) {
	     if(type.equalsIgnoreCase(file.getContentType())) {
	    	 return true;
	     }
    }
	  return false;
  }

	private List<String> getFilesInApplication(Application app) {
		List<Template> list = app.getTemplates();
		List<String> listaFile = new ArrayList<String>();
		for (Template template : list) {
			String path = DIR_TMP+ "/" + app.getPath()+"/"+template.getPath();
			File file = new File(path);
			File afile[] = file.listFiles();
			if(afile!=null) {
				for (File arquivo : afile) {
					if(arquivo.getName().matches("^.*\\.(html)$")) {
						listaFile.add(arquivo.getName());
					}
	      }
			}

    }
	  return listaFile;
  }

}
