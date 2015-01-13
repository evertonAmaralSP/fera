/*!
 Google Analytics Event Tracking jQuery Plugin

 Copyright (C) 2011 by Building Blocks UK

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 Author: Robert Stevenson-Leggett
 Version: 0.8
 
 Handles event tracking through the use of data attributes

 Version history:

 0.1 - Initial version
 0.2 - Support for direct calling
 0.3 - Bug fixes
 0.4 - Added comments and push to github
 0.5 - Add gaTrackEvent
 0.6 - Bug fixes
 0.7 - Added non interactive option
 0.8 - Bug fix

 Modified: David Duarte de Souza 25/03/2012
 
 Description: slightly modified to ensure that the event tracking will be restricted
 to the second account  
 
*/
(function ($) {

    // Extend the jQuery object with the ga trackEvent function. Other functions can call this method, or
    // it can be called directly from existing JS code to add tracking
    // Can be used anywhere in your Javascript like so:
    // $.ga.trackEvent({ category : 'Category', action : 'Action', label : 'Label', value : 0.0});
    // $.ga.trackEvent({ category : 'Category', action: 'Action' });
    //
    // This also enables unit tests to overide this function for testing purposes.
    $.extend({
        ga: {
            trackEvent: function(args) {
                var defaultArgs = {
                    category : 'Unspecified',
                    action: 'Unspecified',
                    nonInteractive:false
                };
                args = $.extend(defaultArgs,args);
                _gaq.push(['SITE._trackEvent', args.category, args.action, args.label, args.value, args.nonInteractive]);
            }
        }
    });

    var defaultOptions = {
        //The category attribute
        categoryAttribute: 'data-ga-category',
        //The action attribute
        actionAttribute: 'data-ga-action',
        //The label attribute (could be changed to href when tracking file downloads)
        labelAttribute: 'data-ga-label',
        //The value attribute (must be integer)
        valueAttribute: 'data-ga-value',
        //An attribute to indicate whether the event is non-interactive (defaults to false)
        noninteractiveAttribute: 'data-ga-noninteractive',
        //Whether to look for the label
        useLabel: true,
        //Whether to look for a value
        useValue: false,
        //false = track as soon as the plugin loads, true = bind to an event
        useEvent: false,
        //The event to bind to if useEvent is true
        event: 'click',
        //A method to call to check whether or not we should call the tracking when the event is clicked
        valid: function (elem, e) { return true; },
        //Tracking complete
        complete: function (elem, e) { },
        //Category should always be set if using gaTrackEvent
        category: 'Unspecified',
        //Action should always be set if using gaTrackEvent
        action: 'Unspecified',
        //Label can be specified if using gaTrackEvent and useLabel == true
        label: 'Unspecified',
        //value can be specified if using gaTrackEvent and useValue == true
        value: 'Unspecified',
        //non-interactive - only used if using gaTrackEvent
        nonInteractive: false
    };

    //
    // gaTrackEvent adds unobtrusive tracking attributes itself
    // So you can add tracking to links etc.
    //
    // This allows to do sitewide event tracking e.g. Document Downloads just
    // by selecting the elements e.g.
    //
    //
    //  $('.track-download').gaTrackEvent({
    //          category:'Downloads', action:'PDF', useEvent:true, event:'click'
    //    });
    //
    $.fn.gaTrackEvent = function (options) {
        options = $.extend(defaultOptions, options);

        return this.each(function () {
            var element = $(this);
            element.attr(options.categoryAttribute, options.category);
            element.attr(options.actionAttribute, options.action);

            if (options.useLabel == true) {
                element.attr(options.labelAttribute, options.label);
            }
            if (options.useValue == true) {
                element.attr(options.valueAttribute, options.value);
            }
            if (options.nonInteractive == true) {
                element.attr(options.noninteractiveAttribute, "true");
            }
            
            element.gaTrackEventUnobtrusive(options);
        });
    };

    //Create the plugin
    // gaTrackEventUnobtrusive expects you to add the data attributes either via server side code
    // or direct into the mark up.
    $.fn.gaTrackEventUnobtrusive = function (options) {

        //Merge options
        options = $.extend(defaultOptions, options);

        //Keep the chain going
        return this.each(function () {

            var _this = $(this);

            //Wrap the tracking so we can reuse it.
            var callTrackEvent = function () {
                //Retreive the info
                var category = _this.attr(options.categoryAttribute);
                var action = _this.attr(options.actionAttribute);
                var label = _this.attr(options.labelAttribute);
                
				if(options.labelAttribute == "html"){
					label = to_slug(_this.html());
				}
				else if(options.labelAttribute == "urlpath"){
					label = window.location.pathname;
				}
				else if(options.labelAttribute == "urllink"){
					label = getLink(_this.attr('href'));
				}
				else if(options.labelAttribute == "value"){
					label = _this.val();
				}
				else if(options.labelAttribute == "title"){
					label = _this.attr("title");
				}
				else if(options.labelAttribute == "position"){
					label = _this.attr("title");
				}
			
				
								
				
                var value = _this.attr(options.valueAttribute);
                var nonInteractive = _this.attr(options.noninteractiveAttribute);

                var args = {
                    category : category,
                    action : action,
                    nonInteractive : nonInteractive
                };

                if (options.useLabel && options.useValue) {
                   args.label = label;
                   args.value = value;
                }
                else if (options.useLabel) {
                    args.label = label;
                }

                $.ga.trackEvent(args);
            };

            //If we want to bind to an event, do it.
            if (options.useEvent == true) {

                //This is what happens when you actually click a button
                var constructedFunction = function (e) {
                    //Check the callback function
                    if (options.valid(_this, e) === true) {
                        callTrackEvent();
                        options.complete(_this, e);
                    }
                };

                //E.g. if we are going to click on a link
                _this.bind(options.event, constructedFunction);
            }
            else {
                //Otherwise just track immediately (e.g. if we just came from a post-back)
                callTrackEvent();
            }
        });
    };

})(jQuery);

