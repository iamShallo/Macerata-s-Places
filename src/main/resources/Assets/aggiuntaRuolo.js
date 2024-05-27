const mostraModuloBtn = document.getElementById('mostraModuloBtn');
const registrazioneForm = document.getElementById('registrazioneForm');
const rimuoviModuloBtn = document.getElementById('rimuoviModuloBtn');
const rimozioneForm = document.getElementById('rimozioneForm');
const board = document.getElementById("board");
const popup = document.querySelector(".popup");
const popupBackground = document.getElementById("popupBackground");

mostraModuloBtn.addEventListener('click', toggleFormDisplay.bind(null, registrazioneForm));
rimuoviModuloBtn.addEventListener('click', toggleFormDisplay.bind(null, rimozioneForm));
document.querySelector(".close-button").addEventListener("click", closePopup);
document.querySelector(".close-button2").addEventListener("click", closePopup);

registrazioneForm.addEventListener("submit", function(event) {
    event.preventDefault();
    hidePopup();
});

rimozioneForm.addEventListener("submit", function(event) {
    event.preventDefault();
    hidePopup();
});

function toggleFormDisplay(form) {
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
    board.style.display = 'none';
    popup.style.display = 'block';
    popupBackground.style.display = 'flex';
}

function closePopup() {
    board.style.display = 'block';
    popup.style.display = 'none';
    popupBackground.style.display = 'none';
}

function hidePopup() {
    closePopup();
    const formFields = document.querySelectorAll("#registrazioneForm input, #registrazioneForm select, #rimozioneForm input, #rimozioneForm select");
    formFields.forEach(function(field) {
        field.removeAttribute("required");
    });
}

function createUser() {
    const users = {
        role: document.getElementById("ruolo").value,
        name: document.getElementById("nome").value,
        surname: document.getElementById("cognome").value,
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    fetch('http://localhost:8080/users/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(users),
    })
        .then(handleResponse)
        .catch(handleError);
}

function deleteUser() {
    const email = document.getElementById("mail").value;
    fetch('http://localhost:8080/users/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: email
    })
        .then(handleResponse)
        .catch(handleError);
}

function getUsersFromDatabase() {
    fetch(`http://localhost:8080/users/getAll`)
        .then(handleResponse)
        .then(displayUsers)
        .catch(handleError);
}

function displayUsers(users) {
    const userListElement = document.querySelector('.utenti-list');
    userListElement.innerHTML = '';

    users.forEach(user => {
        const li = document.createElement('li');
        li.textContent = `E-mail: ${user.email} Ruolo: ${user.userType}`;
        if (user.userType === "Contributor") {
            const button = document.createElement('button');
            button.classList.add('editButton', 'btn', 'btn-secondary');
            button.textContent = 'Modifica ruolo';
            button.addEventListener('click', () => {
                openRoleChangePopup(user.email, user.name);
            });
            li.appendChild(button);
        }
        userListElement.appendChild(li);
    });
}

function openRoleChangePopup(email, name) {
    const popupContainer = createPopupContainer();
    const popupContent = createPopupContent();

    const promptText = document.createElement('p');
    promptText.textContent = `Cosa vuoi fare con il ruolo di ${name}?`;

    const buttonChange = createPopupButton('Animatore', () => makeRoleChange(email, "Animator"));
    const buttonCancel = createPopupButton('Curatore', () => makeRoleChange(email, "Curator"));

    appendElements(popupContent, [promptText, buttonChange, buttonCancel]);
    appendElements(popupContainer, [popupContent]);
    document.body.appendChild(popupContainer);
}

function createPopupContainer() {
    const popupContainer = document.createElement('div');
    popupContainer.classList.add('popup-container');
    return popupContainer;
}

function createPopupContent() {
    const popupContent = document.createElement('div');
    popupContent.classList.add('popup-content');
    popupContent.style.backgroundColor = 'white';
    popupContent.style.padding = '20px';
    popupContent.style.borderRadius = '8px';
    popupContent.style.boxShadow = '0 0 10px rgba(0, 0, 0, 0.3)';
    popupContent.style.maxWidth = '300px';
    popupContent.style.textAlign = 'center';
    popupContent.style.position = 'fixed';
    popupContent.style.top = '50%';
    popupContent.style.left = '50%';
    popupContent.style.transform = 'translate(-50%, -50%)';
    return popupContent;
}

function createPopupButton(text, onClick) {
    const button = document.createElement('button');
    button.textContent = text;
    button.style.margin = '0 10px';
    button.style.padding = '8px 16px';
    button.style.border = 'none';
    button.style.borderRadius = '4px';
    button.style.backgroundColor = '#007bff';
    button.style.color = 'white';
    button.style.cursor = 'pointer';
    button.addEventListener('click', onClick);
    return button;
}

function appendElements(parent, children) {
    children.forEach(child => parent.appendChild(child));
}

function makeRoleChange(email, role) {
    fetch(`http://localhost:8080/users/changeRole?email=${email}&role=${role}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ managerEmail: ManagerEmail }),
    })
        .then(handleResponse)
        .catch(handleError);
}

function handleResponse(response) {
    if (!response.ok) {
        throw new Error('Errore nella richiesta Fetch');
    }
    return response.json();
}

function handleError(error) {
    console.error('Si è verificato un errore:', error);
}

function refreshPage() {
    setTimeout(function() {
        location.reload();
    }, 2000);
}

function getUsersFromDatabase() {
    fetch(`http://localhost:8080/users/getAll`)
        .then(handleResponse) // Gestisce la risposta della chiamata fetch
        .then(displayUsers)   // Passa gli utenti alla funzione che li visualizza
        .catch(handleError);  // Gestisce eventuali errori durante la chiamata
}

function handleResponse(response) {
    if (!response.ok) {
        throw new Error('Errore nella richiesta Fetch');
    }
    return response.json();
}

function handleError(error) {
    console.error('Si è verificato un errore:', error);
}

function createUser() {
    const users = {
        role: document.getElementById("ruolo").value,
        name: document.getElementById("nome").value,
        surname: document.getElementById("cognome").value,
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    fetch('http://localhost:8080/users/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(users),
    })
        .then(handleResponse)
        .then(() => {
            refreshPage(); // Aggiorna la pagina dopo l'aggiunta dell'utente
        })
        .catch(handleError);
}

function deleteUser() {
    const email = document.getElementById("mail").value;
    fetch('http://localhost:8080/users/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: email
    })
        .then(handleResponse)
        .then(() => {
            refreshPage(); // Aggiorna la pagina dopo la rimozione dell'utente
        })
        .catch(handleError);
}