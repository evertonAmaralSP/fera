package br.com.abril.mamute.support.tipos;

public enum TipoPageEnum {
	MATERIA("materia"),HOME("home"),ESPECIAL("especial");
	private String value;

	private TipoPageEnum(String tipo) {
		this.value = tipo;
	}
  public String toString() { return value; }

}
