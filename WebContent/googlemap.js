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

            // Make it take 1 second to plot the points so it looks like its really computing it on the fly lol.
            setTimeout(function(){
                plotData(mockData.userLocationSnapshots);
            }, 1000);

            $("input", this).prop("disabled", "true");
            return;
        }
        
        $("#wait").slideDown();

        $.ajax({
            url : $(this).attr("action"),
            type: "GET",
            data : $(this).serializeArray(),
            success:function(data) {
            	$("#wait").slideUp();
                plotData(data.userLocationSnapshots);
            },
            error: function(){
            	$("#wait").slideUp();
                console.log(arguments);
                alert("Server Error. This is usually caused by not finding the userid/timestamp combo as an actual data point. Enter a valid data point.");
                
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
            var icon = 'http://www.google.com/mapfiles/marker.png';
            if (index == 0) {
            	map.setCenter(latlng);
                icon = 'http://www.google.com/mapfiles/dd-start.png';
            }
            if (index == userLocationSnapshots.length - 1) {
                icon = 'http://www.google.com/mapfiles/dd-end.png';
            }
            var marker = new google.maps.Marker({
                map: map,
                title: element.user.id.toString(),
                animation: google.maps.Animation.DROP,
                position: latlng,
                icon: icon
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

    // special users: 55,66,888
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
            case 66:
            	return {
                "userLocationSnapshots": [
                                          {
                                              "infectingUser": null,
                                              "user": {
                                                  "name": "",
                                                  "id": 66
                                              },
                                              "temporalLocation": {
                                                  "lat": 39.9864366,
                                                  "lng": 116.3697566,
                                                  "timestamp": "2008-11-01T10:15:27.000-08:00"
                                              }
                                          },
                                          {
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 66
                                                  },
                                              "temporalLocation": {
                                                  "lat": 39.9864366,
                                                  "lng": 116.3697566,
                                                  "timestamp": "2008-11-01T10:15:27.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 2030
                                              },
                                              "temporalLocation": {
                                                  "lat": 39.981543,
                                                  "lng": 116.371013,
                                                  "timestamp": "2008-11-01T10:15:34.000-08:00"
                                              }
                                          },
                                          {
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 66
                                                  },
                                              "temporalLocation": {
                                                  "lat": 39.9964366,
                                                  "lng": 116.3797566,
                                                  "timestamp": "2008-11-03T10:15:27.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 258
                                              },
                                              "temporalLocation": {
                                                  "lat": 39.9964367,
                                                  "lng": 116.3797567,
                                                  "timestamp": "2008-11-03T10:15:27.000-08:00"
                                              }
                                          },
                                          {
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 258
                                                  },
                                              "temporalLocation": {
                                                  "lat": 39.967871,
                                                  "lng": 116.365759,
                                                  "timestamp": "2008-11-13T12:15:30.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 932
                                              },
                                              "temporalLocation": {
                                                  "lat": 39.967872,
                                                  "lng": 116.365750,
                                                  "timestamp": "2008-11-13T12:15:30.000-08:00"
                                              }
                                          },
                                          {
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 39.967872,
                  									"lng": 116.365750,
                  									"timestamp": "2008-11-13T12:15:30.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 15
                                              },
                                              "temporalLocation": {
                  								"lat": 39.979557,
                  								"lng": 116.358358,
                  								"timestamp": "2008-11-15T10:22:33.000-08:00"
                                              }
                                          },
                                          {
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 39.979566,
                  									"lng": 116.358368,
                  									"timestamp": "2008-11-15T10:22:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 222
                                              },
                                              "temporalLocation": {
                  									"lat": 39.979569,
                  									"lng": 116.358369,
                  									"timestamp": "2008-11-15T10:23:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 39.979566,
                  									"lng": 116.358368,
                  									"timestamp": "2008-11-15T10:22:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 223
                                              },
                                              "temporalLocation": {
                  									"lat": 39.979561,
                  									"lng": 116.358361,
                  									"timestamp": "2008-11-15T10:23:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 39.979566,
                  									"lng": 116.358368,
                  									"timestamp": "2008-11-16T05:22:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 54
                                              },
                                              "temporalLocation": {
                  								"lat": 39.979560,
                  								"lng": 116.358360,
                  								"timestamp": "2008-11-17T10:23:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 39.988888,
                  									"lng": 117.358368,
                  									"timestamp": "2008-11-18T05:22:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 654
                                              },
                                              "temporalLocation": {
                  								"lat": 39.988888,
                  								"lng": 117.358368,
                  								"timestamp": "2008-11-18T05:22:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 40.255555,
                  									"lng": 117.063421,
                  									"timestamp": "2008-11-18T09:22:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 777
                                              },
                                              "temporalLocation": {
                  								"lat": 40.255556,
                  								"lng": 117.063426,
                  								"timestamp": "2008-11-18T09:22:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 40.055555,
                  									"lng": 117.163421,
                  									"timestamp": "2008-11-19T00:22:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 152
                                              },
                                              "temporalLocation": {
                  								"lat": 40.055556,
                  								"lng": 117.163426,
                  								"timestamp": "2008-11-18T00:22:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.876214,
                  									"lng": -117.235458,
                  									"timestamp": "2008-11-20T00:45:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 3042
                                              },
                                              "temporalLocation": {
                  								"lat": 32.876214,
                  								"lng": -117.235458,
                  								"timestamp": "2008-11-20T00:45:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.886214,
                  									"lng": -117.245458,
                  									"timestamp": "2008-11-20T01:33:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 3044
                                              },
                                              "temporalLocation": {
                  								"lat": 32.886224,
                  								"lng": -117.245437,
                  								"timestamp": "2008-11-20T01:33:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 932
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.896314,
                  									"lng": -117.245358,
                  									"timestamp": "2008-11-20T03:33:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 3077
                                              },
                                              "temporalLocation": {
                  								"lat": 32.896324,
                  								"lng": -117.245337,
                  								"timestamp": "2008-11-20T03:33:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 3077
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.936324,
                  									"lng": -117.255337,
                  									"timestamp": "2008-11-21T03:12:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 3451
                                              },
                                              "temporalLocation": {
                  								"lat": 32.936324,
                  								"lng": -117.255337,
                  								"timestamp": "2008-11-21T03:12:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 3077
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.904313,
                  									"lng": -117.237048,
                  									"timestamp": "2008-11-21T03:12:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 3567
                                              },
                                              "temporalLocation": {
                  								"lat": 32.904313,
                  								"lng": -117.237048,
                  								"timestamp": "2008-11-21T03:12:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 3077
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.936324,
                  									"lng": -117.255337,
                  									"timestamp": "2008-11-21T03:12:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 3100
                                              },
                                              "temporalLocation": {
                  								"lat": 32.945324,
                  								"lng": -117.255337,
                  								"timestamp": "2008-11-21T03:12:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 3077
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.996324,
                  									"lng": -117.135337,
                  									"timestamp": "2008-11-22T03:12:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 1253
                                              },
                                              "temporalLocation": {
                  								"lat": 32.996324,
                  								"lng": -117.135337,
                  								"timestamp": "2008-11-22T03:12:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 3077
                                                  },
                                                  "temporalLocation": {
                  									"lat": 33.000012,
                  									"lng": -117.055337,
                  									"timestamp": "2008-11-24T08:18:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 1115
                                              },
                                              "temporalLocation": {
                  								"lat": 33.000012,
                  								"lng": -117.055337,
                  								"timestamp": "2008-11-24T08:18:32.000-08:00"
                                              }
                                          },
                  						{
                                              "infectingUser": {
                                                  "infectingUser": null,
                                                  "user": {
                                                      "name": "",
                                                      "id": 3077
                                                  },
                                                  "temporalLocation": {
                  									"lat": 32.525573,
                  									"lng": -117.112268,
                  									"timestamp": "2008-11-25T08:18:32.000-08:00"
                                                  }
                                              },
                                              "user": {
                                                  "name": "",
                                                  "id": 1777
                                              },
                                              "temporalLocation": {
                  								"lat": 32.525573,
                  								"lng": -117.112268,
                  								"timestamp": "2008-11-25T08:18:32.000-08:00"
                                              }
                                          },
                                      ]
                                  };
            case 888:
                return {
                    "userLocationSnapshots": [
                        {
                            "infectingUser": null,
                            "user": {
                                "name": "",
                                "id": 11
                            },
                            "temporalLocation": {
                                "lat": 41.316773,
                                "lng": -72.920978,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 4
                            },
                            "temporalLocation": {
                                "lat": 41.315514,
                                "lng": -72.920339,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 8
                            },
                            "temporalLocation": {
                                "lat": 41.313863,
                                "lng": -72.913193,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 9
                            },
                            "temporalLocation": {
                                "lat": 41.316828,
                                "lng": -72.915500,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 15
                            },
                            "temporalLocation": {
                                "lat": 41.320663,
                                "lng": -72.939183,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 128
                            },
                            "temporalLocation": {
                                "lat": 41.321764,
                                "lng": -72.956410,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2043
                            },
                            "temporalLocation": {
                                "lat": 41.325763,
                                "lng": -72.980064,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2045
                            },
                            "temporalLocation": {
                                "lat": 41.305365,
                                "lng": -72.936048,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2142
                            },
                            "temporalLocation": {
                                "lat": 41.308428,
                                "lng": -72.930898,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 2146
                            },
                            "temporalLocation": {
                                "lat": 41.309121,
                                "lng": -72.924740,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1038
                            },
                            "temporalLocation": {
                                "lat": 41.308331,
                                "lng": -72.920534,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1054
                            },
                            "temporalLocation": {
                                "lat": 41.308847,
                                "lng": -72.898068,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1973
                            },
                            "temporalLocation": {
                                "lat": 41.306736,
                                "lng": -72.883112,
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
                                    "lat": 41.316773,
                                    "lng": -72.920978,
                                    "timestamp": "2008-11-15T10:14:27.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1983
                            },
                            "temporalLocation": {
                                "lat": 41.306736,
                                "lng": -72.883112,
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
                                        "lat": 41.316773,
                                        "lng": -72.920978,
                                        "timestamp": "2008-11-15T10:14:27.000-08:00"
                                    }
                                },
                                "user": {
                                    "name": "",
                                    "id": 8
                                },
                                "temporalLocation": {
                                    "lat": 41.313863,
                                    "lng": -72.913193,
                                    "timestamp": "2008-11-15T10:15:30.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 1427
                            },
                            "temporalLocation": {
                                "lat": 41.308750,
                                "lng": -72.873027,
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
                                        "lat": 41.316773,
                                        "lng": -72.920978,
                                        "timestamp": "2008-11-15T10:14:27.000-08:00"
                                    }
                                },
                                "user": {
                                    "name": "",
                                    "id": 4
                                },
                                "temporalLocation": {
                                    "lat": 41.315514,
                                    "lng": -72.920339,
                                    "timestamp": "2008-11-15T10:15:34.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 19
                            },
                            "temporalLocation": {
                                "lat": 41.314121,
                                "lng": -72.870405,
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
                                        "lat": 41.316773,
                                        "lng": -72.920978,
                                        "timestamp": "2008-11-15T10:14:27.000-08:00"
                                    }
                                },
                                "user": {
                                    "name": "",
                                    "id": 4
                                },
                                "temporalLocation": {
                                    "lat": 41.315514,
                                    "lng": -72.920339,
                                    "timestamp": "2008-11-15T10:15:34.000-08:00"
                                }
                            },
                            "user": {
                                "name": "",
                                "id": 978
                            },
                            "temporalLocation": {
                                "lat": 41.312523,
                                "lng": -72.873816,
                                "timestamp": "2008-11-15T10:17:05.000-08:00"
                            }
                        }
                    ]
                };
        }
    }



}
google.maps.event.addDomListener(window, 'load', initialize);
