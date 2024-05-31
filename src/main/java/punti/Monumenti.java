package punti;

import com.unicam.projectzanncald.model.content.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.Date;
/**
 * Questa classe rappresenta un monumento, ovvero un tipo di punto su una mappa che indica un sito storico o culturale significativo.
 * Estende la classe Point e aggiunge campi per memorizzare la data di inaugurazione e una breve storia o descrizione del monumento.
 */
@Entity
public class Monumenti extends Point {
    @Column(name="inaugurationDate")
    private Date inaugurationDate;
    @Column(name="story")
    private String story;

    public Monumenti(Float x, Float y, String type, String name, String story, Date inaugurationDate) {
        super(x, y,type , name);
        this.inaugurationDate = inaugurationDate;
        this.story = story;
    }

    public Monumenti() {

    }

    public Date getInaugurationDate() {
        return inaugurationDate;
    }

    public String getStory() {
        return story;
    }

    @Override
    public Monumenti clone() {
        return new Monumenti(this.getX(), this.getY(),super.getName(), super.getType(), this.getStory(), this.getInaugurationDate());
    }
}
