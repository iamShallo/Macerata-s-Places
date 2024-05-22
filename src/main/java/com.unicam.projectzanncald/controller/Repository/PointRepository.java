package com.unicam.projectzanncald.controller.Repository;
import com.unicam.projectzanncald.model.content.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point,Integer> {
    @Query ("SELECT COUNT (z) FROM PuntoBid z WHERE z.x = :x AND z.y= :y")
    int isAlreadyIn(@Param("x") Float x, @Param("y") Float y);

    @Query ("SELECT COUNT (z) FROM Point z WHERE z.name = :name")
    int isAlreadyInByTitle(@Param("name") String name);

    @Query("SELECT m.id FROM Monumenti m WHERE m.name = :title " +
            "UNION " +
            "SELECT g.id FROM AreeVerdi g WHERE g.name = :title " +
            "UNION " +
            "SELECT r.id FROM Ristorante r WHERE r.name = :title " +
            "UNION " +
            "SELECT s.id FROM Piazza s WHERE s.name = :title")
    Integer findAllByTitle(@Param("title") String title);
}
