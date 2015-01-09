package br.com.abril.mamute.model.editorial;

import com.google.gson.annotations.SerializedName;

public class Transformacoes {
	private Transformacao original;
	@SerializedName("base_crop")
	private Transformacao baseCrop;
	@SerializedName("thumbnail_101x81")
	private Transformacao thumbnail101x81;
	@SerializedName("thumbnail_60x60")
	private Transformacao thumbnail60x60;
	@SerializedName("thumbnail_240x240")
	private Transformacao thumbnail240x240;
	@SerializedName("capa_ng")
	private Transformacao capaNg;
	@SerializedName("thumbnail_120x120")
	private Transformacao thumbnail120x120;
	@SerializedName("thumbnail_480x480")
	private Transformacao thumbnail480x480;
	@SerializedName("capa_vtsidebar")
	private Transformacao capaVtSidebar;
	@SerializedName("capa_190x250")
	private Transformacao capa190x250;
	@SerializedName("capa_ng190x276")
	private Transformacao capaNG190x276;
	@SerializedName("home_150x200")
	private Transformacao home150x200;
	@SerializedName("home_350x250")
	private Transformacao home350x250;
	@SerializedName("home_600x200")
	private Transformacao home600x200;
	@SerializedName("materia_100x60")
	private Transformacao materia100x60;
	@SerializedName("materia_620x350")
	private Transformacao materia620x350;
	@SerializedName("wallpapers_553x415")
	private Transformacao wallpapers553x415;
	@SerializedName("wallpapers_1024x768")
	private Transformacao wallpapers1024x768;
	@SerializedName("wallpapers_800x600")
	private Transformacao wallpapers800x600;
	@SerializedName("carrossel_home_620")
	private Transformacao carrosselHome620;
	@SerializedName("carrossel_pequeno_100")
	private Transformacao carrosselPequeno100;
	@SerializedName("carrossel_pequeno_80")
	private Transformacao carrosselPequeno80;
	@SerializedName("carrousel_home_vt")
	private Transformacao carrouselHomeVt;
	@SerializedName("autor_blog")
	private Transformacao autorBlog;
	@SerializedName("abre_lugar_300")
	private Transformacao abreLugar300;
	@SerializedName("conteudo_relacionado_60")
	private Transformacao conteudoRelacionado60;
	@SerializedName("produto_200")
	private Transformacao produto200;
	@SerializedName("catalogo_140")
	private Transformacao catalogo140;
	@SerializedName("foto_ampliada_842")
	private Transformacao fotoAmpliada842;
	@SerializedName("destaque_300")
	private Transformacao destaque300;
	@SerializedName("sugestoes_viagem_220")
	private Transformacao sugestoesViagem220;
	@SerializedName("listadestinos_80")
	private Transformacao listaDestinos80;
	@SerializedName("thumb_galeria_72")
	private Transformacao thumbGaleria72;
	@SerializedName("foto_galeria_materia_620")
	private Transformacao fotoGaleriaMateria620;
	
