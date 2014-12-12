package br.com.abril.mamute.support.slug;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SlugUtilTest {


	 SlugUtil slug = new SlugUtil();


	@Test
	public void testVariacaoTexto() {
		assertTrue(resultado1.equals(slug.toSlug(text1)));
		assertTrue(resultado2.equals(slug.toSlug(text2)));
		assertTrue(resultado3.equals(slug.toSlug(text3)));

	}

	private String text1 = "açucar em ação para àh";
	private String text2 = "Cai a participação de investidores na compra de imóveis";
	private String text3 = "Banco devolve R$ 52 mi após acordo sobre contas de Maluf";
	private String resultado1 = "acucar-em-acao-para-ah";
	private String resultado2 = "cai-a-participacao-de-investidores-na-compra-de-imoveis";
	private String resultado3 = "banco-devolve-r-52-mi-apos-acordo-sobre-contas-de-maluf";

}
