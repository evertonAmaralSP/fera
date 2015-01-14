package br.com.abril.mamute.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.abril.mamute.dao.ProductDAO;
import br.com.abril.mamute.model.Product;

@Component
public class TemporarioInterceptor implements HandlerInterceptor {
	
	@Autowired
	private ProductDAO productDAO;
	
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
    	if(request.getSession().getAttribute("useMarca")==null){
    		Product product = productDAO.get(1);
	  		List<Product> listaMarcas = productDAO.list();
	  		request.getSession().setAttribute("useMarca", product);
	  		request.getSession().setAttribute("listaMarcas", listaMarcas);
      }
  		System.out.println("Remover e delegar para autenticação a a escolha da marca");
      return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }
 
    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception exception)
            throws Exception {
    }
}
