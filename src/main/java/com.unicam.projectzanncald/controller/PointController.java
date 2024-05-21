package com.unicam.projectzanncald.controller;

import com.unicam.projectzanncald.Exception.PointAlreadyInException;
import com.unicam.projectzanncald.Exception.PointNotExistException;
import com.unicam.projectzanncald.Exception.UtenteBadTypeException;
import com.unicam.projectzanncald.Exception.UtenteNotExistException;
import com.unicam.projectzanncald.controller.Repository.PointBidRepository;
import com.unicam.projectzanncald.controller.Repository.PointRepository;
import com.unicam.projectzanncald.controller.Repository.UtentiRepository;
import com.unicam.projectzanncald.model.content.Point;
import com.unicam.projectzanncald.model.punti.AreeVerdi;
import com.unicam.projectzanncald.model.punti.Monumenti;
import com.unicam.projectzanncald.model.punti.Piazza;
import com.unicam.projectzanncald.model.punti.Ristorante;
import com.unicam.projectzanncald.model.utenti.BaseUser;
import com.unicam.projectzanncald.model.utenti.UserRole;
import com.unicam.projectzanncald.util.PuntoBid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * La classe PointController gestisce la logica per la gestione degli oggetti Point, inclusa l'addizione,
 * operazioni di validazione e ricerca.
 */
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/points")
public class PointController {
    private final PointRepository points;
    private final PointBidRepository points2D;
    private final UtentiRepository users;

    @Autowired
    public PointController(PointRepository points, PointBidRepository points2D, UtentiRepository users) {
        this.points = points;
        this.points2D = points2D;
        this.users = users;
    }

    @PostMapping("/addRestaurant{userId}")
    public ResponseEntity<?> addPointRestaurant(@RequestBody Ristorante point, @PathParam(("userId")) int userId) {
        return addPoint(point,userId);
    }

    @PostMapping("/addMonument{userId}")
    public ResponseEntity<?> addPointMonument(@RequestBody Monumenti point, @PathParam(("userId")) int userId) {
        return addPoint(point,userId);
    }

    @PostMapping("/addGreenZone{userId}")
    public ResponseEntity<?> addPointGreenZone(@RequestBody AreeVerdi point, @PathParam(("userId")) int userId) {
        return addPoint(point, userId);
    }

    @PostMapping("/addSquare{userId}")
    public ResponseEntity<?> addPointSquare(@RequestBody Piazza point, @PathParam(("userId")) int userId) {
        return addPoint(point, userId);
    }

    private ResponseEntity<?> addPoint(Point point, int userId) {
        if (points.isAlreadyIn(point.getX(), point.getY()) == 0 && points.findAllByTitle(point.getName()) == null) {
            points2D.save(new PuntoBid(point.getX(), point.getY()));
            BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
                point.setAuthor(userId);
                if(user.getUserType().equals(UserRole.Contributor))
                    this.addWithPending(point);
                else
                    this.addWithoutPending(point);
                return new ResponseEntity<>("Point created", HttpStatus.OK);
            } else throw new PointAlreadyInException();
    }

    private void addWithPending(Point point) {
        point.setValidation(false);
        this.points.save(point);
    }

    private void addWithoutPending(Point point) {
        point.setValidation(true);
        this.points.save(point);
    }

    @RequestMapping(value = "/validate{choice}{userId}{pointId}", method = RequestMethod.PUT)
    public ResponseEntity<?> validatePoint(@PathParam("choice") boolean choice, @PathParam("userId") int userId, @PathParam("pointId") int pointId) {
        if (users.findById(userId).orElseThrow(UtenteNotExistException::new).getUserType().equals(UserRole.Curator)) {
            Point point = points.findById(pointId).orElseThrow(PointAlreadyInException::new);
                if (choice) {
                    point.setValidation(true);
                    points.save(point);
                    return new ResponseEntity<>("Point Validated", HttpStatus.OK);
                } else {
                    points2D.deleteByCoordinate(point.getX(), point.getY());
                    this.points.deleteById(pointId);
                    return new ResponseEntity<>("Point Deleted", HttpStatus.OK);
                }
            }else throw new UtenteBadTypeException();
    }

    @RequestMapping(value = "/search{title}" , method = RequestMethod.GET)
    public ResponseEntity<?> searchPoint(@PathParam("title") String title) {
        Optional<Integer> id = Optional.of(points.findAllByTitle(title));
        return new ResponseEntity<>(points.findById(id.get()),HttpStatus.OK);
    }

    @GetMapping(value ="/getAll")
    public ResponseEntity<?> getPoints(){
        return new ResponseEntity<>(points.findAll(),HttpStatus.OK);
    }

    @GetMapping(value ="/getAllAuthorized")
    public ResponseEntity<?> getAuthorizedPoints(){
        return new ResponseEntity<>(points.findAll().stream().filter(Point::isValidate),HttpStatus.OK);
    }

    @GetMapping(value = "/delete{id}{userId}")
    public ResponseEntity<?> pointDelete(@PathParam("id") int id, @PathParam("userId") int userId){
        BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
        Point point = points.findById(id).orElseThrow(PointNotExistException::new);
        if(user.getUserType().equals(UserRole.Curator) || point.getAuthor() == userId){
            points.delete(point);
        }
        return new ResponseEntity<>("Point Deleted", HttpStatus.OK);
    }

    @GetMapping(value="/clone{id}")
    public Point getCloneById(@PathParam("id") int id) {
        Point originalPoint = points.getById(id);
        return originalPoint.clone();
    }
}

