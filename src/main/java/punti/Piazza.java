package punti;

import com.unicam.projectzanncald.model.content.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Rappresenta una piazza, che è un tipo di punto su una mappa tipicamente utilizzato come spazio pubblico aperto.
 * Estende la classe Point e include un campo aggiuntivo per memorizzare informazioni storiche sulla piazza.
 */
@Entity
public class Piazza extends Point {
    @Column(name="history")
    private String history;

    public Piazza(Float x, Float y, String type, String name, String history) {
        super(x , y, type,name);
        this.history = history;
    }

    public Piazza() {
    }

    public String getHistory() {
        return history;
    }

    @Override
    public Piazza clone() {
        return new Piazza(this.getX(), this.getY(), super.getType(),super.getName(), this.history);
    }
}
