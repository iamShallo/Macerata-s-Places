package com.unicam.projectzanncald.controller.Repository;

import com.unicam.projectzanncald.model.utenti.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtentiRepository extends JpaRepository<BaseUser,Integer> {
    @Query ("SELECT COUNT(user) FROM BaseUser user WHERE user.email= :email")
    int countByEmail(@Param("email") String email);

    @Query ("SELECT user FROM BaseUser user WHERE user.email= :email")
    BaseUser selectByEmail(@Param("email") String email);
}
