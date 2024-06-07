package com.unicam.projectzanncald.model.content;

import jakarta.persistence.*;

import java.util.List;

/**
 *Rappresenta un itinerario, un tipo di contenuto che comprende un elenco di punti.
 * Estende il contenuto della classe base.
 */
@Entity
public class Itinerary extends Content {
    @ElementCollection
    @CollectionTable(name = "itinerary_points", joinColumns = @JoinColumn(name = "itinerary_id"))
    @Column(name = "point_id")
    private List<Integer> points;

    public Itinerary(String name, List<Integer> points, String description) {
        super(name, description);
        this.points = points;
    }

    public Itinerary() {
        super(null, "");
    }

    public List<Integer> getPoints() {
        return this.points;
    }

    public void addPoint(Integer point) {
        this.points.add(point);
    }

}
