package model.utenti;
/**
 * Enumerazione che rappresenta diversi ruoli utente nel sistema.
 * Ciascun ruolo definisce un determinato livello di accesso o autorizzazioni.
 */
public enum UserRole {
    Contributor,
    ContributorAuthorized,
    Tourist,
    TouristAuthorized,
    Curator,
    Animator,
    PlatformManager
}
