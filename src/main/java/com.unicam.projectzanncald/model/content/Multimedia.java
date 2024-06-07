package com.unicam.projectzanncald.model.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 *Rappresenta un contenuto multimediale con informazioni sull'utente, una foto, una descrizione e uno stato di segnalazione.
 * Questa classe estende la classe Content e ne eredita le proprietà.
 */
@Entity
public class Multimedia extends Content {
    @Column
    private boolean signaled;
    private String path;
    private Integer pointId;

    public Multimedia(String name, String description, String path) {
        super(name, description);
        this.path = path;
        this.signaled = false;
    }

    public Multimedia() {
        super(null, "");
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getSignaled() {
        return signaled;
    }

    public void setSignaled(boolean signaled) {
        this.signaled = signaled;
    }

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }

}
