package br.com.abril.fera.support.tipos;

import java.util.HashMap;
import java.util.Map;

public enum TipoPageEnum {
	MATERIA("materia"),HOME("home"),ESPECIAL("especial");
	private String value;

	private TipoPageEnum(String tipo) {
		this.value = tipo;
	}
  public String toString() { return value; }
  
  private static Map<String, TipoPageEnum> relations;  
  public static TipoPageEnum get(String value) {  
    return relations.get(value);  
  } 
  static {  
    relations = new HashMap<String, TipoPageEnum>();  
    for(TipoPageEnum tipoPageEnum : values()) relations.put(tipoPageEnum.toString(), tipoPageEnum);      
  }
}
