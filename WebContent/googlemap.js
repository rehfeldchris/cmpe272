function initialize() {
    var mapCanvas = document.getElementById('map_canvas');
    var mapOptions = {
        center: new google.maps.LatLng(39.9139, 116.3917),
        zoom: 10,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions);

    $(".form-signin").on("submit", function(event){
        var userId = Number($(this).find("input[name=originalInfectedUserId]").val());
        var mockData = getMockedData(userId);
        if (mockData) {
            plotData(mockData);
            return;
        }
        $.ajax({
            url : $(this).attr("action"),
            type: "GET",
            data : $(this).serializeArray(),
            success:function(data) {
                plotData(data.userLocationSnapshots);
            },
            error: function(){
                console.log(arguments);
            }
        });
        event.preventDefault(); //STOP default action
        $("input", this).prop("disabled", "true");
    });

    function plotData(userLocationSnapshots) {
        var lineCoordinates = new Array();
        $.each(userLocationSnapshots, function(index, element)
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
    }

    function getMockedData(userId) {
        switch (userId) {
            case 55:
                return [];

        }
    }



}
google.maps.event.addDomListener(window, 'load', initialize);
