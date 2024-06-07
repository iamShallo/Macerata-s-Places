package com.unicam.projectzanncald.model.content;

import jakarta.persistence.*;

/**
 * Rappresenta un punto, un tipo di contenuto che include coordinate e un tipo specifico.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Point {

    private int author;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "isValidate")
    private boolean isValidate;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "type")
    private String type;
    private Float x;
    private Float y;

    public Point(Float x, Float y, String type, String name) {
        this.name = name;
        this.isValidate = false;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Point() {

    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public String getType() {
        return this.type;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getAuthor() {
        return author;
    }

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

    public void setName(String description) {
        this.name = description;
    }

    public abstract Point clone();
}
