const options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
};
let map, infoWindow, currentCord;
//let placeId, name, rating, userRatingsTotal, formattedAddress, type, openingHours, websiteUrl;
async function getCurrentLocation(){
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                currentCord = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };

                return currentCord;
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

async function initMap() {
    const UEFSchoolCord = { lat: 10.797436384668082, lng: 106.7035743219549 };
    currentCord = await getCurrentLocation();

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
    new ClickEventHandler(map, origin);

    infoWindow = new google.maps.InfoWindow();
    //----------------------------------------

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

            restarantInfoParse(place);

        });
        map.fitBounds(bounds);
    });
    //-----------------------------------------
    // var request = {
    //     fields: ['place_id','user_ratings_total','rating','types','formatted_address','name','opening_hours',],
    //     query: 'tiệm nước và quán ăn trong phường',
    //     locationBias: map.getBounds()
    //
    // };
    //
    // service = new google.maps.places.PlacesService(map);
    // service.findPlaceFromQuery(request, async (results, status) => {
    //     if (status === google.maps.places.PlacesServiceStatus.OK && results) {
    //         for (let i = 0; i < results.length; i++) {
    //             await createAdvancedMarker(results[i]);
    //         }
    //         const tableBody = document.querySelector("tbody"); // Assuming your table body has this tag
    //
    //         for (let i = 0; i < results.length; i++) {
    //             const restaurant = results[i];
    //
    //             // Create table row element
    //             const tableRow = document.createElement("tr");
    //
    //             // Restaurant Name
    //             const nameCell = document.createElement("td");
    //             nameCell.textContent = restaurant.name;
    //
    //             // Other Restaurant Details (modify based on your table structure)
    //             const ratingCell = document.createElement("td");
    //             // Assuming 'user_ratings_total' and 'rating' are available from the Places API response
    //             ratingCell.innerHTML = `<i><i class="fas fa-star"></i>Avg: ${restaurant.rating} </i> from ${restaurant.user_ratings_total}`; // Adjust HTML for ratings
    //
    //             const descriptionCell = document.createElement("td");
    //             descriptionCell.textContent = restaurant.url;
    //
    //             const addressCell = document.createElement("td");
    //             addressCell.textContent = restaurant.formatted_address;
    //
    //             const openingTimeCell = document.createElement("td");
    //             // Assuming 'opening_hours' has relevant data
    //             openingTimeCell.textContent = openingTimeCell.textContent = extractOpeningHours(restaurant.opening_hours); // Implement a function to extract opening hours (see explanation below)
    //
    //             const closingTimeCell = document.createElement("td");
    //             // Assuming 'opening_hours' has relevant data
    //             closingTimeCell.textContent = extractClosingHours(restaurant.opening_hours); // Implement a function to extract closing hours (see explanation below)
    //
    //             // Edit Button (assuming your existing logic remains the same)
    //             const editCell = document.createElement("td");
    //             // ... Your existing code for edit button ...
    //
    //             // Add cells to the table row
    //             //tableRow.appendChild(imageCell);
    //             tableRow.appendChild(nameCell);
    //             tableRow.appendChild(ratingCell);
    //             tableRow.appendChild(descriptionCell);
    //             tableRow.appendChild(addressCell);
    //             tableRow.appendChild(openingTimeCell);
    //             tableRow.appendChild(closingTimeCell);
    //             tableRow.appendChild(editCell);
    //
    //             // Add the table row to the table body
    //             tableBody.appendChild(tableRow);
    //         }
    //         // Consider centering the map on the search area (optional)
    //     } else {
    //         // Handle unsuccessful search results (optional)
    //     }
    // });

    // service.AutocompleteService();
    // service.getQueryPredictions({ input: "pizza near Syd" }, displaySuggestions);

}

async function createAdvancedMarker(place) {
    if (!place.geometry || !place.geometry.location) return;

    // Assuming you have imported the libraries for AdvancedMarker
    const { AdvancedMarkerElement, MarkerView } = await google.maps.importLibrary("marker");

    const content = document.createElement("div");
    content.textContent = place.name || "";

    const markerView = new MarkerView({
        content: content,
    });

    const marker = new AdvancedMarkerElement({
        position: place.geometry.location,
        content: markerView,
    });

    marker.setMap(map);

    google.maps.event.addListener(marker, "click", () => {
        infowindow.setContent(""); // Clear previous content (optional)
        infowindow.setContent(place.name || "");
        infowindow.open(map);
    });
}
function extractOpeningHours(openingHoursData) {
    // Check if opening hours data is available
    if (!openingHoursData || !openingHoursData.periods) {
        return "N/A"; // Or any placeholder for missing data
    }

    // The Places API response format for opening hours can vary
    // Depending on the format, you might need to adjust the parsing logic

    const openingHoursArray = openingHoursData.periods.map(period => {
        try {
            const openingTime = period.open?.time;

            if (!openingTime) {
                return "N/A";
            }

            if (openingTime.length !== 4) {
                return "Invalid closing time format";
            }
            const hours = parseInt(openingTime.slice(0, 2));
            const minutes = parseInt(openingTime.slice(2));

            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                return "Invalid closing time format";
            }
            return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
        } catch (error) {
            console.error("Error parsing opening hours:", error);
            // Return a more informative error message
            return "Invalid opening hours format";
        }
    });

    // If there are multiple opening periods, return the first one
    return openingHoursArray[0] || "N/A";
}

