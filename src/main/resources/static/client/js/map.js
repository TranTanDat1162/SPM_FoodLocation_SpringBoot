const options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
};
let map, infoWindow;
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
