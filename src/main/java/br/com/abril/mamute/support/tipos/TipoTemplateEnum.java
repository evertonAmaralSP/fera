package br.com.abril.mamute.support.tipos;

import java.util.HashMap;
import java.util.Map;

public enum TipoTemplateEnum {
	LAYOUT("layout-page-master",1,"Layout de template de Pagina"),COMPONENTE("componente",2,"Template para Componetes de pagina"),FULL_PAGE("full-page-template",3,"Template Pagina Completa");
	private String name;
	private Integer id;
	private String description;

	private TipoTemplateEnum(String name,Integer id, String description) {
		this.name = name;
		this.id = id;
		this.description = description;
	}
	public String toString() { return name; }
	public String getName() { return name; }
	public Integer getId() { return id; }
	public String getDescription() { return description; }

  private static Map<String, TipoTemplateEnum> relations;  
  public static TipoTemplateEnum get(String value) {  
    return relations.get(value);  
  } 
  static {  
    relations = new HashMap<String, TipoTemplateEnum>();  
    for(TipoTemplateEnum tipoComponenteEnum : values()) relations.put(tipoComponenteEnum.getName(), tipoComponenteEnum);      
  } 
}