function strip_tags(html){
 
		//PROCESS STRING
		if(arguments.length < 3) {
			html=html.replace(/<\/?(?!\!)[^>]*>/gi, '');
		} else {
			var allowed = arguments[1];
			var specified = eval("["+arguments[2]+"]");
			if(allowed){
				var regex='</?(?!(' + specified.join('|') + '))\b[^>]*>';
				html=html.replace(new RegExp(regex, 'gi'), '');
			} else{
				var regex='</?(' + specified.join('|') + ')\b[^>]*>';
				html=html.replace(new RegExp(regex, 'gi'), '');
			}
		}
 
		//CHANGE NAME TO CLEAN JUST BECAUSE 
		var clean_string = html;
 
		//RETURN THE CLEAN STRING
		return clean_string;
}
	
function getLink(link){
	var er_local = /:3000/;
	var er_prod = /\.com\.br/;
	var barra = /\//;
	var retorno = '';
	if(er_local.test(link)){
		var url = link.split(":3000/");
		retorno = url[1];
	}
	else if(er_prod.test(link)){
		var url = link.split(".com.br/");
		retorno = url[1];	
	}	
	else{
		retorno = link;
	}
	if(barra.test(retorno)){
		retorno = retorno.replace(/\/$/g,"");
		retorno = retorno.replace(/^\//g,"");
		retorno = retorno.replace(/\//g,":");
	}
	return retorno;
}
function to_slug(str) {
  str = str.replace(/^\s+|\s+$/g, ''); // trim
  str = str.toLowerCase();
  
  // remove accents, swap ñ for n, etc
  var from = "àáãâèéëêìíïîòóöôùúüûñç·/_,:;";
  var to   = "aaaaeeeeiiiioooouuuunc------";
  for (var i=0, l=from.length ; i<l ; i++) {
    str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
  }

  str = str.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
    .replace(/\s+/g, '-') // collapse whitespace and replace by -
    .replace(/-+/g, '-'); // collapse dashes

  return str;
}


jQuery(document).ready(function() {
  var controlled_labels = $( 'head' ).find( 'meta[name=controlled_labels]' ).attr( 'content' );
	if(controlled_labels != null){
		var labels = controlled_labels.split(",");
		if(labels.length > 0){
			for(var i=0; i<labels.length; i++){
				_gaq.push(['SITE._trackEvent', "keywords", "view", labels[i], 0, true]);
			}
		}				
	}
	
	
    //Header - Menu
	$(".menu-links a").gaTrackEvent({
	    category: 'menu-superior',
	    action: 'click',
	    labelAttribute: "urllink",
	    useEvent: true,
	    event: 'click'
	});
    //Header - Menu - VT
    $(".menu-nav a").gaTrackEvent({
	    category: 'menu-superior-vt',
	    action: 'click',
	    labelAttribute: "urllink",
	    useEvent: true,
	    event: 'click'
	});
	//Header - Link
	$(".header-destaque a").gaTrackEvent({
	    category: 'em-destaque',
	    action: 'click',
	    labelAttribute: "urllink",
	    useEvent: true,
	    event: 'click'
	});
	
	//Direita Cidades, estados e países - Ver mais
	$(".city-options a").click(function(){
		text = $(this).text();
		_gaq.push(['SITE._trackEvent', "ver-mais", "click", to_slug(text), 0, true]);
	});

	//Botões cidades
	$('.ond_menu_botoes a').click(function(){
		var index = $(this).index();
		var text = '';
		if(index == 0){
			text = "o-que-fazer"
		}
		else if(index == 1){
			text = "onde-comer"
		}
		else if(index == 2){
			text = "onde-ficar"
		}
		if(text != "") {
			_gaq.push(['SITE._trackEvent', "botoes-mais-na-cidade", "click", text, 0, true]);
		}
	})
	
	//Conteúdo - Cidade - Botão 48 horas
	$('.horas48 a').click(function(){
		text = $( 'head' ).find( 'meta[name=city_slug]' ).attr( 'content' );
		_gaq.push(['SITE._trackEvent', "48-horas", "click", text, 0, true]);
	})
	
	
	
	//Conteúdo - Estado - Destinos neste estado
	$('.related-destiny .first ul li a').click(function(){
		var posicao = $(this).parent().parent().parent().index() + 1;
		text =  '1-' + posicao + ':' +getLink($(this).attr('href').replace("cidades/",""))
		_gaq.push(['SITE._trackEvent', "destino-neste-estado", "click", text, 0, true]);
	})
	//Conteúdo - Estado - Destinos neste estado
	$('.related-destiny .second ul li a').click(function(){
		var posicao = $(this).parent().parent().parent().index() + 1;
		text =  '2-' + posicao + ':' +getLink($(this).attr('href').replace("cidades/",""))
		_gaq.push(['SITE._trackEvent', "destino-neste-estado", "click", text, 0, true]);
	})
	
	
	//Conteúdo - Estado - Destinos neste estado - ver todos
	$(".content-destino-estab .related-destiny .title a").click(function(){
		_gaq.push(['SITE._trackEvent', "destino-neste-estado", "click", "ver-todos", 0, true]);
	});
	
	
	//Conteúdo - País/Estado/cidade/estabelecimento - mapa-ampliar
	$(".aside .mapa .zoom a").click(function(){
		_gaq.push(['SITE._trackEvent', "mapa-ampliar", "click", "", 0, true]);
	});
	
	//Conteúdo - País/Estado/cidade/estabelecimento - link-mapa
	$(".aside .mapa .map-options a").eq(0).click(function(){
		_gaq.push(['SITE._trackEvent', "link-mapa", "click", "", 0, true]);
	});
	
	//Conteúdo - País/Estado/cidade/estabelecimento - link-tracar-rota
	$(".aside .mapa .map-options a").eq(1).click(function(){
		_gaq.push(['SITE._trackEvent', "link-tracar-rota", "click", "", 0, true]);
	});
	
	
	//Conteúdo - Estado - Cidades
	$('.slider-destiny ul li a').click(function(){
		var posicao = $(this).parent().index() + 1;
		slug = $( 'head' ).find( 'meta[name=state_slug]' ).attr( 'content' );
		if(slug != undefined) {
			text = posicao + ':' + slug;
			_gaq.push(['SITE._trackEvent', "cidades-neste-estado", "click", text, 0, true]);
		}
		else {
			text = posicao + ':' + $( 'head' ).find( 'meta[name=country_slug]' ).attr( 'content' );		
			_gaq.push(['SITE._trackEvent', "cidades-neste-pais", "click", text, 0, true]);
		}
		
	})
	
	//Conteúdo - Estado - Cidades - ver todos
	$(".content-destino-estab .slider-wrapper .title a").click(function(){
		_gaq.push(['SITE._trackEvent', "cidades-neste-estado", "click", "ver-todos", 0, true]);
	});
	
	//Direita - Últimas notícias
	$('.aside .latest-newsR1 ul li a').click(function(){
		var posicao = $(this).parent().parent().index() + 1;
		text = posicao + ':' +getLink($(this).attr('href'))
		_gaq.push(['SITE._trackEvent', "ultimas-noticias", "click", text, 0, true]);
	})
	
	
	
	//Direita Cidades, estados e países - Ver fotos
	$(".destiny-picture a").click(function(){
		text = getLink($(this).attr('href').replace("/fotos",""));
		_gaq.push(['SITE._trackEvent', "galeria-ver-todas-as-fotos", "click", text, 0, true]);
	});

	
	
	//Conteúdo - Estado - Cidades em destaque
	$('.content-destino-estab ul.lista-cidades-uf li a').click(function(){
		text = getLink($(this).attr('href').replace("cidades/",""));	
		_gaq.push(['SITE._trackEvent', "cidades-em-destaque", "click", text, 0, true]);
	})
	
	//Conteúdo - Encontrar destino neste Estado
	$('.state .aside-search form #buscar-within').click(function(){
		text = $('#local-search').val();
		if(text == ""){
			text = 'null'
		}
		_gaq.push(['SITE._trackEvent', "encontrar-destino-neste-estado", "click", text, 0, true]);
	})
	
	//Conteúdo - Encontre nesta cidade
	$('.city .aside-search form #buscar-within').click(function(){
		text = $('#local-search').val();
		if(text == ""){
			text = 'null'
		}
		_gaq.push(['SITE._trackEvent', "encontre-nesta-cidade", "click", text, 0, true]);
	})
	
		
	
	//Direita - Cidades - Destinos recomandados
	$('.aside .recommendList a').click(function(){
		var posicao = $(this).parent().index() + 1;
		text = posicao + ':' +getLink($(this).attr('href').replace("cidades/",""));
		_gaq.push(['SITE._trackEvent', "destinos-recomendados", "click", text, 0, true]);
	})
	
	//Leia mais - Cidades
	$('.content-destino-estab .recormat .recommendList a').click(function(){
		var posicao = $(this).parent().index() + 1;
		text = posicao + ':' +getLink($(this).attr('href').replace("materias/",""));
		_gaq.push(['SITE._trackEvent', "leia-mais-cidades", "click", text, 0, true]);
	})	
	
	//Leia mais - Cidades - ver todos
	$('.recormat .title span a').click(function(){
		_gaq.push(['SITE._trackEvent', "leia-mais-cidades", "click", 'ver-todos', 0, true]);
	})
	
	
	//Leia mais - Matérias
	$('.recommendListLeiaMais a').click(function(){
		var posicao = $(this).parent().parent().index() + 1;
		text = posicao + ':' +getLink($(this).attr('href'));
		_gaq.push(['SITE._trackEvent', "leia-mais", "click", text, 0, true]);
	})
	
	//Links na legenda de fotos
	$('.picture-text a').click(function(){
		text = getLink($(this).attr('href'));
		_gaq.push(['SITE._trackEvent', "links-lengenda-foto", "click", text, 0, true]);
	})
	
	//Links "veja também" nas Galerias de Fotos
	$('.fotos_full .go-back').click(function(){
		text = getLink($(this).attr('href'));
		_gaq.push(['SITE._trackEvent', "galerias-veja-tambem", "click", text, 0, true]);
	})
	
	//Links nos textos
	$('.text-event a').click(function(){
		text = getLink($(this).attr('href'));
		_gaq.push(['SITE._trackEvent', "links-texto", "click", text, 0, true]);
	})
	
	//Links redes sociais no rodapé
	$('.follow li a').click(function(){
		redes = Array();
		redes[1] = 'facebook';
		redes[3] = 'twitter';
		redes[5] = 'youtube';
		redes[7] = 'orkut';	
		redes[9] = 'aplicativos';	
		redes[11] = 'newsletters';		
		posicao = $(this).parent().index();
		_gaq.push(['SITE._trackSocial', redes[posicao], "acessar"]);
	})
	
	
	
	//Traçar Rota
	$("#swap_adress").click(function(){
		_gaq.push(['SITE._trackEvent', "tracar-rota", "click", "trocar-origem-destino", 0, true]);
	});
	
	$("#view_in_map").click(function(){	
		if($("#origin_city").val() != ""){
			origem = $("#origin_city").val();
			origem = origem.split(",");
			
			text = to_slug(origem[0]) +":"+to_slug(origem[1])			
			_gaq.push(['SITE._trackEvent', "tracar-rota", "add-origem", text, 0, true]);
		}
		
		if($("#destination_city").val() != ""){
			destino = $("#destination_city").val();
			destino = destino.split(",");
			
			text = to_slug(destino[0]) +":"+to_slug(destino[1])			
			_gaq.push(['SITE._trackEvent', "tracar-rota", "add-destino", text, 0, true]);
		}
		
		if($("#price-per-liter").val() != ""){
			_gaq.push(['SITE._trackEvent', "tracar-rota", "add-preco-combustivel", $("#price-per-liter").val(), 0, true]);
		}
		if($("#tank-capacity").val() != ""){
			_gaq.push(['SITE._trackEvent', "tracar-rota", "add-capacidade-tanque", $("#tank-capacity").val(), 0, true]);
		}
		if($("#average-consumption").val() != ""){
			_gaq.push(['SITE._trackEvent', "tracar-rota", "add-consumo-veiculo", $("#average-consumption").val(), 0, true]);
		}
		
		
		
		_gaq.push(['SITE._trackEvent', "tracar-rota", "click", "ver-no-mapa", 0, true]);
	});
	
	$("#zclip-ZeroClipboardMovie_1").click(function(){	
		_gaq.push(['SITE._trackEvent', "tracar-rota", "click", "copiar-link", 0, true]);
	});
	
	$(".js-add-stops").click(function(){	
		_gaq.push(['SITE._trackEvent', "tracar-rota", "click", "add-parada", 0, true]);
	});
	
	$(".btn-print").click(function(){		
		_gaq.push(['SITE._trackEvent', "tracar-rota", "click", "imprimir", 0, true]);
	});
	
	//Botões cidades - Traçar rota
	$('#info-origem .ond_menu_botoes a').click(function(){
		var index = $(this).index();
		var text = '';
		if(index == 0){
			text = "origem:o-que-fazer"
		}
		else if(index == 1){
			text = "origem:onde-comer"
		}
		else if(index == 2){
			text = "origem:onde-ficar"
		}
		if(text != "") {
			_gaq.push(['SITE._trackEvent', "tracar-rota", "click", text, 0, true]);
		}
	})
	
	$('#info-destino .ond_menu_botoes a').click(function(){
		var index = $(this).index();
		var text = '';
		if(index == 0){
			text = "destino:o-que-fazer"
		}
		else if(index == 1){
			text = "destino:onde-comer"
		}
		else if(index == 2){
			text = "destino:onde-ficar"
		}
		if(text != "") {
			_gaq.push(['SITE._trackEvent', "tracar-rota", "click", text, 0, true]);
		}
	})
	
	
	
			

});