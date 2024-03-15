const options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
};
let map, infoWindow;
//For Admin------------------------------------------------
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

//For Client------------------------------------------------
async function getCurrentLocation(){
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };

                const center = `${pos.lat},${pos.lng}`;
                return center;
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
}
async function generateMapEmbed(center) {
    center = await getCurrentLocation() ?? '10.797436384668082,106.7035743219549';
    // Replace with your actual API key
    const apiKey = "AIzaSyA5hp-jSwTRsJQOsmed-sZHF7kOX1jl_yw";
    const searchTerm = `tiệm+nước+và+quán+ăn+trong+phường`;
    const zoomLevel = 15;

    // // Encode special characters in the search term
    // const encodedSearchTerm = encodeURIComponent(searchTerm);

    // Construct the iframe source URL
    const iframeSrc = `https://www.google.com/maps/embed/v1/search?key=${apiKey}&q=${searchTerm}&center=${center}&zoom=${zoomLevel}`;

    // Create the iframe element
    const iframe = document.createElement("iframe");
    iframe.width = "100%";
    iframe.height = "100%";
    iframe.frameborder = "0";
    iframe.style.border = "0";
    iframe.referrerpolicy = "no-referrer-when-downgrade";
    iframe.allowfullscreen = true;
    iframe.src = iframeSrc;
    iframe.id = "embedded_map"
    const mapElement = document.getElementById("map"); // Replace with your container element ID

    const existed_map = document.getElementById("embedded_map")

    if(existed_map != null)
        existed_map.src = iframeSrc;
    else
        mapElement.appendChild(iframe);
}
function innit() {

    generateMapEmbed(null);

    const locationButton = document.createElement("button");

    locationButton.textContent = "Pan to Current Location";

    locationButton.classList.add("btn");
    locationButton.classList.add("btn-primary");
    locationButton.classList.add("border-2");
    locationButton.classList.add("border-primary");
    locationButton.classList.add("py-3");
    locationButton.classList.add("px-4");
    locationButton.classList.add("position-absolute");
    locationButton.classList.add("rounded-pill");
    locationButton.classList.add("text-white");
    locationButton.classList.add("h-100");

    const location_btn = document.getElementById("current_loc");

    location_btn.appendChild(locationButton);

    locationButton.addEventListener("click", () => {

        generateMapEmbed(getCurrentLocation());

    });

}
// You can call generateMapEmbed with different center coordinates to create multiple iframes

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(
        browserHasGeolocation
            ? "Error: The Geolocation service failed."
            : "Error: Your browser doesn't support geolocation.",
    );
    infoWindow.open(map);
}

window.innit()
