/* Version: 1.19, Date: 07/08/2014 */
/* Update: Add new IVC tag
/* Conf site */

_abrConfSite = {
   	'codeAbril'  : 'UA-5652544-1' ,
	'additionalCodes'   : { "SITE": 'UA-11416880-11', "ALEXANDRIA" : 'UA-32393717-1'}, 
	'multDomain' : false , 
	'enableIVC'	 : true ,
	'enableComScore' : true ,	
	
	initOldTracker: function() {
		   _gaq.push(function() { 
			   pageTracker  = _gat._createTracker(_abrConfSite['codeAbril'], 'abrTracker'); 
			});
	}
}

_abrMetrics = {
  abrParams : [] ,
  
  load : function (){
  	  var loc	= new String (document.location);
  	  
 	  this.abrParams['path']   = this.mkPath();
	  
	  if (loc.indexOf('.abril.') >-1 ){
 		  this.abrParams['domain'] = ".abril.com.br"; 
 	  } else {
 		  this.abrParams['domain'] = "none"; 
 	  }
	   	  
	  this.callHit();
  },
  
  mkPath : function(){
  	 var qs = window.location.search.substring(1);
	 var p =  '' ;
	 
	 if ((typeof _cmAbr != 'undefined') && (_cmAbr["setPath"] != null)) 
	 	p +=  _cmAbr["setPath"];
	 else
		p += window.location.pathname;	

     if (qs!="") p += "?" + qs ;

	 return (p);
  },
  
  callHit : function(p){
	  
	  var path = (p != null) ? p : this.abrParams['path'] ; 
	  
	  window._gaq = window._gaq || [];

  	  _gaq.push(['_setAccount',    _abrConfSite['codeAbril']]);
  	  _gaq.push(['_setDomainName', this.abrParams['domain']]);
  	 
  	  if (window._abrConfSite.initOldTracker) _abrConfSite.initOldTracker(); 
      
  	  if (_abrConfSite['enableIVC']){
		_gaq.push(['_setLocalGifPath', document.location.protocol + '//ivc.abril.com.br/__utm.gif' ]); 
	  	_gaq.push(['_setLocalRemoteServerMode', true]);
	  }

	  _gaq.push(['_trackPageview', path ]);
  	  
	  for(var acc in _abrConfSite.additionalCodes) {
	  
	  	_gaq.push([acc + '._setAccount', _abrConfSite.additionalCodes[acc]]);
	    
		if (_abrConfSite['multDomain']) {	
			_gaq.push([acc + '._setDomainName', 'none']);
			_gaq.push([acc + '._setAllowLinker', true]);	  	
		} else {
			_gaq.push([acc + '._setDomainName', this.abrParams['domain']]);
		}

	  	_gaq.push([acc + '._trackPageview', path]);

	  }

	  
  	  (function() {
			var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
			ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
			var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  	   })();
	   
	    if (_abrConfSite['enableComScore']){
	    	_abrMetrics.callComScore();
	    }	

	    if (_abrConfSite['enableIVC']){
			_abrMetrics.callIVC();
	    }
  },
  
  callComScore: function(p) {
  	 window._comscore = window._comscore || [];
	_comscore.push({ c1: "2", c2: "6906435" });
 
	(function() {
        var s = document.createElement("script"), el = document.getElementsByTagName("script")[0];
        s.async = true; s.src = (document.location.protocol == "https:" ? "https://sb" : "http://b") + ".scorecardresearch.com/beacon.js";
        el.parentNode.insertBefore(s, el); 
 	})();    
  },

  callIVC: function(p) {
  	 var ivcUrl = ("https:" == document.location.protocol ? "https://datpdpmej1fn2.cloudfront.net/" :
    "http://ivccftag.ivcbrasil.org.br/") + "ivc.js";
    ;(function(p,l,o,w,i,n,g){if(!p[i]){p.GlobalIvcNamespace=p.GlobalIvcNamespace||[];
    p.GlobalIvcNamespace.push(i);p[i]=function(){(p[i].q=p[i].q||[]).push(arguments)
    };p[i].q=p[i].q||[];n=l.createElement(o);g=l.getElementsByTagName(o)[0];n.async=1;
    n.src=w;g.parentNode.insertBefore(n,g)}}(window,document,"script",ivcUrl,"ivc")); 

    window.ivc('newTracker', 'cf', 'ivccf.ivcbrasil.org.br', {
        idWeb: '76'
    }); 

    window.ivc('trackPageView'); 
  }  
  
}

_abrMetrics.load();