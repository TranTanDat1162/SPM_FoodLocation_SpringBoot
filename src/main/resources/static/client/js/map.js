const options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
};
let map, infoWindow,cords;
//For Client------------------------------------------------
async function getCurrentLocation(){
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            (position) => resolve(`${position.coords.latitude},${position.coords.longitude}`),
            (error) => reject(error)
        );
    });
}
async function generateMapEmbed(center, keyword, region, radius) {
    //center = await getCurrentLocation() ?? '10.797436384668082,106.7035743219549';
    // Replace with your actual API key
    const apiKey = "AIzaSyA5hp-jSwTRsJQOsmed-sZHF7kOX1jl_yw";

    let searchTerm = keyword?.trim() || "quán+ăn+và+quán+nước";

    searchTerm = searchTerm + (region ?? '');

    let zoomLevel = radius || 15;

    // // Encode special characters in the search term
    // const encodedSearchTerm = encodeURIComponent(searchTerm);

    // Construct the iframe source URL

    let iframeSrc = `https://www.google.com/maps/embed/v1/search?key=${apiKey}&q=${searchTerm}&zoom=${zoomLevel}`;

    // Add center parameter only if it exists
    if (center) {
        iframeSrc += `&center=${center}`;
    }

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
async function innit() {

    try {
        cords = await getCurrentLocation();
    } catch (error) {
        console.error('Error getting current location:', error);
    }

    const locationButton = document.getElementById("pan_to_current");
    const searchButton = document.getElementById("query_map");
    const searchedKeyword = document.getElementById("query_keyword");
    const searchedRegion = document.getElementById("district");
    const searchedRadius = document.getElementById("radius");

    const fetchedLat = document.getElementById("currentLat");
    const fetchedLng = document.getElementById("currentLng");

    const temp = cords.split(",")
    fetchedLat.value = temp[0];
    fetchedLng.value = temp[1];

    locationButton.addEventListener("click", async () => {
        try {
            cords = await getCurrentLocation();
            generateMapEmbed(cords);
        } catch (error) {
            console.error('Error getting current location:', error);
            // Handle location errors gracefully (e.g., display an error message)
        }
    });

    searchButton.addEventListener("click", async () =>{
        try {
            generateMapEmbed(null, splitToPlus(searchedKeyword.value), "+near+"+searchedRegion.value, searchedRadius.value);
        } catch (error) {
            console.error('Error getting current location:', error);
            // Handle location errors gracefully (e.g., display an error message)
        }
    })

}
// You can call generateMapEmbed with different center coordinates to create multiple iframes
async function generateMapDirectionEmbed(placeId, des_lat, des_lng) {
    let center = await getCurrentLocation() ?? '10.797436384668082,106.7035743219549';

    // Replace with your actual API key
    const apiKey = "AIzaSyA5hp-jSwTRsJQOsmed-sZHF7kOX1jl_yw";
    const cordinate = center.split(",");

    // Calculate distance between POIs (replace with your coordinates)
    const distance = calculateDistance(cordinate[0], cordinate[1], des_lat, des_lng);

    // Estimate zoom level based on distance and map size
    const zoomLevel = estimateZoomLevel(distance);

    // Construct the iframe source URL
    let iframeSrc = `https://www.google.com/maps/embed/v1/directions?key=${apiKey}&origin=${center}&destination=place_id:${placeId}&zoom=${zoomLevel}`;

    // Create the iframe element
    const iframe = document.createElement("iframe");
    iframe.width = "100%";
    iframe.height = "100%";
    iframe.frameborder = "0";
    iframe.style.border = "0";
    iframe.allowfullscreen = true;
    iframe.referrerpolicy = "no-referrer-when-downgrade";
    iframe.src = iframeSrc;

    const mapElement = document.getElementById(placeId); // Replace with your container element ID
    mapElement.appendChild(iframe);
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
window.innit()

function splitToPlus(text) {
    // Check if text is a string and avoid errors with non-strings
    if (typeof text !== 'string') {
        return text;
    }

    // Use replace method with a regular expression
    return text.replace(/\s/g, '+');
}

function calculateDistance(lat1, lng1, lat2, lng2) {
    const R = 6371; // Earth's radius in kilometers

    const latRad1 = radians(lat1);
    const latRad2 = radians(lat2);
    const dLat = radians(lat2 - lat1);
    const dLng = radians(lng2 - lng1);

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(latRad1) * Math.cos(latRad2) *
        Math.sin(dLng / 2) * Math.sin(dLng / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
}

// Helper function to convert degrees to radians
function radians(degrees) {
    return degrees * Math.PI / 180;
}

function estimateZoomLevel(distanceInKm) {
    switch (true) {
        case distanceInKm < 1:
            return 15; // Very close zoom (e.g., building level)
        case distanceInKm < 4:
            return 13; // Close zoom (e.g., street level)
        case distanceInKm < 8:
            return 12; // Town/city level
        case distanceInKm < 25:
            return 10; // Regional area
        case distanceInKm < 50:
            return 6; // State/province level
        default:
            return 0; // Very far out
    }
}
