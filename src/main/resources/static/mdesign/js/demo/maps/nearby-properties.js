'use strict';

function initMap() {
    var contentString = [
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '1',
            img: '',
            price: '',
            latitude: '40.73399',
            longitude: '-73.89345'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '21 Shop St, San Francisco',
            img: '',
            price: '',
            latitude: '40.73385',
            longitude: '-73.89520'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '2',
            img: '',
            price: '',
            latitude: '40.7332229',
            longitude: '-73.8825551'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '3',
            img: '',
            price: '',
            latitude: '40.73814',
            longitude: '-73.89646'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '4',
            img: '',
            price: '',
            latitude: '40.73683',
            longitude: '-73.89673'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '5',
            img: '',
            price: '',
            latitude: '40.73312',
            longitude: '-73.89375'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '6',
            img: '',
            price: '',
            latitude: '40.73542',
            longitude: '-73.89122'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '7',
            img: '',
            price: '',
            latitude: '40.73738',
            longitude: '-73.88815'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '8',
            img: '',
            price: '',
            latitude: '40.73103',
            longitude: '-73.88917'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '9',
            img: '',
            price: '',
            latitude: '40.73148',
            longitude: '-73.88982'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '10',
            img: '',
            price: '',
            latitude: '40.73786',
            longitude: '-73.89866'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '11',
            img: '',
            price: '',
            latitude: '40.73190',
            longitude: '-73.89466'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '12',
            img: '',
            price: '',
            latitude: '40.73113',
            longitude: '-73.88457'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '13',
            img: '',
            price: '',
            latitude: '40.73264',
            longitude: '-73.89734'
        },
        {
            title: 'Etiam ultrices vel quam eget bibendum',
            location: '14',
            img: '',
            price: '',
            latitude: '40.73386',
            longitude: '-73.89974'
        },
        {
            title: '15',
            location: '15',
            img: '',
            price: '',
            latitude: '40.73672',
            longitude: '-73.89356'
        },
    ]


    // Basic options for a simple Google Map
    // For more options see: https://developers.google.com/maps/documentation/javascript/reference#MapOptions
    var mapOptions = {
        // How zoomed in you want the map to start at (always required)
        zoom: 15,
        disableDefaultUI: true,
        scrollwheel: false,

        // The latitude and longitude to center the map (always required)
        center: new google.maps.LatLng(40.7352391, -73.8913099), // New York

        // How you would like to style the map.
        // This is where you would paste any style found on Snazzy Maps.
        styles: [{"featureType":"landscape","stylers":[{"hue":"#FFBB00"},{"saturation":43.400000000000006},{"lightness":37.599999999999994},{"gamma":1}]},{"featureType":"road.highway","stylers":[{"hue":"#FFC200"},{"saturation":-61.8},{"lightness":45.599999999999994},{"gamma":1}]},{"featureType":"road.arterial","stylers":[{"hue":"#FF0300"},{"saturation":-100},{"lightness":51.19999999999999},{"gamma":1}]},{"featureType":"road.local","stylers":[{"hue":"#FF0300"},{"saturation":-100},{"lightness":52},{"gamma":1}]},{"featureType":"water","stylers":[{"hue":"#0078FF"},{"saturation":-13.200000000000003},{"lightness":2.4000000000000057},{"gamma":1}]},{"featureType":"poi","stylers":[{"hue":"#00FF6A"},{"saturation":-1.0989010989011234},{"lightness":11.200000000000017},{"gamma":1}]}]
    };

    // Get the HTML DOM element that will contain your map
    // We are using a div with id="map" seen below in the <body>
    var mapElement = document.getElementById('nearby-properties');

    // Create the Google Map using our element and options defined above
    var map = new google.maps.Map(mapElement, mapOptions);

    var mapMarkerImg;

    if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
        mapMarkerImg = new google.maps.MarkerImage('img/icons/map_marker@2x.png', null, null, null, new google.maps.Size(20,28));
    }
    else {
        mapMarkerImg = 'img/icons/map_marker.png'
    }

    var infowindow = new google.maps.InfoWindow();

    var marker, prop;

    for (prop in contentString) {
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(contentString[prop].latitude, contentString[prop].longitude),
            map: map,
            icon: mapMarkerImg
        });

        google.maps.event.addListener(marker, 'click', (function(marker, prop) {
            return function() {
                infowindow.setContent(contentString[prop].location+'</div><div>'+contentString[prop].latitude+'</div></div>');
                infowindow.open(map, marker);
            }
        })(marker, prop));
    }
}
