async function getCurrentLocation() {
    try {
        return await navigator.geolocation.getCurrentPosition();
    } catch (error) {
        console.error("Error getting location:", error);
        // Handle location retrieval error (e.g., display an error message)
    }
}

async function initMap() {
    const position = await getCurrentLocation();
    const map = await new google.maps.Map(document.getElementById("map"), {
        zoom: 16,
        center: { lat: position?.latitude ?? 10.797436384668082, lng: position?.longitude?? 106.7035743219549 },
        zoomControl: false,
        scaleControl: true,
    });
}