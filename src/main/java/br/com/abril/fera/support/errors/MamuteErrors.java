package br.com.abril.fera.support.errors;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MamuteErrors {
	
	private Map<String, Object> errors;
	
	public MamuteErrors() {
		errors = new HashMap<String, Object>();
  }
	public Object get(String key) {
		return errors.get(key);
	}
	public boolean is(String key) {
		 return errors.get(key)!=null;
	}
	
	public void addError(String key, Object value){
		errors.put(key, value);
	}
	
	public void clean(){
		errors.clear();
	}

}
