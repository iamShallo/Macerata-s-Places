package com.unicam.projectzanncald.util;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Classe che rappresenta un punto bidimensionale con coordinate x y.
 */
@Entity
public class PuntoBid {

    // x and y coordinates of the punti
    private double x;
    private double y;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public PuntoBid(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PuntoBid() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distance(PuntoBid target) {
        double deltaX = this.x - target.getX();
        double deltaY = this.y - target.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

