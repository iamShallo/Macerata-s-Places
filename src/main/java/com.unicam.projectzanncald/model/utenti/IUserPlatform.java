package com.unicam.projectzanncald.model.utenti;

import jakarta.persistence.MappedSuperclass;

/**
 * Interfaccia che rappresenta una piattaforma utente in un sistema.
 * Definisce i metodi per recuperare le informazioni di base su un utente.
 */
@MappedSuperclass
public interface IUserPlatform {

    int getId();

    UserRole getUserType();

    String getName();

    String getSurname();

    String getUsername();

    String getEmail();

    String getPassword();

    void setRole(UserRole role);
}
