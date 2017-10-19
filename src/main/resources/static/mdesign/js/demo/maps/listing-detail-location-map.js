'use strict';

function isRetina(){
    return ((window.matchMedia && (window.matchMedia('only screen and (min-resolution: 192dpi), only screen and (min-resolution: 2dppx), only screen and (min-resolution: 75.6dpcm)').matches || window.matchMedia('only screen and (-webkit-min-device-pixel-ratio: 2), only screen and (-o-min-device-pixel-ratio: 2/1), only screen and (min--moz-device-pixel-ratio: 2), only screen and (min-device-pixel-ratio: 2)').matches)) || (window.devicePixelRatio && window.devicePixelRatio >= 2)) && /(iPad|iPhone|iPod)/g.test(navigator.userAgent);
}

function initMap() {
    var mapElement = document.getElementById('listing-map');
    var myLatLng = {lat: 40.7352391, lng: -73.8913099};
    var mapOptions = {
        zoom: 15,
        disableDefaultUI: true,
       //scrollwheel: false,
        center: new google.maps.LatLng(myLatLng),
        styles: [{"featureType":"landscape","stylers":[{"hue":"#FFBB00"},{"saturation":43.400000000000006},{"lightness":37.599999999999994},{"gamma":1}]},{"featureType":"road.highway","stylers":[{"hue":"#FFC200"},{"saturation":-61.8},{"lightness":45.599999999999994},{"gamma":1}]},{"featureType":"road.arterial","stylers":[{"hue":"#FF0300"},{"saturation":-100},{"lightness":51.19999999999999},{"gamma":1}]},{"featureType":"road.local","stylers":[{"hue":"#FF0300"},{"saturation":-100},{"lightness":52},{"gamma":1}]},{"featureType":"water","stylers":[{"hue":"#0078FF"},{"saturation":-13.200000000000003},{"lightness":2.4000000000000057},{"gamma":1}]},{"featureType":"poi","stylers":[{"hue":"#00FF6A"},{"saturation":-1.0989010989011234},{"lightness":11.200000000000017},{"gamma":1}]}]
    };
    var mapMarkerImg;

    if(isRetina()) {
        mapMarkerImg = new google.maps.MarkerImage('img/icons/map_marker@2x.png', null, null, null, new google.maps.Size(20,28));
    }
    else {
        mapMarkerImg = 'img/icons/map_marker.png'
    }

    var map = new google.maps.Map(mapElement, mapOptions);

    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        icon: mapMarkerImg
    });
}

$(document).ready(function () {
    $('body').on('shown.bs.tab', 'a[href="#detail-media-map"]', function () {
        initMap();
    });
});