	public Transformacao getOriginal() {
		return original;
	}
	public void setOriginal(Transformacao original) {
		this.original = original;
	}
	public Transformacao getBaseCrop() {
		return baseCrop;
	}
	public void setBaseCrop(Transformacao baseCrop) {
		this.baseCrop = baseCrop;
	}
	public Transformacao getThumbnail101x81() {
		return thumbnail101x81;
	}
	public void setThumbnail101x81(Transformacao thumbnail101x81) {
		this.thumbnail101x81 = thumbnail101x81;
	}
	public Transformacao getThumbnail60x60() {
		return thumbnail60x60;
	}
	public void setThumbnail60x60(Transformacao thumbnail60x60) {
		this.thumbnail60x60 = thumbnail60x60;
	}
	public Transformacao getThumbnail120x120() {
		return thumbnail120x120;
	}
	public void setThumbnail120x120(Transformacao thumbnail120x120) {
		this.thumbnail120x120 = thumbnail120x120;
	}
	public Transformacao getThumbnail240x240() {
		return thumbnail240x240;
	}
	public void setThumbnail240x240(Transformacao thumbnail240x240) {
		this.thumbnail240x240 = thumbnail240x240;
	}
	public Transformacao getThumbnail480x480() {
		return thumbnail480x480;
	}
	public void setThumbnail480x480(Transformacao thumbnail480x480) {
		this.thumbnail480x480 = thumbnail480x480;
	}
	public Transformacao getCapaNg() {
		return capaNg;
	}
	public void setCapaNg(Transformacao capaNg) {
		this.capaNg = capaNg;
	}
	public Transformacao getCapaVtSidebar() {
		return capaVtSidebar;
	}
	public void setCapaVtSidebar(Transformacao capaVtSidebar) {
		this.capaVtSidebar = capaVtSidebar;
	}
	public Transformacao getAutorBlog() {
		return autorBlog;
	}
	public void setAutorBlog(Transformacao autorBlog) {
		this.autorBlog = autorBlog;
	}
	public Transformacao getCarrouselHomeVt() {
		return carrouselHomeVt;
	}
	public void setCarrouselHomeVt(Transformacao carrouselHomeVt) {
		this.carrouselHomeVt = carrouselHomeVt;
	}
	public Transformacao getCarrosselHome620() {
		return carrosselHome620;
	}
	public void setCarrosselHome620(Transformacao carrosselHome620) {
		this.carrosselHome620 = carrosselHome620;
	}
	public Transformacao getCarrosselPequeno100() {
		return carrosselPequeno100;
	}
	public void setCarrosselPequeno100(Transformacao carrosselPequeno100) {
		this.carrosselPequeno100 = carrosselPequeno100;
	}
	public Transformacao getWallpapers553x415() {
		return wallpapers553x415;
	}
	public void setWallpapers553x415(Transformacao wallpapers553x415) {
		this.wallpapers553x415 = wallpapers553x415;
	}
	public Transformacao getWallpapers1024x768() {
		return wallpapers1024x768;
	}
	public void setWallpapers1024x768(Transformacao wallpapers1024x768) {
		this.wallpapers1024x768 = wallpapers1024x768;
	}
	public Transformacao getCapa190x250() {
		return capa190x250;
	}
	public void setCapa190x250(Transformacao capa190x250) {
		this.capa190x250 = capa190x250;
	}
	public Transformacao getWallpapers800x600() {
		return wallpapers800x600;
	}
	public void setWallpapers800x600(Transformacao wallpapers800x600) {
		this.wallpapers800x600 = wallpapers800x600;
	}
	public Transformacao getCapaNG190x276() {
		return capaNG190x276;
	}
	public void setCapaNG190x276(Transformacao capaNG190x276) {
		this.capaNG190x276 = capaNG190x276;
	}
	public Transformacao getAbreLugar300() {
		return abreLugar300;
	}
	public void setAbreLugar300(Transformacao abreLugar300) {
		this.abreLugar300 = abreLugar300;
	}
	public Transformacao getConteudoRelacionado60() {
		return conteudoRelacionado60;
	}
	public void setConteudoRelacionado60(Transformacao conteudoRelacionado60) {
		this.conteudoRelacionado60 = conteudoRelacionado60;
	}
	public Transformacao getCarrosselPequeno80() {
		return carrosselPequeno80;
	}
	public void setCarrosselPequeno80(Transformacao carrosselPequeno80) {
		this.carrosselPequeno80 = carrosselPequeno80;
	}
	public Transformacao getProduto200() {
		return produto200;
	}
	public void setProduto200(Transformacao produto200) {
		this.produto200 = produto200;
	}
	public Transformacao getCatalogo140() {
		return catalogo140;
	}
	public void setCatalogo140(Transformacao catalogo140) {
		this.catalogo140 = catalogo140;
	}
	public Transformacao getFotoAmpliada842() {
		return fotoAmpliada842;
	}
	public void setFotoAmpliada842(Transformacao fotoAmpliada842) {
		this.fotoAmpliada842 = fotoAmpliada842;
	}
	public Transformacao getDestaque300() {
		return destaque300;
	}
	public void setDestaque300(Transformacao destaque300) {
		this.destaque300 = destaque300;
	}
	public Transformacao getSugestoesViagem220() {
		return sugestoesViagem220;
	}
	public void setSugestoesViagem220(Transformacao sugestoesViagem220) {
		this.sugestoesViagem220 = sugestoesViagem220;
	}
	public Transformacao getListaDestinos80() {
		return listaDestinos80;
	}
	public void setListaDestinos80(Transformacao listaDestinos80) {
		this.listaDestinos80 = listaDestinos80;
	}
	public Transformacao getThumbGaleria72() {
		return thumbGaleria72;
	}
	public void setThumbGaleria72(Transformacao thumbGaleria72) {
		this.thumbGaleria72 = thumbGaleria72;
	}
	public Transformacao getFotoGaleriaMateria620() {
		return fotoGaleriaMateria620;
	}
	public void setFotoGaleriaMateria620(Transformacao fotoGaleriaMateria620) {
		this.fotoGaleriaMateria620 = fotoGaleriaMateria620;
	}
	public Transformacao getMateria100x60() {
		return materia100x60;
	}
	public void setMateria100x60(Transformacao materia100x60) {
		this.materia100x60 = materia100x60;
	}
	public Transformacao getMateria620x350() {
		return materia620x350;
	}
	public void setMateria620x350(Transformacao materia620x350) {
		this.materia620x350 = materia620x350;
	}
	public Transformacao getHome150x200() {
		return home150x200;
	}
	public void setHome150x200(Transformacao home150x200) {
		this.home150x200 = home150x200;
	}
	public Transformacao getHome350x250() {
		return home350x250;
	}
	public void setHome350x250(Transformacao home350x250) {
		this.home350x250 = home350x250;
	}
	public Transformacao getHome600x200() {
		return home600x200;
	}
	public void setHome600x200(Transformacao home600x200) {
		this.home600x200 = home600x200;
	}
	

}
