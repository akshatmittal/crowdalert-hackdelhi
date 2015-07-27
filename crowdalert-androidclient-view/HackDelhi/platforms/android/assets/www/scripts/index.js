(function () {
    "use strict";

    document.addEventListener( 'deviceready', onDeviceReady.bind( this ), false );

    function onDeviceReady() {
        // Handle the Cordova pause and resume events
        document.addEventListener( 'pause', onPause.bind( this ), false );
        document.addEventListener( 'resume', onResume.bind( this ), false );

        // TODO: Cordova has been loaded. Perform any initialization that requires Cordova here.
    };

    function onPause() {
        // TODO: This application has been suspended. Save application state here.
    };

    function onResume() {
        // TODO: This application has been reactivated. Restore application state here.
    };
} )();
var t,active_state="0";
$(window).ready(function(){
  setTimeout(function(){
    $(".splash").fadeOut(700);
    t = setTimeout(getStat, 1000);
  }, 2000);
});
var getStat = function() {
  clearInterval(t);
  $.getJSON("http://192.168.2.18/hack-delhi-web/train/2", function(e){
    console.log(e);
    $(".count").html( (e.status)<10?"0"+e.status:e.status );
    animate(e.status);
    t = setTimeout(getStat, 1000);
  });
}
var animate = function(st) {
  var el = $("body");
  var m = parseInt(st);
  if(m == 0){
    m = 0;
  } else if(m == 1){
    m = 10;
  } else if(m<=2) {
    m = 1;
  } else if (m<=4){
    m = 2;
  } else {
    m = 3;
  }
  if(m == active_state) return;
  switch (m) {
    case 1:
    case 10: el.css('background-color','#55a629');break;
    case 2: el.css('background-color','#bfbd16');break;
    case 3: el.css('background-color','#af2525');break;
    default: el.css('background-color','#376cc3');break;
  }
  $(".ac-" + active_state).fadeOut(600);
  $(".ac-" + m).fadeIn(600);
  active_state = m;
}