function extractClosingHours(openingHoursData) {
    // Similar logic to extractOpeningHours
    if (!openingHoursData || !openingHoursData.periods) {
        return "N/A";
    }

    const closingHoursArray = openingHoursData.periods.map(period => {
        try {
            const closingTime = period.close?.time;

            if (!closingTime) {
                return "N/A";
            }

            if (closingTime.length !== 4) {
                return "Invalid closing time format";
            }
            const hours = parseInt(closingTime.slice(0, 2));
            const minutes = parseInt(closingTime.slice(2));

            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                return "Invalid closing time format";
            }
            return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;

        } catch (error) {
            console.error("Error parsing closing hours:", error);
            // Return a more informative error message
            return "Invalid closing hours format";
        }
    });
    return closingHoursArray[0] || "N/A";
}

function isIconMouseEvent(e) {
    return "placeId" in e;
}

class ClickEventHandler {
    origin;
    map;
    directionsService;
    directionsRenderer;
    placesService;
    infowindow;
    infowindowContent;

    constructor(map, origin) {
        this.origin = origin;
        this.map = map;
        // this.directionsService = new google.maps.DirectionsService();
        // this.directionsRenderer = new google.maps.DirectionsRenderer();
        // this.directionsRenderer.setMap(map);
        this.placesService = new google.maps.places.PlacesService(map);
        this.infowindow = new google.maps.InfoWindow();
        this.infowindowContent = document.getElementById("infowindow-content");
        this.infowindow.setContent(this.infowindowContent);
        // Listen for clicks on the map.
        this.map.addListener("click", this.handleClick.bind(this));
    }

    handleClick(event) {
        console.log("You clicked on: " + event.latLng);
        // If the event has a placeId, use it.
        if (isIconMouseEvent(event)) {
            console.log("You clicked on place:" + event.placeId);

            document.getElementById("formLatitude").value = event.latLng.lat();
            document.getElementById("formLongitude").value = event.latLng.lng();
            document.getElementById("formPlaceId").value = event.placeId;

            event.stop();
            if (event.placeId) {
                //this.calculateAndDisplayRoute(event.placeId);
                this.getPlaceInformation(event.placeId);
            }
        }
    }

    // calculateAndDisplayRoute(placeId) {
    //     const me = this;
    //
    //     this.directionsService
    //         .route({
    //             origin: this.origin,
    //             destination: {placeId: placeId},
    //             travelMode: google.maps.TravelMode.WALKING,
    //         })
    //         .then((response) => {
    //             me.directionsRenderer.setDirections(response);
    //         })
    //         .catch((e) => window.alert("Directions request failed due to " + status));
    // }

    getPlaceInformation(placeId) {
        const me = this;

        this.placesService.getDetails({placeId: placeId}, (place, status) => {
            if (
                status === "OK" &&
                place &&
                place.geometry &&
                place.geometry.location
            ) {
                // me.infowindow.close();
                // me.infowindow.setPosition(place.geometry.location);
                // me.infowindowContent.children["place-icon"].src = place.icon;
                // me.infowindowContent.children["place-name"].textContent = place.name;
                // me.infowindowContent.children["place-id"].textContent = place.place_id;
                // me.infowindowContent.children["place-address"].textContent =
                //     place.formatted_address;
                // me.infowindow.open(me.map);


                //-----------------------------------------
                restarantInfoParse(place);
                //-----------------------------------------
            }
        });
    }
}

function restarantInfoParse(place){
    // Name
    name = place.name;

    // Rating (if available)
    rating = place.rating;
    userRatingsTotal = place.user_ratings_total;

    // Address
    formattedAddress = place.formatted_address;

    // Lat&Lng
    cordinate = place.latLng;

    // Types
    type = place.types;

    // Opening hours (parsing might be needed)
    openingHours = place.opening_hours;

    // Website (if available)
    websiteUrl = place.url;



    const formName = document.getElementById("formName");
    const formAddress = document.getElementById("formAddress");
    const formRating = document.getElementById("formRating");
    const formDescription = document.getElementById("formDescription");
    const formOpenTime = document.getElementById("formOpenTime");
    const formCloseTime = document.getElementById("formCloseTime");
    const formContactNum = document.getElementById("formContactNum");


    // Fill in the form fields with place details
    formName.value = place.name;

    // Fill in Location
    formAddress.value = place.formatted_address;

    formRating.value = place.rating ? place.rating : "";  // Assuming 'rating' holds the average value

    formDescription.value = place.url ? place.url : "" + " " + place.types ? place.types : "";

    formOpenTime.value = extractOpeningHours(place.opening_hours);
    formCloseTime.value = extractClosingHours(place.opening_hours);

    formContactNum.value = place.formatted_phone_number;

}