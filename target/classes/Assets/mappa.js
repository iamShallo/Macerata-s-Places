const map = L.map('map').setView([43.300084, 13.453467], 15);

const tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

const marker = L.marker([43.300169, 13.453647]).addTo(map)
    .bindPopup('<b>Teatro Lauro Rossi</b>').openPopup();

const popup = L.popup()
    .setLatLng([43.300267, 13.453566])
    .setContent('Centro di Macerata')
    .openOn(map);

function onMapClick(e) {
    popup
        .setLatLng(e.latlng)
        .setContent(`Hai cliccato la mappa nelle cordinate: ${e.latlng.toString()}`)
        .openOn(map);
}
map.on('click', onMapClick);
function centerMapToLocation(latitude, longitude) {
    map.setView([latitude, longitude], 13);
}

var centerMapButton = document.getElementById('centerMapButton');

centerMapButton.addEventListener('click', function() {
    // Chiama la funzione per spostare la mappa al centro di una posizione selezionata
    centerMapToLocation(43.300267, 13.453566);
    const popup = L.popup()
        .setLatLng([43.300267, 13.453566])
        .setContent('Centro di Macerata')
        .openOn(map);
});



function onPageLoad() {
    fetch('http://localhost:8080/points/getAll')
        .then(response => response.json())
        .then(data => {
            data.forEach(point => {
                var aa = L.marker([point.x, point.y]).addTo(map);
                aa.bindPopup(`<b>${point.name}</b><br>Tipo: ${point.type}`);
            });
        })
        .catch(error => {
            console.error('Si è verificato un errore durante il caricamento dei punti:', error);
        });
}

// Chiama la funzione onPageLoad quando la pagina è completamente caricata
document.addEventListener('DOMContentLoaded', onPageLoad);

