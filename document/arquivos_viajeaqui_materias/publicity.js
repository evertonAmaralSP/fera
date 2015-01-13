
PUBLICITY = {}
var AJAX_positions = [];
var AJAX_path = '';

PUBLICITY.init = function(){
    for (i=0;i<AJAX_positions.length;i++){
		PUBLICITY.position(AJAX_positions[i]);
	}
	PUBLICITY.pageView();
}

PUBLICITY.pageView = function(){
	_abrMetrics.callHit(AJAX_path)
}
PUBLICITY.position = function(position){
	abrAd.refreshAds(position);
	//alert(position);
	//url = '/publicidade/peca?ABR_pos=' + position + '&ABR_page=' + ABR_page;
	//$('#abr_' + position).html('<iframe id="banner_'+ position + '" style="width:100%; height:100%; overflow: hidden; border: 0px;" border="0" src="' + url + '">');
	
}
