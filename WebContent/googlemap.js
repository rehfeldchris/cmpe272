function initialize() {
    var mapCanvas = document.getElementById('map_canvas');
    var mapOptions = {
        center: new google.maps.LatLng(39.9139, 116.3917),
        zoom: 10,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions)

    $.getJSON("http://localhost:8080/plague/JobProcessor?originalInfectedUserId=126&maxNodeHopsFromOrigin=1&maxResultSize=100&minInfectionRangeYards=1000000&maxTimeOfInfectionSpreading=00%3A03&incubationTime=00%3A01&startTime=2008-11-01T03:38:36", function(data)
    {
        var lineCoordinates = new Array();
        $.each(data.userLocationSnapshots, function(index, element)
        {
            latlng = new google.maps.LatLng(element.temporalLocation.lat, element.temporalLocation.lng);
            lineCoordinates.push(latlng);
            //Marker
            var marker = new google.maps.Marker(
            {
                map: map,
                title: element.user.id.toString(),
                animation: google.maps.Animation.DROP,
                position: latlng,
                icon: 'http://localhost:8080/plague/men.png'
            });
            //InfoWindow
            google.maps.event.addListener(marker, 'click', function()
            {
                var infowindow = new google.maps.InfoWindow(
                {
                    content: '<p>user id: ' + marker.title + '</p>'
                });
                infowindow.open(map, marker);
            });
        });
        //Polyline
        var lineSymbol = {
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
        };
        var line = new google.maps.Polyline(
        {
            path: lineCoordinates,
            map: map,
            geodesic: true,
            strokeColor: 'grey',
            icons: [
            {
                icon: lineSymbol,
                offset: '100%'
            }]
        });
    });
}
google.maps.event.addDomListener(window, 'load', initialize);
