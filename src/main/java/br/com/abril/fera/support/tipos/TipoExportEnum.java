package br.com.abril.fera.support.tipos;

import java.util.HashMap;
import java.util.Map;

public enum TipoExportEnum {
	S3_AWS("3s Amazonws"),FTP("ftp"),SFTP("sftp");
	private String value;

	private TipoExportEnum(String tipo) {
		this.value = tipo;
	}
  public String toString() { return value; }

  private static Map<String, TipoExportEnum> relations;  
  public static TipoExportEnum get(String value) {  
    return relations.get(value);  
  } 
  static {  
    relations = new HashMap<String, TipoExportEnum>();  
    for(TipoExportEnum tipoExportEnum : values()) relations.put(tipoExportEnum.toString(), tipoExportEnum);      
  } 
}
