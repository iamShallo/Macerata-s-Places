package com.unicam.projectzanncald.controller.Repository;

import com.unicam.projectzanncald.model.content.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultimediaRepository extends JpaRepository<Multimedia,Integer> {
}