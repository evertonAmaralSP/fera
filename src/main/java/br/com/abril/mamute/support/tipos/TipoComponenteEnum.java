package br.com.abril.mamute.support.tipos;

import java.util.HashMap;
import java.util.Map;

public enum TipoComponenteEnum {
	MATERIA("materia"),AUTOMATICO("automatico"),MANUAL("manual"),ESTATICO("estatico");
	private String value;

	private TipoComponenteEnum(String tipo) {
		this.value = tipo;
	}
  public String toString() { return value; }

  private static Map<String, TipoComponenteEnum> relations;  
  public static TipoComponenteEnum get(String value) {  
    return relations.get(value);  
  } 
  static {  
    relations = new HashMap<String, TipoComponenteEnum>();  
    for(TipoComponenteEnum tipoComponenteEnum : values()) relations.put(tipoComponenteEnum.toString(), tipoComponenteEnum);      
  } 
}
