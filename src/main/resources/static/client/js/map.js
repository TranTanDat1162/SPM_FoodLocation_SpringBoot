const options = {
    enableHighAccuracy: true,
    timeout: 5000,
    maximumAge: 0,
};
let map, infoWindow;
//For Client------------------------------------------------
async function getCurrentLocation(){
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            (position) => resolve(`${position.coords.latitude},${position.coords.longitude}`),
            (error) => reject(error)
        );
    });
}
async function generateMapEmbed(center, keyword, radius) {
    //center = await getCurrentLocation() ?? '10.797436384668082,106.7035743219549';
    // Replace with your actual API key
    const apiKey = "AIzaSyA5hp-jSwTRsJQOsmed-sZHF7kOX1jl_yw";
    let searchTerm = keyword ?? `quán+ăn+và+quán+nước`;
    let zoomLevel = radius ?? 15;

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
        const cords = await getCurrentLocation();
        generateMapEmbed(cords);
    } catch (error) {
        console.error('Error getting current location:', error);
        // Handle location errors gracefully (e.g., display an error message)
    }

    const locationButton = document.getElementById("pan_to_current");
    const searchButton = document.getElementById("query_map");
    const searchedKeyword = document.getElementById("query_keyword");
    const searchedRegion = document.getElementById("district");
    const searchedRadius = document.getElementById("radius");



    locationButton.addEventListener("click", async () => {
        try {
            const cords = await getCurrentLocation();
            generateMapEmbed(cords);
        } catch (error) {
            console.error('Error getting current location:', error);
            // Handle location errors gracefully (e.g., display an error message)
        }
    });

    searchButton.addEventListener("click", async () =>{
        try {
            generateMapEmbed(null, splitToPlus(searchedKeyword.value)+" near+"+searchedRegion.value, searchedRadius.value);
        } catch (error) {
            console.error('Error getting current location:', error);
            // Handle location errors gracefully (e.g., display an error message)
        }
    })

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

function splitToPlus(text) {
    // Check if text is a string and avoid errors with non-strings
    if (typeof text !== 'string') {
        return text;
    }

    // Use replace method with a regular expression
    return text.replace(/\s/g, '+');
}