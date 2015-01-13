function ViajeAqui_installAbrilIDWidgetEventHooks() {
  if (typeof(widgetEventManager) === 'undefined') {
    setTimeout(ViajeAqui_installAbrilIDWidgetEventHooks, 100);
    return;
  }

  setApplication('VIAJE_AQUI');

  widgetEventManager.listenTo('login:success', function(json) {
    var html = '<ul class="logged_in closed">' +
      '<li class="user_name"><img class="user_avatar">' + json.person.name + '<div></div></li>' +
      '<li><a href="/minha_viagem">Minhas viagens</a></li>' +
      '<li><a onclick="renderLogoutWidget(\'login_widget\');" href="javascript: void(0);">Sair</a></li>' +
      '</ul>';

    var $html = jQuery(html);
    $html.find('img').attr('src', json.person.avatar_url);
    jQuery('div.login').html($html);
    window.location.reload();
  });

  widgetEventManager.listenTo('logout:success', function(json) {
    jQuery('#logged_in_user_options').hide();
    jQuery('#logged_out_user_options').show();
    window.location.reload();
  });
}

//jQuery(document).ready(ViajeAqui_installAbrilIDWidgetEventHooks);
