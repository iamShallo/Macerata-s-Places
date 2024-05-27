const map = L.map('map').setView([43.300084, 13.453467], 15);

const tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

const marker = L.marker([43.300169, 13.453647]).addTo(map)
    .bindPopup('<b>Teatro Lauro Rossi</b>').openPopup();

const popup = L.popup()
    .setLatLng([43.300267, 13.453566])

const userId= 4;
function onMapClick(e) {
    popup
        .setLatLng(e.latlng)
}

map.on('click', onMapClick);

function centerMapToLocation(latitude, longitude) {
    map.setView([latitude, longitude], 13);
}

var centerMapButton = document.getElementById('centerMapButton');

const user=3;

centerMapButton.addEventListener('click', function () {
    centerMapToLocation(43.300267, 13.453566);
    const popup = L.popup()
        .setLatLng([43.300267, 13.453566])
        .setContent('Centro di Macerata')
        .openOn(map);
});
function createPointMonument(name, x, y, type, inaugurationDate , story) {
    const monument= {name: name, x:x, y:y, type:type, inaugurationDate: inaugurationDate, story:story};
    fetch(`http://localhost:8080/points/addMonument?userId=${user}`,
    {
    method: 'POST',
        headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(monument),
})
.then(response => response.text())
    .then(data => console.log(data))
    .catch(error => console.error('Error:Monument non creato', error));
}
function createPointRestaurant(name, x, y, type, typeRestaurant , openingHours) {
    const restaurant= {name: name, x:x, y:y, type:type, typeRestaurant: typeRestaurant, openingHours:openingHours};
    fetch(`http://localhost:8080/points/addRestaurant?userId=${user}`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(restaurant),
        })
        .then(response => response.text())
        .then(data => console.log(data))
        .catch(error => console.error('Error:Restaurant non creato', error));
}
function createPointGreenZone(name, x, y, type, characteristics) {
    const greenZone= {name: name, x:x, y:y, type:type, characteristics: characteristics};
    fetch(`http://localhost:8080/points/addGreenZone?userId=${user}`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(greenZone),
        })
        .then(response => response.text())
        .then(data => console.log(data))
        .catch(error => console.error('Error:Zona verde non creata', error));
}
function createPointSquare(name, x, y, type, history) {
    const square= {name: name, x:x, y:y, type:type, history: history};
    fetch(`http://localhost:8080/points/addGreenZone?userId=${user}`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(square),
        })
        .then(response => response.text())
        .then(data => console.log(data))
        .catch(error => console.error('Error:Piazza non creata', error));
}

map.on('click', function (e) {
    Swal.fire({
        title: 'Seleziona il tipo',
        input: 'select',
        inputOptions: {
            'Monument': 'Monumento',
            'Restaurant': 'Ristorante',
            'Greenzone': 'Parco',
            'Square': 'Piazza'
        },
        inputPlaceholder: 'Seleziona un tipo',
        showCancelButton: true,
        confirmButtonText: 'Avanti',
        cancelButtonText: 'Annulla',
        inputValidator: (value) => {
            return new Promise((resolve) => {
                if (value !== '') {
                    resolve();
                } else {
                    resolve('Devi selezionare un tipo');
                }
            });
        }
    }).then((result) => {
        if (result.isConfirmed) {
            var type = result.value;
            if (type === 'Restaurant') {
                Swal.fire({
                    title: 'Seleziona il tipo di ristorante',
                    input: 'select',
                    inputOptions: {
                        'FastFood': 'FastFood',
                        'Italiano': 'Italiano',
                        'Pizzeria': 'Pizzeria',
                        'Sushi': 'Sushi'
                    },
                    inputPlaceholder: 'Seleziona un tipo di ristorante',
                    showCancelButton: true,
                    confirmButtonText: 'Aggiungi',
                    cancelButtonText: 'Annulla',
                    inputValidator: (value) => {
                        return new Promise((resolve) => {
                            if (value !== '') {
                                resolve();
                            } else {
                                resolve('Devi selezionare un tipo di ristorante');
                            }
                        });
                    }
                }).then((subResult) => {
                    var subType = subResult.value;
                    var latitude = e.latlng.lat;
                    var longitude = e.latlng.lng;
                    var name = prompt('Inserisci il nome per il nuovo punto:');
                    var opening = prompt('Inserisci un orario di apertura (formato HH:MM):');
                   // if (subResult.isConfirmed) {
                        if (name && opening) {
                            createPointRestaurant(name, latitude, longitude, type, subType, opening);
                        }

                    ricaricaPaginaDopoTempo(2000);
                });
            } else if (type === "Monument") {
                var latitude = e.latlng.lat;
                var longitude = e.latlng.lng;
                var name = prompt('Inserisci il nome per il nuovo punto:');
                var history = prompt("Inserisci la storia per questo monumento:")
                var date;
                do {
                    date = prompt('Inserisci una data nel formato YYYY-MM-DD:');
                } while (!isValidDate(date));

                function isValidDate(dateString) {
                    var regex = /^\d{4}-\d{2}-\d{2}$/;
                    return regex.test(dateString);
                }

                if (name && date) {
                    createPointMonument(name, latitude, longitude, type, date, history);
                }

                ricaricaPaginaDopoTempo(2000);
            } else if (type === "Greenzone") {
                var latitude = e.latlng.lat;
                var longitude = e.latlng.lng;
                var name = prompt('Inserisci il nome per il nuovo punto:');
                var characteristics = prompt("Inserisci la storia per questo parco:")
                if (name && characteristics) {
                    createPointGreenZone(name, latitude, longitude, type, characteristics);
                }

                ricaricaPaginaDopoTempo(2000);
            } else {
                var latitude = e.latlng.lat;
                var longitude = e.latlng.lng;
                var name = prompt('Inserisci il nome per il nuovo punto:');
                var history = prompt("Inserisci la storia per questa piazza:")
                if (name && history) {
                    createPointSquare(name, latitude, longitude, type, history)
                }

                ricaricaPaginaDopoTempo(2000);
            }
        }
    });
});

function ricaricaPaginaDopoTempo(tempo) {
    setTimeout(function() {
        location.reload();
    }, tempo);
}

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

document.addEventListener('DOMContentLoaded', onPageLoad);