package com.unicam.projectzanncald.PrototypeDesignPatternTest;

import com.unicam.projectzanncald.model.content.Point;
import com.unicam.projectzanncald.model.punti.Monumenti;
import com.unicam.projectzanncald.model.punti.Ristorante;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class PrototypeDesignPatternTest {
    @Test
    public void testClone() {
        // Creazione di un oggetto Monumenti per Macerata
        Float xMonumento = 43.3000f;
        Float yMonumento = 13.4500f;
        String typeMonumento = "Monumento";
        String nameMonumento = "Palazzo Buonaccorsi";
        String storyMonumento = "Palazzo storico di Macerata";
        Date inaugurationDateMonumento = new Date();

        Monumenti monumento = new Monumenti(xMonumento, yMonumento, typeMonumento, nameMonumento, storyMonumento, inaugurationDateMonumento);

        // Creazione di un oggetto Ristorante per Macerata
        Float xRistorante = 43.3000f;
        Float yRistorante = 13.4567f;
        String typeRistorante = "Ristorante";
        String nameRistorante = "Osteria dei Fiori";
        String typeRestaurant = "Italiano";
        String openingHours = "12:00 - 15:00, 19:00 - 23:00";

        Ristorante ristorante = new Ristorante(xRistorante, yRistorante, typeRistorante, nameRistorante, typeRestaurant, openingHours);

        // Aggiunta dei punti all'elenco
        List<Point> points = new ArrayList<>();
        points.add(monumento);
        points.add(ristorante);

        // Clonazione dei punti
        List<Point> pointsCopy = new ArrayList<>();
        for (Point point : points) {
            pointsCopy.add(point.clone());
        }

        // Confronto e output
        for (int i = 0; i < points.size(); i++) {
            System.out.println(points.get(i) + "-----" + pointsCopy.get(i));
            if (points.get(i) != pointsCopy.get(i)) {
                System.out.print(i + ": ");
                if (points.get(i).equals(pointsCopy.get(i))) {
                    System.out.println("Gli oggetti sono differenti ma identici");
                } else {
                    System.out.println("Gli oggetti sono differenti e non identici");
                }
            } else {
                System.out.println(i + ": Gli oggetti sono gli stessi");
            }
        }
    }
}
