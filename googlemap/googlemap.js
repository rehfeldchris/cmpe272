function initialize() {
    var string = "<h1>Santa Claus</h1><p>Merry Christmas<p>";
    var infowindow = new google.maps.InfoWindow({
        content: string
    });
    var lineCoordinates = [
        new google.maps.LatLng(-25.363882, 131.044922),
        new google.maps.LatLng(-35.363882, 140.044922),
        new google.maps.LatLng(-20.363882, 165.044922)
    ];
    var myLatlng = new google.maps.LatLng(-25.363882, 131.044922);
    var mapOptions = {
        zoom: 4,
        center: myLatlng
    }
    var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
    var line = new google.maps.Polyline({
        path: lineCoordinates,
        map: map,
        geodesic: true
    });
    for (var i = 0; i < 3; i++) {
        var marker = new google.maps.Marker({
            map: map,
            title: "Hello World!",
            //draggable: true,
            animation: google.maps.Animation.DROP,
            position: lineCoordinates[i]//,
            //icon: 'http://www.operationlettertosanta.com/Christmas%20images/logos/VistaICO_Christmas/PNG/Santa-Claus.png'
        });
    };
    google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map, marker);
    });
}
google.maps.event.addDomListener(window, 'load', initialize);
