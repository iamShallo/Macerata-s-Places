package com.unicam.projectzanncald.model.content;

import jakarta.persistence.*;

/**
 * La classe astratta che rappresenta il contenuto dell'applicazione.
 * Serve come classe base per vari tipi di contenuto.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Content {

    private int author;
    private String name;
    private String description;
    private boolean isValidate;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    public Content(String name, String description) {
        this.name = name;
        this.isValidate = false;
        this.description = description;
    }

    public Content() {

    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author){this.author = author;}

    public String getName() {
        return name;
    }

    public boolean isValidate() {
        return this.isValidate;
    }

    public void setValidation(boolean val) {
        this.isValidate = val;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String description){this.name =description;}

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}


