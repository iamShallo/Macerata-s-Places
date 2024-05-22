package com.unicam.projectzanncald.controller.Repository;

import com.unicam.projectzanncald.util.PuntoBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointBidRepository extends JpaRepository<PuntoBid,Integer> {

    @Query("SELECT z.id FROM PuntoBid z WHERE z.x = :x AND z.y = :y")
    void deleteByCoordinate(@Param("x") Float x, @Param("y") Float y);
}
