package punti;

import com.unicam.projectzanncald.model.content.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Questa classe rappresenta le aree verdi, ovvero un tipo di punto su una mappa con caratteristiche specifiche, come parchi o giardini.
 * Estende la classe Point e aggiunge un campo per memorizzare le caratteristiche specifiche delle zone verdi.
 */
@Entity
public class AreeVerdi extends Point {
    @Column(name="characteristics")
    private String characteristics;
    public AreeVerdi(Float x, Float y, String type, String name, String characteristics) {
        super(x , y, type, name);
        this.characteristics = characteristics;
    }

    public AreeVerdi() {
    }
    public String getCharacteristics() {
        return characteristics;
    }

    @Override
    public AreeVerdi clone() {
        return new AreeVerdi(this.getX(), this.getY(), super.getType(),super.getName(), this.getCharacteristics());
    }
}
