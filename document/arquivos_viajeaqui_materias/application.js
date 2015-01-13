var ViajeAqui = window.ViajeAqui || {};

(function($) {
  ViajeAqui.Menu = function(selector) {
    var $this = this;

    $this.selector = selector;
    var clickEvent = 'createTouch' in document ? 'touchstart' : 'click';

    $(selector).bind(clickEvent, function() {
      var menuLink = $(this).find("a:first").get(0);

      if (clickEvent === 'click' && menuLink !== event.target && !$.contains(menuLink, event.target))
        return;

      //event.preventDefault();
      $this.toggle();
    });

    $(selector).bind('mouseenter', function() {
      $this.show();
    });

    $(selector).bind('mouseleave', function() {
      $this.hide();
    });
  };
  

  $.extend(ViajeAqui.Menu.prototype, {
    show: function() {
      $(this.selector).addClass('hover').find('> .drop-sub').addClass('open');
    },

    hide: function() {
      $(this.selector).removeClass('hover').find('> .drop-sub').removeClass('open');
    },

    toggle: function() {
      if ($(this.selector).find('> .drop-sub').css('visibility') === 'hidden') {
        this.show();
      } else {
        this.hide();
      }
    }
  });

  $(function() {
		$("'.menu-links > ul > li:has(ul)'").each(function() {
			var menu = new ViajeAqui.Menu(this);
		});
  });
})(jQuery);

var ViajeAqui         = window.ViajeAqui || {};
ViajeAqui.Models      = ViajeAqui.Models || {};
ViajeAqui.Controllers = ViajeAqui.Controllers || {};

