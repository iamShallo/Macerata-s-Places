document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Evita il comportamento predefinito del form

    var email = document.getElementById('email').value.toLowerCase();
    switch (true){
        case email.includes('animatore'):
            window.location.href = '/animatore/animatore.html';
            break;
        case email.includes('contributorautorizzato'):
            window.location.href = 'contributorAutorizzato/contributorAutorizzato.html';
            break;
        case email.includes('curatore'):
            window.location.href = '/curatore/curatore.html';
            break;
        case email.includes('turistaautorizzato'):
            window.location.href = '/turistaAutorizzato/turistaAutorizzato.html';
            break;
        case email.includes('gestorepiattaforma'):
            window.location.href = '../../java/model/gestorePiattaforma/gestorePiattaforma.html';
            break;
        default:
            alert('Effettua il login');
    }
});

