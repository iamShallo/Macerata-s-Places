function mostraMessaggio() {
    var inputText = prompt("Inserisci il messaggio:");
    if (inputText) {
        var overlayText = document.querySelector('.overlay-text');
        var message = document.createElement("p");
        var currentDate = new Date();
        var dateString = currentDate.toLocaleDateString('it-IT');
        var timeString = currentDate.toLocaleTimeString('it-IT');
        overlayText.appendChild(message);

        // Aggiunta del testo dopo le immagini
        var customText = "<p class='overlay-message'>Animatore: " + inputText + " " + dateString + " " + timeString + "</p>";
        document.querySelector('.photo-grid').insertAdjacentHTML('afterend', customText);
    }
}

function likePhoto() {
    alert("Hai messo Mi Piace!");
}