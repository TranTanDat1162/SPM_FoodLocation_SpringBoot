const options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
};
let map, infoWindow;
async function initMap() {
    const UEFSchoolCord = { lat: 10.797436384668082, lng: 106.7035743219549 };

    map = await new google.maps.Map(document.getElementById("map"), {
        zoom: 16,
        center: UEFSchoolCord,
        zoomControl: false,
        scaleControl: true
    });

    infoWindow = new google.maps.InfoWindow();
    //-----------------------------------------
    const allowedPlaceIds = ["restaurant", "cafe", "bar"];

    function filterMarkersByPlaceId(place) {
        return allowedPlaceIds.includes(place.place_id);
    }

    const service = new google.maps.places.PlacesService(map);
    // Search for businesses around a specific location
    service.nearbySearch({
        location: UEFSchoolCord,
        radius: 5000, // Adjust search radius as needed
        type: 'business'
    }, (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK) {
            results.filter(filterMarkersByPlaceId).forEach(place => {
                const marker = new google.maps.Marker({
                    position: place.geometry.location,
                    map,
                    title: place.name
                });
            });
        }
    });
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

}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(
        browserHasGeolocation
            ? "Error: The Geolocation service failed."
            : "Error: Your browser doesn't support geolocation.",
    );
    infoWindow.open(map);
}