(function(jQuery) {
ViajeAqui.Models.Media = function(properties) {
  this.url          = properties.url;
  this.thumbUrl     = properties.thumbUrl;
  this.author       = properties.author;
  this.description  = properties.description;
  this.title        = properties.title;
  this.selected     = false;
};

ViajeAqui.Controllers.MediaController = function() {
  this.collection = [];
  this.views = [];

  this.select = function(index) {
    if (!this.collection[index]) return false;

    if (this.currentlySelectedIdx !== undefined) {
      this.collection[this.currentlySelectedIdx].selected = false;
    }

    this.currentlySelectedIdx = index;
    this.collection[this.currentlySelectedIdx].selected = true;
    this.notifyObservers('image:selected', [this.collection[this.currentlySelectedIdx], this.currentlySelectedIdx]);
  };

  this.addObserver = function(event, observer) {
    jQuery(this).bind(event, function() {
      var args = Array.prototype.slice.apply(arguments);
      args.shift();
      observer.apply(observer, args);
    });
  };

  this.notifyObservers = function(event, data) {
    jQuery(this).trigger(event, data);
  };

  this.registerView = function(view) {
    this.views.push(view);
    for(var ev in view.eventHandlers) {
      if(view.eventHandlers.hasOwnProperty(ev)) {
        this.addObserver(ev, function() {
          view.eventHandlers[ev].apply(view, arguments);
        });
      }
    }
    view.collection = this.collection;
  };

  var self = this;
  var interval = setInterval(function() {
    var hash = document.location.hash;
    var idx = hash.replace(/^#/, "");

    if (idx !== '') {
      var newIdx = parseInt(idx, 10) - 1;
      if (newIdx !== self.currentlySelectedIdx) {
        self.select(newIdx);
      }
    }
  }, 100);

  this.stopListeningToHashChange = function() {
    clearInterval(interval);
  };
};
})(jQuery);

var path_materia = null;
var load = null;
var ViajeAqui = window.ViajeAqui || {};

function loadPublicity(index) {
    AJAX_path = _cmAbr["setPath"] + '/foto-' + index;
    AJAX_positions = ['abrAD_leaderboard1, abrAD_rectangle1'];
    PUBLICITY.init();
};



(function(jQuery) {

ViajeAqui.ImagePlayer = function(imagesCollection) {
  jQuery("#gallery-wrapper").removeClass('gallery-wrapper');
  jQuery("#gallery-contents").removeClass('hidden');
  
  var _element    = jQuery('<div class="image-player">');
  var sliderView  = new ViajeAqui.ImagePlayer.SliderView();
  var displayView = new ViajeAqui.ImagePlayer.DisplayView();

  this.controller = new ViajeAqui.Controllers.MediaController();
  this.controller.collection = imagesCollection;
  this.controller.registerView(sliderView);
  this.controller.registerView(displayView);

  this.render = function() {
    _element.empty();

    sliderView.render();
    _element.append(sliderView.getElement());

    displayView.render();
    _element.append(displayView.getElement());
    
    jQuery(".ampliar-galeria").fadeIn();
    
  };

  this.getElement = function() {
    return _element;
  };

  this.paintIn = function(domElement) {
    domElement.replaceWith(_element);
    _element.find('.viajeaqui-ui-component').trigger('paint');
  };
};

ViajeAqui.ImagePlayer.SliderView = function() {
  var _element = jQuery('<div class="slider-gallery viajeaqui-ui-component">')
    .bind('paint', function() {
      var $this      = jQuery(this);
      var thumbs     = $this.find('li');
      var thumbSize  = thumbs.outerWidth(true);
      var totalWidth = thumbs.length * thumbSize;

      $this.find('ul').css('width', totalWidth);
    });

  function bindViewEvents(self) {
    _element.find('.previous').click(function() { self.slidePrevious.apply(self, arguments); return false; });
    _element.find('.next').click(function() { self.slideNext.apply(self, arguments); return false; });
  }

  this.render = function() {
    var thumbs = '', image;

    for (var i=0; i < this.collection.length; i++) {
      image = this.collection[i];
      
      //if((path_materia == 'undefined') || (path_materia == null)){
        thumbs +=
          '<li>' +
          '<a href="#' + (Number(i) + 1) + '" onclick="javascript:loadPublicity(' + (Number(i) + 1) +');">' +
          '<img src="' + image.thumbUrl + '">' +
          '</a>' +
          '</li>';
	  //}
	  /*else {
	  	thumbs +=
          '<li>' +
          '<a href="' + path_materia + '?foto=' + (Number(i) + 1) + '#' + (Number(i) + 1) + '">' +
          '<img src="' + image.thumbUrl + '">' +
          '</a>' +
          '</li>';
	  }*/
    }

    _element.html(
        '<span class="previous"><a href="#">Anterior</a></span>' +
        '<div class="slider-pics"><ul>' + thumbs + '</ul></div>' +
        '<span class="next"><a href="#">Próximo</a></span>'
    );

    bindViewEvents(this);
  };

  var imageSize;
  function slide(dir) {
    var operator = dir > 0 ? '+=' : '-=';
    imageSize = imageSize || _element.find('li').outerWidth(true);
    _element.find('.slider-pics').animate({scrollLeft: operator + (imageSize * Math.abs(dir))});
  }

  this.imageSelected = function(image, index) {
    var listItems = _element.find('li');
    listItems.removeClass('selected');
    var selected = listItems.filter('li:eq(' + index + ')');

    if (selected.get(0)) {
      selected.addClass('selected');

      var selectedOffset = selected.position().left,
          containerWidth = _element.find('.slider-pics').innerWidth(),
          listItemSize   = listItems.outerWidth(true),
          visibleOffset  = 2;

      var position;
      if (selectedOffset > containerWidth) {
        position = Math.round((selectedOffset - containerWidth) / listItemSize) + 1;
      } else if (selectedOffset < 0) {
        position = Math.round(selectedOffset / listItemSize);
      }

      if (position) {
        slide(position);
      }
    }
  };

  this.getElement = function() { return _element; };

  this.slidePrevious = function() { slide(-1); };
  this.slideNext     = function() { slide(+1); };

  this.eventHandlers = {
    'image:selected': this.imageSelected
  };
};

ViajeAqui.ImagePlayer.DisplayView = function() {
  var self = this;
  var _element = jQuery('<div class="picture-display">');
  var _displayingIdx;

  function renderCounter() {
     var counter =
        '<p class="counter">' +
        'Foto <span>' + (_displayingIdx + 1) + '</span> de ' + self.collection.length +
        '</p>';

    return counter;
  }

  function renderControls() {
    var controls    = '';
    var previousIdx = self.collection[_displayingIdx - 1] ? _displayingIdx : '';
    var nextIdx     = self.collection[_displayingIdx + 1] ? _displayingIdx + 2 : '';

	//if((path_materia == 'undefined') || (path_materia == null)) {
    	controls += previousIdx ?
      		'<a class="img-nav previous-img" onclick="javascript:loadPublicity(' + previousIdx +');" href="#' + previousIdx + '" title="Anterior">Anterior</a>' : '<a class="img-nav previous-img" onclick="javascript:loadPublicity(' + self.collection.length +');" href="#' + self.collection.length + '" title="Anterior">Anterior</a>';

    	controls += nextIdx ?
      		'<a class="img-nav next-img" onclick="javascript:loadPublicity(' + nextIdx +');" href="#' + nextIdx + '" title="Próxima">Próxima</a>' : '<a class="img-nav next-img" onclick="javascript:loadPublicity(' + 1 +');" href="#' + 1 + '" title="Próxima">Próxima</a>';
	/*}
	else {
		controls += previousIdx ?
      		'<a class="img-nav previous-img" href="' + path_materia + '?foto=' + previousIdx + '#' + previousIdx + '" title="Anterior">Anterior</a>' : '<a class="img-nav previous-img" href="' + path_materia + '?foto=' + self.collection.length + '#' + self.collection.length + '" title="Anterior">Anterior</a>';

    	controls += nextIdx ?
      		'<a class="img-nav next-img" href="' + path_materia + '?foto=' + nextIdx + '#' + nextIdx + '" title="Próxima">Próxima</a>' : '<a class="img-nav next-img" href="' + path_materia + '?foto=' + 1 + '#' + 1 + '" title="Próxima">Próxima</a>';
		
	}*/
    return controls;
  }

  this.render = function(image) {
    var author = '';
    var picture = '';
    var description = '';
    var controls = '';
    var counter = '';
    var title = '';

    if (!isNaN(_displayingIdx)) {
        image       = this.collection[_displayingIdx];
      
        var str = image.title; 
        var new_title = (str !== undefined) ? str.replace(/<[^>]+>/g, "") : "";
        picture     = '<img src="' + image.url + '" alt="'+ new_title +'" title="'+ new_title +'" />';
        picture     = '<img src="' + image.url + '" alt="'+ image.title +'" title="'+ image.title +'" />';
        author      = '<p class="fotografo">Foto: ' + image.author + '</p>';
        title       = '';
        description = '<p>' + image.description + '</p>';
        counter     = renderCounter();
        controls    = renderControls();
    }

    _element.html(
        '<div class="picture-body">' +
          '<div class="picture-header">' +
            counter + author +
          '</div>' +
          picture + controls +
       '</div>' +
       '<div class="picture-text">' +
         title + description +
       '</div>'
    );
  };

  this.getElement = function() {
    return _element;
  };

  this.imageSelected = function(image, index) {
    _displayingIdx = index;
    this.render();
  };

  this.eventHandlers = {
    'image:selected': this.imageSelected
  };
};

ViajeAqui.ImagePlayer.DataSource = function(element) {
  var _element = element;

  this.getData = function() {
    var collection = [];

    _element.find('li').each(function() {
      var $this = jQuery(this);
      var anchor = $this.find('a');

      var properties = {
        url: anchor.attr('href'),
        thumbUrl: anchor.data('thumb'),
        author: $this.find('span').text(),
        description: $this.find('div').html(),
        title: anchor.eq(0).text()
      };

      collection.push( new ViajeAqui.Models.Media(properties) );
    });

    return collection;
  };
};

ViajeAqui.ImagePlayer.jQueryPlugin = function() {
  var $this           = jQuery(this);
  var dataSource      = new ViajeAqui.ImagePlayer.DataSource($this);
  var imageCollection = dataSource.getData();
  var imagePlayer     = new ViajeAqui.ImagePlayer(imageCollection);

  imagePlayer.render();
  imagePlayer.controller.select(0);
  imagePlayer.paintIn($this);

  return imagePlayer.getElement();
};


jQuery.fn.imagePlayer = ViajeAqui.ImagePlayer.jQueryPlugin;

})(jQuery);

jQuery(function($) {
  $('.image-player-content').imagePlayer();
});


var ViajeAqui = window.ViajeAqui || {};

(function($) {
  ViajeAqui.Template = function(template_name) {
    this.template_element = $("#" + template_name + "-tmpl");
    this.placeholder      = $(this.template_element).parent();
    if (this.template_element === null && typeof(console) !== 'undefined') {
      console.log("Can't find template with name " + template_name);
    }
  };

  $.extend(ViajeAqui.Template.prototype, {
    render: function(data) {
      this.placeholder.empty();
      this.template_element.tmpl(data).appendTo(this.placeholder);
    }
  });

  ViajeAqui.FieldSwapper = function(element) {
    this.element = $(element);

    var match = this.element.data('fields').match(/^([^,]+),([^,]+)$/);
    this.field_a = $("#" + match[1]);
    this.field_b = $("#" + match[2]);
  };

  ViajeAqui.FieldSwapper.prototype.swap = function() {
    var aux = this.field_a.val();
    this.field_a.val(this.field_b.val());
    this.field_b.val(aux);
  };

})(jQuery);

/*
 * Core extensions
 */
Array.prototype.count = function(func) {
  var count = 0;

  for (var i = 0; i < this.length; i++) {
    if (func(this[i])) {
      count++;
    }
  }

  return count;
};

Number.prototype.round = function(dec) {
  return Math.round(this * Math.pow(10, dec)) / Math.pow(10, dec);
};

Number.prototype.toCurrency = function() {
  var match   = this.round(2).toString().match(/^(\d+)(?:\.(\d+))?$/),
      integer = match[1],
      decimal = match[2];

  if (!decimal) {
    decimal = "00";
  } else if (decimal.length === 1) {
    decimal = decimal + "0";
  }

  return integer + "," + decimal;
};

ViajeAqui.bind = function(object, binding) {
  for (var member_name in object) {
    if (typeof(object[member_name]) === 'function') {
      (function() {
        var member = object[member_name];

        object[member_name] = function() {
          return member.apply(binding, arguments);
        };
      })();
    }
  }
};

jQuery(function($) {
  var LOADING_IMG = 'http://'+ window.assets_host + '/images/loading.gif';
  var clickEvent = document.createTouch ? 'touchend' : 'click';

  /*
   * Dropdown menus
   */
  $('.search-header-options ul li.first, #show-other-options').live(clickEvent, function(e){
    e.preventDefault();
    $(this).parents('ul').toggleClass('closed');
  });

  $('.logged_in').live(clickEvent + ' hover', function(e) {
    if (e.type === clickEvent) {
      $(this).toggleClass('closed');
    } else if (e.type === 'mouseleave' && !$(this).hasClass('closed')) {
      $(this).addClass('closed');
    }
  });
  
  try{
    $('.recommend.dynamic-editorial').slides({
      preload: false,
      preloadImage: false,
      pause: 500,
      hoverPause: true,
      generateNextPrev: false,
      pagination: false,
      generatePagination: false,
      container: "dynamic-editorial-container"
    });
    $('.especial.dynamic-chamadas').slides({
      preload: true,
      preloadImage: LOADING_IMG,
      pause: 500,
      hoverPause: true,
      generateNextPrev: false,
      pagination: false,
      generatePagination: false,
      container: "dynamic-editorial-chamadas"
    });
    $('#slides').slides({
      preload: false,
      preloadImage: false,
      play: 5000,
      pause: 500,
      hoverPause: true,
      generateNextPrev: false,
      pagination: false,
      generatePagination: false,
      container: "slides_container"
    });    
  }catch(e){}

  /*
   * Map
   */
  if (ViajeAqui.Map) {
    var element   = $('#map'),
        strategy  = element.data('brasil') ? 'MapLink' : 'GoogleMaps',
        map       = new ViajeAqui.Map.Canvas(element, new ViajeAqui.Map.Strategies[strategy]());
  }

  /*
   * Map Routes
   */

  if ($('#route-form').length && ViajeAqui.MapTraceRoutesView) {
    var RouteForm = ViajeAqui.MapTraceRoutesView.RouteForm;

    RouteForm.initialize();

    $('.other-route-options a').live('click', function(e) {
      e.preventDefault();
      var $this = $(this),
          parent = $this.parents('.other-route-options'),
          type = parent.hasClass('origin-points') ? 'origin' : 'destination';

      $('#' + type).val($this.text());
      $('#route-form').submit();
    });

    var loading = $('<img class="loading-icon" src="'+('http://'+ window.assets_host + '/images/ajax-loader.gif')+'">');

    var onlyOneAddress = false;

    $('.mode-rote-option').live('click', function(e) {
        if($(this).val() === "1"){
          $('.destination').hide();
          $('#inverter-destino-origem').hide();
          $('#destination-block').hide();
          $('#show-other-options').hide();
          onlyOneAddress = true;
          $(".route-information, .route-description-container, .trace-route-views").css('display', 'none');
        }else{
          $('.destination').show();
          $('#inverter-destino-origem').show();
          $('#destination-block').show();
          $('#show-other-options').show();
          onlyOneAddress = false;
          $(".route-general-info-one-point").css('display', 'none');
        }
    });

    $('#route-form').live('submit', function(e) {
      $('.other-route-options').slideUp();
      e.preventDefault();


      var $form = $(this), submit = $form.find('#show-route');

      var originField      = $("#origin");
      var destinationField = $("#destination");
      var origin = originField.val();
      var destination = destinationField.val();

      var emptyFields = !origin;
      originField.toggleClass('field-error', !origin);

      if(!onlyOneAddress){
        destinationField.toggleClass('field-error', !destination);
        emptyFields = !destination;
      }

      $('.error-messages')
        .find('p').hide()
        .filter('.empty-route-point').toggle(emptyFields);

      if (emptyFields) {
        return false;
      }

      submit.after(loading);


      if(onlyOneAddress){

          if (ViajeAqui.Map) {
            var element   = $('#map'),
                strategy  = element.data('brasil') ? 'MapLink' : 'GoogleMaps',
                map       = new ViajeAqui.Map.Canvas(element, new ViajeAqui.Map.Strategies[strategy]());
          }

        ViajeAqui.Geocoder.geocode(origin, origin, function(origin_addr, destination_addr) {
          var options = RouteForm.getRouteOptions();

          var invalidAddresses = (!origin_addr.length || !destination_addr.length);
          originField.toggleClass('field-error', !origin_addr.length);
          destinationField.toggleClass('field-error', !destination_addr.length);
          $('.error-messages .invalid-route-point').toggle(invalidAddresses);

          if (invalidAddresses) {
            loading.remove();
            return false;
          }

          options.origin = origin_addr[0];
          options.destination = destination_addr[0];

          map.renderRouteOneAddress(options, function(data) {
            loading.remove();
            var view = new ViajeAqui.MapTraceRoutesView();
            view.originAddresses = origin_addr;
            view.destinationAddresses = destination_addr;
            view.routeData = data;
            view.renderOtherAddressOptions(); //Renderiza a rota e os detalhes
            // view.renderRouteStops();
            // view.renderFuelConsumption();
            // view.renderTollInformation();
            // view.renderTotalDistance();
            // view.renderRouteDescription();

            $(".route-information, .route-description-container, .trace-route-views").css('display', 'none');
          });
        });


      }else{
        if (ViajeAqui.Map) {
          var element   = $('#map'),
              strategy  = element.data('brasil') ? 'MapLink' : 'GoogleMaps',
              map       = new ViajeAqui.Map.Canvas(element, new ViajeAqui.Map.Strategies[strategy]());
        }

      ViajeAqui.Geocoder.geocode(origin, destination, function(origin_addr, destination_addr) {
        var options = RouteForm.getRouteOptions();

        var invalidAddresses = (!origin_addr.length || !destination_addr.length);
        originField.toggleClass('field-error', !origin_addr.length);
        destinationField.toggleClass('field-error', !destination_addr.length);
        $('.error-messages .invalid-route-point').toggle(invalidAddresses);

        if (invalidAddresses) {
          loading.remove();
          return false;
        }

        options.origin = origin_addr[0];
        options.destination = destination_addr[0];

        map.renderRoute(options, function(data) {
          loading.remove();
          var view = new ViajeAqui.MapTraceRoutesView();
          view.originAddresses = origin_addr;
          view.destinationAddresses = destination_addr;
          view.routeData = data;
          view.renderOtherAddressOptions(); //Renderiza a rota e os detalhes
          view.renderRouteStops();
          view.renderFuelConsumption();
          view.renderTollInformation();
          view.renderTotalDistance();
          view.renderRouteDescription();

          $(".route-information, .route-description-container, .trace-route-views").css('display', 'block');
        });
      });
    }

    });
  }
  /*
   * Field swapping
   */
  $(".swap-field-values").live('click', function(e) {
        var originVal = $('input[name=origin_city]').val();
		var destinationVal = $('input[name=destination_city]').val();
		$('input[name=origin_city]').val(destinationVal);
		$('input[name=destination_city]').val(originVal);
  });

  /*
   * Toggler
   */
  $(".toggler").each(function() {
    var toggler         = $(this),
        togglee_id      = $(this).data('togglee'),
        togglee         = $("#" + togglee_id),
        primary_label   = $(this).text(),
        secondary_label = $(this).data('secondary-label');

    toggler.bind('click', function(e) {
      e.preventDefault();
      var callback;

      if (secondary_label) {
        callback = function() {
          if (primary_label === toggler.text()) {
            toggler.text(secondary_label);
          } else {
            toggler.text(primary_label);
          }
        };
      } else {
        callback = null;
      }

      togglee.slideToggle(250, 'linear', callback);
    });
  });

  /*
   * Comentarios
   */
  if ($('.comment-item').length > 0) {
    var stripped_url = document.location.toString().split("#");

    if (stripped_url.length > 1) {
      $('.comment-item').show();
      $('.more-comments').hide();
    }
  }

  $('a.more-comments').live('click', function(e) {
    e.preventDefault();
    var $comments = $(".comment-list .comment-item:hidden");

    $comments.slice(0, 5).show();

    if ($comments.length <= 5) {
      $(this).hide();
      $('a.all-comments').hide();
    }
  });

  $('a.all-comments').live('click', function(e) {
    e.preventDefault();
    var $comments = $(".comment-list .comment-item:hidden");
    $comments.show();
    $(this).hide();
    $('a.more-comments').hide();
  });

  if($('p.success, p.error').length > 0){
    location.href = location.href +  "#comment_form";
  }

  /**
   * Fields placeholders
   */
  $("[data-placeholder]")
    .live("focus", function(event) {
      var text = $(this).val(),
          placeholder = $(this).data("placeholder");

      if (text === placeholder) $(this).val("").removeClass("placeholder");
    })
    .live("blur", function(event) {
      var text = $.trim($(this).val()),
          placeholder = $(this).data("placeholder");

      if (text === "") $(this).val(placeholder).addClass("placeholder");
    });

    /* We need this to make sure content loaded async gets binded */
    setInterval(function() {
      $("[data-placeholder]").each(function() {
        if (!$(this).data('placeholder-loaded')) {
          $(this).blur().data('placeholder-loaded', true);
        }
      });
    }, 100);

    $("form").live("submit ajax:before", function(event) {
      $("[data-placeholder]", this).focus();
    });

  /**
   * Async loading of blocks
   */
  $("[data-async]").each(function() {
    var url = $(this).data('async');
	if (url == "/assine_rodape") {
		$(this).load(url, function(){
			$.getScript('http://barrasassine.abril.com.br/novabarra/viagem.js');
		});
	}
	else {
    	$(this).load(url);
	}
  });
});

(function($) {
  $.fn.toggleTransition = function() {
    if ($(this).css('visibility') === 'visible') $(this).transitionOut();
    else $(this).transitionOut();
  };

  $.fn.transitionIn = function() {
    $(this).css("visibility", "visible").css("opacity", 1);
  };

  $.fn.transitionOut = function() {
    var $this = this;
    $this.css("opacity", 0);
    setTimeout(function() { $this.css("visibility", "hidden"); }, 300);
  };
})(jQuery);

var ViajeAqui = window.ViajeAqui || {};
(function($) {

  ViajeAqui.CommentBar = function(selector) {
    var $this = this;
    $this.selector = selector;
    $this.links_selector = 'li a';

    $(selector + ' ' + $this.links_selector).live('click', function(e) {
      e.preventDefault();
      $this.classify($(this).closest('li').attr('class'));
    });
  };

  $.extend(ViajeAqui.CommentBar.prototype, {
    element: function(){
      return $(this.selector);
    },

    selected: function() {
      return $(this.links_selector, this.element()).filter(".select").closest('li').attr('class');
    },

    classify: function(classification){
      $(this.links_selector, this.element()).removeClass('select')
        .closest("li." + classification + " a").addClass('select');
    }
  });

  var commentBar = new ViajeAqui.CommentBar('.classification-field');

  $('.place-comment-form').live('ajax:before', function(e) {
    $('#comentario_filtros_extras').val(JSON.stringify({'classificacao': commentBar.selected()}));
  }).live('ajax:beforeSend', function(e) {
    if ($('textarea', this).val().length < 5) {
      $('.message').addClass('error').text('Preencha os comentários e selecione uma avaliação');
      return false;
    }

    $('input[type=submit]').attr('disabled', true);
  }).live('ajax:success', function() {
    document.location.reload();
  });
})(jQuery);

var ViajeAqui = window.ViajeAqui || {};

(function($) {
  ViajeAqui.RatingBar = function(selector) {
    var $this = this;

    $this.selector       = selector;
    $this.links_selector = '.dot-rating li a';
    $this.desc_selector  = '#ratings-description span';
    $this.selectedLevel  = 0;

    $($this.links_selector).live({
      'click': function(e) {
        e.preventDefault();
        $("#voto_nota").val($(this).data('rating'));
        $this.rate($(this).data('rating'));
      },

      'mouseover': function(e) {
        $this.preview($(this).data('rating'));
      },

      'mouseout': function(e) {
        $this.reset();
      }
    });
  };

  function setRateCircles(level) {
    $(this.links_selector, this.element()).removeClass("selected")
      .filter(':lt(' + level + ')').addClass("selected");
  }

  function setRateDescription(level) {
    var descText = $(this.links_selector, this.element()).filter(':eq(' + (level - 1) + ')').data('rating-description'),
        descEl   = $(this.desc_selector, this.element());

    if (descText) {
      descEl.addClass("rating-helper-tooltip").text(descText);
    } else {
      descEl.removeClass("rating-helper-tooltip").text("");
    }
  }

  $.extend(ViajeAqui.RatingBar.prototype, {
    element: function() {
      if (!this._element) {
        this._element = $(this.selector);
      }

      return this._element;
    },

    preview: function(level) {
      setRateCircles.call(this, level);
      setRateDescription.call(this, level);
    },

    reset: function() {
      this.preview(this.selectedLevel);
    },

    rate: function(level) {
      this.preview(level);
      this.selectedLevel = level;
    }
  });

  var ratingBar = new ViajeAqui.RatingBar('.ratings-field');

  function logRegister(callback){
    var conteudo = $(".amenity-ratings-form").serializeArray();
    jQuery.ajax({
      dataType: "json",
      url: "/votes/register",
      type: "post",
      data: conteudo,
      complete: function(){
        if(typeof callback === "function") callback.call();
      }
    });
  }
  $('.amenity-ratings-form').live('ajax:before', function(e) {
    $('#comentario_filtros_extras').val(JSON.stringify({'rating': ratingBar.selectedLevel}));
  }).live('ajax:beforeSend', function(e) {
    if (ratingBar.selectedLevel === 0 || $('textarea', this).val().length < 5) {
      $('.message').addClass('error').text('Preencha os comentários e selecione uma avaliação');
      return false;
    }

    $('input[type=submit]').attr('disabled',true);
  }).live('ajax:success', function() {
    logRegister(function(){
      document.location.reload();
    });
  });
})(jQuery);
var ViajeAqui = window.ViajeAqui || {};

(function($) {
  var _private = {
    'link': function() {
      return $(this.linkSelector);
    },

    'message': function(message, className) {
      $(this.messagesSelector).html(message + ' <span class="down-arrow"/>').removeClass().addClass(className);
    },

    'notifySuccess': function() {
      _private.message(
        'Tudo certo: esta página agora faz parte da sua viagem', 'added-to-bookmarks-with-success');
    },

    'notifyError': function() {
      _private.message(
        'Esta página já consta na sua viagem :)', 'already-added-to-bookmarks');
    },

    'isLoggedIn': function() {
      return !_private.link().hasClass('trigger-login');
    },

    'bookmarkPath': function() {
      return _private.link().data('bookmark-path');
    },

    'store': function() {
      $.ajax({
        'type'    : 'POST',
        'url'     : '/minha_viagem',
        'data'    : {'bookmark': {'path': _private.bookmarkPath()}},
        'success' : _private.notifySuccess,
        'error'   : _private.notifyError
      });
    },

    'authenticateAndStore': function() {
      widgetEventManager.listenTo('login:success', _private.store);
      renderLoginWidget('login_widget');
    }
  };

  ViajeAqui.Bookmarks = function(linkSelector, messagesSelector) {
    var $this = this;
    $this.linkSelector     = linkSelector;
    $this.messagesSelector = messagesSelector;

    $($this.linkSelector).live('click', function(e) {
      e.preventDefault();
      $this.save();
    });

    ViajeAqui.bind(_private, this);
  };

  $.extend(ViajeAqui.Bookmarks.prototype, {
    'save': function() {
      if (_private.isLoggedIn()) {
        _private.store();
      } else {
        _private.authenticateAndStore();
      }
    }
  });

  var bookmarks = new ViajeAqui.Bookmarks('a.share-viagem', '#bookmark-messages');

  ViajeAqui.BookmarksHelpMessage = {
    initialize: function (showLink, hideLink, message) {
      showLink.click(function() {
        message.css('display', 'block');
        return false;
      });
      hideLink.click(function() {
        message.css('display', 'none');
        return false;
      });
    }
  };
})(jQuery);

var ViajeAqui    = window.ViajeAqui || {};
ViajeAqui.Models = ViajeAqui.Models || {};
ViajeAqui.News   = ViajeAqui.News   || {};

ViajeAqui.Models.News = function(props) {
  this.source     = props.source || 'Sem Fonte';
  this.title      = props.title;
  this.created_at = props.created_at;
  this.url        = props.url;
};

ViajeAqui.Models.News.prototype = {
  sourceClass: function() {
    return this.source.toLowerCase().replace(/\s+/, '-');
  },

  formattedDate: function() {
    if (this.created_at) {
		  var date = new Date(Number(this.created_at) * 1000);
		  return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();
    }

    return "";
  },

  truncatedTitle: function() {
    if (this.title.length > 120) {
      var stop = (this.title.charAt(120) === " ") ? 120 : this.title.indexOf(" ", 120);
      return this.title.substring(0, stop) + " ...";
    }
    return this.title;
  }
};

ViajeAqui.News.DataSource = function(resourceUrl) {
  this.resourceUrl = resourceUrl;
};

ViajeAqui.News.DataSource.prototype = {
  createNewsObject: function(rawData) {
    return new ViajeAqui.Models.News(this.translateObj(rawData));
  },
  getData: function(callback) {
    var self = this;
    var collection = [];

    jQuery.getJSON(self.resourceUrl, function(data) {
      jQuery.each(self.getMembers(data), function(i, member) {
        collection.push(self.createNewsObject(member));
      });
      callback.call(callback, collection);
    });
  },
  getMembers: function(result) {
    return result;
  },
  translateObj: function(rawData) {
    return rawData;
  }
};

ViajeAqui.News.SearchAPIDataSource = function(resourceUrl) {
  this.resourceUrl = resourceUrl;
};

ViajeAqui.News.SearchAPIDataSource.prototype = new ViajeAqui.News.DataSource();

ViajeAqui.News.SearchAPIDataSource.prototype.translateObj = function(searchEntry) {
  return {
    source:     searchEntry['fonte-meta_nav'] || 'viajeaqui',
    title:      searchEntry['article-meta_nav'],
    url:        searchEntry['url'],
    created_at: searchEntry['criada_em-meta_nav']
  };
};

ViajeAqui.News.SearchAPIDataSource.prototype.getMembers = function(collection) {
  return collection.resultado.members;
};

ViajeAqui.News.SearchAPIDataSource.APIUrl = "http://viajeaqui.abril.com.br/noticias/ultimas?v33";


var ViajeAqui = window.ViajeAqui || {};

(function($) {
  var News = ViajeAqui.News || {};

  News.Widget = function(collection) {
    this.collection = collection;

    var element = $('<div class="latest-news">');
    this.getElement = function() {
      return element;
    };
  };

  News.Widget.prototype = {
    render: function() {
      var header = $("<div class=\"title\"><h2>" + this.title + "</h2>" +
        "<span> <a href=\"" + this.viewAllLink + "\">" + this.viewAllText + "</a></span>" +
        "<div class=\"borda\"></div></div>");

      var list = News.Widget.NewsListRenderer.render(this.collection, this.maxEntries);

      this.getElement().empty().append(header).append(list);
    },

    paintIn: function(element) {
      element.replaceWith(this.getElement());
    }
  };

  News.Widget.NewsListRenderer = {
    render: function(collection, manyEntries) {
      var list = [];
      var maxEntries = manyEntries || collection.length;

      for(var i = 0; i < maxEntries; i++) {
        if(collection[i])
          list[i] = this.renderItem(collection[i]);
      }

      return '<ul>' + list.join('') + '</ul>';
    },
    renderItem: function(model) {
      return "<li class=\"item\"><strong class=\"" + model.sourceClass() + "\">" +
             model.source + "</strong> <span>" + model.formattedDate() + "</span>" +
             "<h2><a href=\"" + model.url + "\">"  + model.truncatedTitle() + "</a></h2></li>";
    }
  };

})(jQuery);


var ViajeAqui = window.ViajeAqui || {};

(function($) {

  var LatestNews = ViajeAqui.LatestNews = {};

  LatestNews.jQueryPlugin = function() {
    $(this).each(function() {

      var $this      = $(this);
      var dataSource = new ViajeAqui.News.SearchAPIDataSource(ViajeAqui.News.SearchAPIDataSource.APIUrl);

      dataSource.getData(function(collection) {
        LatestNews.jQueryPlugin.renderWidget($this, collection);
      });
    });
  };

  LatestNews.jQueryPlugin.renderWidget = function(element, collection) {
    var widget  = new ViajeAqui.News.Widget(collection);
    var $element = $(element);

    widget.title       = $element.data('title');
    widget.viewAllLink = $element.data('view-all-link');
    widget.viewAllText = $element.data('view-all-text');
    widget.maxEntries  = $element.data('max-entries');

    widget.render();
    widget.paintIn($element);
  };

  $.fn.latestNewsWidget = LatestNews.jQueryPlugin;
  $('.latest-news').latestNewsWidget();

})(jQuery);

var ViajeAqui = window.ViajeAqui || {};

(function($) {
  $.fn.editorialListWidget = function() {
    $(this).each(function() {
      var $this      = $(this);
      var maxEntries = $this.data('max-entries') || 5;
      var url        = $this.data('lista-editorial') + "?max-entries=" + maxEntries;

      $.get(url, function(result) {
        $this.replaceWith(result);
      });
    });
  };
})(jQuery);

jQuery(function($) {
  $('.editorial-list').editorialListWidget();
});
