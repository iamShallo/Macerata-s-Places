package punti;

import com.unicam.idsproject2324.model.content.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Rappresenta un ristorante, ovvero un tipo di punto su una mappa.
 * Estende la classe Punti e include campi aggiuntivi per memorizzare la tipologia del ristorante e i suoi orari di apertura.
 */
@Entity
public class Ristorante extends Point {
    @Enumerated(EnumType.STRING)
    private TipoRistoranti typeRestaurant;
    @Column(name="openingHours")
    private String openingHours;

    public Ristorante(Float x, Float y, String typeR, String name, String type, String openingHours) {
        super(x, y, typeR, name);
        this.typeRestaurant = TipoRistoranti.valueOf(TipoRistoranti.class,type);
        this.openingHours = openingHours;
    }

    public Ristorante() {
    }

    public TipoRistoranti getTypeRestaurant() {
        return typeRestaurant;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    @Override
    public Ristorante clone() {
        return new Ristorante(this.getX(), this.getY(), super.getName(),super.getType(), this.getTypeRestaurant().toString(), this.getOpeningHours());
    }
}

