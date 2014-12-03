function initialize() {
    var mapCanvas = document.getElementById('map_canvas');
    var mapOptions = {
        center: new google.maps.LatLng(39.9139, 116.3917),
        zoom: 10,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions);

    $(".form-signin").on("submit", function(event){
        event.preventDefault(); //STOP default action


        var userId = Number($(this).find("input[name=originalInfectedUserId]").val());
        var mockData = getMockedData(userId);
        if (mockData) {
            plotData(mockData.userLocationSnapshots);
            $("input", this).prop("disabled", "true");
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

    // special users: 55,
    function getMockedData(userId) {
        switch (userId) {
            case 55:
                return {
                    "userLocationSnapshots": [
                        {
                            "infectingUser": null,
                            "user": {
                                "name": "",
                                "id": 11
                            },
                            "temporalLocation": {
                                "lat": 39.9804366,
                                "lng": 116.3607466,
                                "timestamp": "2008-11-15T10:14:27.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 4
                            },
                            "temporalLocation": {
                                "lat": 39.991543,
                                "lng": 116.331013,
                                "timestamp": "2008-11-15T10:15:34.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 8
                            },
                            "temporalLocation": {
                                "lat": 39.957871,
                                "lng": 116.355759,
                                "timestamp": "2008-11-15T10:15:30.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 9
                            },
                            "temporalLocation": {
                                "lat": 39.999584,
                                "lng": 116.338309,
                                "timestamp": "2008-11-15T10:15:32.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 15
                            },
                            "temporalLocation": {
                                "lat": 40.037392,
                                "lng": 116.328498,
                                "timestamp": "2008-11-15T10:15:31.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 128
                            },
                            "temporalLocation": {
                                "lat": 40.0755349,
                                "lng": 116.3193516,
                                "timestamp": "2008-11-15T10:15:33.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2043
                            },
                            "temporalLocation": {
                                "lat": 39.8995183,
                                "lng": 116.2587133,
                                "timestamp": "2008-11-15T10:15:34.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2045
                            },
                            "temporalLocation": {
                                "lat": 39.88736,
                                "lng": 116.679369,
                                "timestamp": "2008-11-15T10:15:33.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2142
                            },
                            "temporalLocation": {
                                "lat": 39.9858533,
                                "lng": 116.350685,
                                "timestamp": "2008-11-15T10:15:34.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2146
                            },
                            "temporalLocation": {
                                "lat": 40.027454,
                                "lng": 116.430965,
                                "timestamp": "2008-11-15T10:15:31.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1038
                            },
                            "temporalLocation": {
                                "lat": 40.0861483,
                                "lng": 116.2672616,
                                "timestamp": "2008-11-15T10:15:32.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1054
                            },
                            "temporalLocation": {
                                "lat": 39.8810716,
                                "lng": 118.7224499,
                                "timestamp": "2008-11-15T10:15:33.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1973
                            },
                            "temporalLocation": {
                                "lat": 39.990064,
                                "lng": 116.450937,
                                "timestamp": "2008-11-15T10:15:34.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": null,
                                "user": {
                                    "name": "",
                                    "id": 11
                                },
                                "temporalLocation": {
                                    "lat": 39.9804366,
                                    "lng": 116.3607466,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1983
                            },
                            "temporalLocation": {
                                "lat": 39.990064,
                                "lng": 116.450937,
                                "timestamp": "2008-11-15T10:15:34.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": {
                                    "infectingUser": null,
                                    "user": {
                                        "name": "",
                                        "id": 11
                                    },
                                    "temporalLocation": {
                                        "lat": 39.9804366,
                                        "lng": 116.3607466,
                                        "timestamp": "2008-11-15T10:14:27.000-08:00"
                                    }
                                },
                                "user": {
                                    "name": "",
                                    "id": 8
                                },
                                "temporalLocation": {
                                    "lat": 39.957871,
                                    "lng": 116.355759,
                                    "timestamp": "2008-11-15T10:15:30.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1427
                            },
                            "temporalLocation": {
                                "lat": 39.927578,
                                "lng": 116.472269,
                                "timestamp": "2008-11-15T10:16:33.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": {
                                    "infectingUser": null,
                                    "user": {
                                        "name": "",
                                        "id": 11
                                    },
                                    "temporalLocation": {
                                        "lat": 39.9804366,
                                        "lng": 116.3607466,
                                        "timestamp": "2008-11-15T10:14:27.000-08:00"
                                    }
                                },
                                "user": {
                                    "name": "",
                                    "id": 4
                                },
                                "temporalLocation": {
                                    "lat": 39.991543,
                                    "lng": 116.331013,
                                    "timestamp": "2008-11-15T10:15:34.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 19
                            },
                            "temporalLocation": {
                                "lat": 39.8658466,
                                "lng": 116.3835383,
                                "timestamp": "2008-11-15T10:17:06.000-08:00"
                            }
                        },
                        {
                            "infectingUser": {
                                "infectingUser": {
                                    "infectingUser": null,
                                    "user": {
                                        "name": "",
                                        "id": 11
                                    },
                                    "temporalLocation": {
                                        "lat": 39.9804366,
                                        "lng": 116.3607466,
                                        "timestamp": "2008-11-15T10:14:27.000-08:00"
                                    }
                                },
                                "user": {
                                    "name": "",
                                    "id": 4
                                },
                                "temporalLocation": {
                                    "lat": 39.991543,
                                    "lng": 116.331013,
                                    "timestamp": "2008-11-15T10:15:34.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 978
                            },
                            "temporalLocation": {
                                "lat": 39.970579,
                                "lng": 116.418898,
                                "timestamp": "2008-11-15T10:17:05.000-08:00"
                            }
                        }
                    ]
                };

        }
    }



}
google.maps.event.addDomListener(window, 'load', initialize);
