async function initMap() {
    const UEFSchoolCord = { lat: 10.797436384668082, lng: 106.7035743219549 };

    map = await new google.maps.Map(document.getElementById("map"), {
        zoom: 16,
        center: UEFSchoolCord,
        zoomControl: false,
        scaleControl: true,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
        }
    });

    infoWindow = new google.maps.InfoWindow();
    //-----------------------------------------
    // const allowedPlaceIds = ["restaurant", "cafe", "bar"];
    //
    // function filterMarkersByPlaceId(place) {
    //     return allowedPlaceIds.includes(place.place_id);
    // }
    //
    // const service = new google.maps.places.PlacesService(map);
    // // Search for businesses around a specific location
    // service.nearbySearch({
    //     location: UEFSchoolCord,
    //     radius: 5000, // Adjust search radius as needed
    //     type: 'business'
    // }, (results, status) => {
    //     if (status === google.maps.places.PlacesServiceStatus.OK) {
    //         results.filter(filterMarkersByPlaceId).forEach(place => {
    //             const marker = new google.maps.Marker({
    //                 position: place.geometry.location,
    //                 map,
    //                 title: place.name
    //             });
    //         });
    //     }
    // });
    //-----------------------------------------
    const locationButton = document.createElement("button");

    locationButton.textContent = "Pan to Current Location";
    locationButton.classList.add("custom-map-control-button");
    map.controls[google.maps.ControlPosition.TOP_CENTER].push(locationButton);
    locationButton.addEventListener("click", () => {
        // Try HTML5 geolocation.
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const pos = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                    };

                    infoWindow.setPosition(pos);
                    infoWindow.setContent("Location found.");
                    infoWindow.open(map);
                    map.setCenter(pos);
                },
                () => {
                    handleLocationError(true, infoWindow, map.getCenter());
                },
                options
            );
        } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
        }
    });
    //------------------------------------------
    var searchBox = new google.maps.places.SearchBox(document.getElementById('search-input'));
    //map.controls[google.maps.ControlPosition.TOP_CENTER].push(document.getElementById('search-input'));

    // Bias the SearchBox results towards current map's viewport.
    map.addListener("bounds_changed", () => {
        searchBox.setBounds(map.getBounds());
    });

    let markers = [];

    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener("places_changed", () => {
        const places = searchBox.getPlaces();

        if (places.length == 0) {
            return;
        }

        // Clear out the old markers.
        markers.forEach((marker) => {
            marker.setMap(null);
        });
        markers = [];

        // For each place, get the icon, name and location.
        const bounds = new google.maps.LatLngBounds();

        places.forEach((place) => {
            if (!place.geometry || !place.geometry.location) {
                console.log("Returned place contains no geometry");
                return;
            }

            const icon = {
                url: place.icon,
                size: new google.maps.Size(71, 71),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(17, 34),
                scaledSize: new google.maps.Size(25, 25),
            };

            // Create a marker for each place.
            markers.push(
                new google.maps.Marker({
                    map,
                    icon,
                    title: place.name,
                    position: place.geometry.location,
                }),
            );
            if (place.geometry.viewport) {
                // Only geocodes have viewport.
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });
        map.fitBounds(bounds);
    });
}
