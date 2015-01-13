// DIV Inicio
		afc_div_width = '620';
		afc_div_padding = 0;
		afc_div_margin = 0;
		afc_div_background_color = 'FFFFFF';
		afc_div_border = '0';
		afc_div_border_color = '335F72';
		afc_div_float = 'left';
		afc_div_fonte = 'Arial';
		afc_div_font_size = '10';
		// DIV Fim

		// Titulo Inicio
		afc_tit_texto = 'Links Patrocinados';
		afc_tit_fonte_cor = '95948f';
		afc_tit_tam_fonte = '11';
		afc_tit_fonte_estilo = 'normal';
		afc_tit_fonte_line_height = '12';
		afc_tit_text_align = 'right';
		afc_tit_fonte = 'Arial';
		afc_tit_margin = 0;
		// Titulo Fim

		// UL Inicio
		afc_ul_padding = 0;
		afc_ul_margin = 0;
		afc_ul_border = 0;
		afc_ul_list_style = 'none';
		// UL Fim

		// Linha 1 Inicio
		afc_lin1_color = cor;
		afc_lin1_font_size = 14;
		afc_lin1_height = 13;
		afc_lin1_font_weight = 'bold';
		afc_lin1_text_decoration = 'none';
		afc_li_padding_top = 8;
		afc_lin1_margin = 8;
		afc_lin1_border = 0;
		afc_lin1_line_height = 12;
		afc_lin1_font = 'Arial';
		afc_lin1_height = 13;
		afc_lin1_font_height = 13;
		// Linha 1 Fim

		// Linha 2 Inicio
		afc_lin2_color = '333';
		afc_lin2_font_size = 13;
		afc_lin2_line_height = '14';
		afc_lin2_font_weight = 'normal';
		afc_lin2_text_decoration = 'none';
		afc_lin2_padding_top = 2;
		afc_lin2_font = 'Arial';
		afc_lin2_margin = '0';
		// Linha 2 Fim

		// Linha 3 Inicio
		afc_lin3_color = cor;
		afc_lin3_font_size = 12;
		afc_lin3_line_height = 12;
		afc_lin3_font_weight = 'normal';
		afc_lin3_text_decoration = 'underline';
		afc_lin3_padding_top = 1;
		afc_lin3_font = 'Arial';
		afc_lin3_margin = '0';
		// Linha 3 Fim

		// Config Google Inicio
		google_ad_client = 'ca-abril_js';
		google_ad_channel = channel;
		google_ad_output = 'js';
		google_max_num_ads = '3';
		google_ad_type = 'text';
		google_encoding = 'iso-8859-1';
		google_safe = 'high';
		google_adtest = 'off';

function google_ad_request_done(google_ads) {
	if (google_ads.length == 0) {
		return;
	}
	//Cria??o da DIV
	var s = '<div id="afc_links" style="width:'+afc_div_width+'px; background-color:#'+afc_div_background_color+'; border:'+afc_div_border+'px solid #'+afc_div_border_color+'; padding:'+afc_div_padding+'px; clear:both; float:'+afc_div_float+'; font-size:'+afc_div_font_size+'px; margin:'+afc_div_margin+'px; font:'+afc_div_fonte+';">';

	if (google_ads[0].type == "text") {
		s += '<h3 style="font-family:'+afc_tit_fonte+'; font-size:'+afc_tit_tam_fonte+'px; font-weight:'+afc_tit_fonte_estilo+'; line-height:'+afc_tit_fonte_line_height+'px; color:#'+afc_tit_fonte_cor+'; margin:'+afc_tit_margin+'px; text-align:'+afc_tit_text_align+';">'+afc_tit_texto+'</h3>';
		s += '<ul style="padding:'+afc_ul_padding+'px; margin:'+afc_ul_margin+'px; border:'+afc_ul_border+'px; list-style:'+afc_ul_list_style+'; ;">';
		if (google_ads.length > 0) {
			for(i=0; i < google_ads.length; ++i) {
				s+=
					// Primeira linha do An?ncio
					'<li style="list-style:none ; padding-top:'+afc_li_padding_top+'px ;"><h4 style="margin-top:'+afc_lin1_margin+'px ; border:'+afc_lin1_border+'px ; padding-top:2px ; line-height:'+afc_lin1_line_height+'px ; height:'+afc_lin1_height+'px ; margin-bottom: 0px; font-size: 14px;"><a style="font-family:'+afc_lin1_font+' ; font-size:'+afc_lin1_font_size+'px ; font-weight:'+afc_lin1_font_weight+' ; font-height:'+afc_lin1_font_height+'px ; color:#'+afc_lin1_color+' ; text-decoration:'+afc_lin1_text_decoration+' ;" href="'+google_ads[i].url+'" target="_blank">'+google_ads[i].line1+'</a></h4>'+
					// Texto do An?ncio
					'<p style="font-family:'+afc_lin2_font+' ; font-size:'+afc_lin2_font_size+'px ; font-weight:'+afc_lin2_font_weight+' ; line-height:'+afc_lin2_line_height+'px ; color:#'+afc_lin2_color+' ; margin:'+afc_lin2_margin+'px; padding-top:'+afc_lin2_padding_top+'px; text-decoration:'+afc_lin2_text_decoration+' ;">' + google_ads[i].line2 + ' ' + google_ads[i].line3 + '</p>'+
					// Link final do An?ncio
					'<p style="font-family:'+afc_lin3_font+'; font-size:'+afc_lin3_font_size+'px; line-height:'+afc_lin3_line_height+'px; padding-top:'+afc_lin3_padding_top+'px; margin:'+afc_lin3_margin+'px;"><a style="color:#'+afc_lin3_color+'; font-weight:'+afc_lin3_font_weight+'; text-decoration:'+afc_lin3_text_decoration+';" href="'+google_ads[i].url+ '"  target="_blank">'+google_ads[i].visible_url+ 
					'</a></p></li>';
			}
		} 
		s += '</ul>';
	}
	s += '</div>';

	document.write(s);
}
// Config Google Fim