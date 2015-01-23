package br.com.abril.fera.support.tipos;

public enum TipoRecursoEnum {
	MATERIA("materia"),IMAGEM("imagem"),GALERIA_MULTIMIDIA("galeria_multimidia");
	private String value;

	private TipoRecursoEnum(String tipo) {
		this.value = tipo;
	}
  public String toString() { return value; }

}
