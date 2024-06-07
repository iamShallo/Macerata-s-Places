package com.unicam.projectzanncald.model.utenti;

import com.unicam.projectzanncald.model.observer.Observer;
import jakarta.persistence.*;

/**
 * Rappresenta un'implementazione di base dell'interfaccia {@link IUserPlatform} e
 * implementa l'interfaccia {@link Observer} per l'osservazione della pubblicazione di content.
 * Questa classe fornisce funzionalità di base per operazioni relative all'utente e notifiche.
 */
@Entity
public class BaseUser implements IUserPlatform , Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private String name;
    private String surname;
    private  String username;
    private String password;
    @Column(unique = true)
    private String email;

    public BaseUser(int id, String role, String name, String surname, String username, String email, String password) {
        this.id = id;
        this.userRole = UserRole.valueOf(UserRole.class,role);
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public BaseUser() {

    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public UserRole getUserType() {
        return this.userRole;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSurname() {
        return this.surname;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {return this.password;}

    @Override
    public void setRole(UserRole role) {this.userRole= role;}

    @Override
    public void update(String message) {
        System.out.println(username + ": Notifica - Nuovo contenuto multimediale pubblicato: " + message);
    }
}
