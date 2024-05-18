package controller;

import com.unicam.projectzanncald.Exception.ItinerariNotExistException;
import com.unicam.projectzanncald.Exception.PointNotExistException;
import com.unicam.projectzanncald.Exception.UtenteBadTypeException;
import com.unicam.projectzanncald.Exception.UtenteNotExistException;
import com.unicam.projectzanncald.controller.Repository.ItineraryRepository;
import com.unicam.projectzanncald.controller.Repository.PointRepository;
import com.unicam.projectzanncald.controller.Repository.UtentiRepository;
import com.unicam.projectzanncald.model.content.Itinerary;
import com.unicam.projectzanncald.model.content.Point;
import com.unicam.projectzanncald.model.utenti.BaseUser;
import com.unicam.projectzanncald.model.utenti.UserRole;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * La classe controller Itinerario gestisce l'aggiunta e la convalida dell'itinerario,
 *distinzione tra aggiunta immediata e approvazione in attesa in base al ruolo dell'utente.
 */

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/itineraries")
public class ItineraryController {

    private final ItineraryRepository itineraryRepository;

    private final PointRepository pointRepository;

    private final UtentiRepository users;

    @Autowired
    public ItineraryController(ItineraryRepository itinerariesList, PointRepository pointRepository, UtentiRepository users) {
        this.itineraryRepository = itinerariesList;
        this.pointRepository = pointRepository;
        this.users = users;
    }

    @PostMapping("/add{userId}")
    public ResponseEntity<?> addItinerary(@RequestBody Itinerary itinerary, @PathParam("userId") int userId) {
        BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
        if (!(user.getUserType().equals(UserRole.Tourist) || user.getUserType().equals(UserRole.Animator))) {
            this.pointExistControl(itinerary);
            itinerary.setAuthor(userId);

            if (user.getUserType().equals(UserRole.Contributor))
                this.addWithPending(itinerary);
            else
                this.addWithoutPending(itinerary);

            return new ResponseEntity<>("Itinerary created", HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    private void pointExistControl(Itinerary itinerary){
        for (Integer pointId: itinerary.getPoints()) {
            if(!pointRepository.existsById(pointId) || !pointRepository.findById(pointId).get().isValidate())
                throw new PointNotExistException();
        }
    }

    private void addWithPending(Itinerary itinerary){
        itinerary.setValidation(false);
        this.itineraryRepository.save(itinerary);
    }

    private void addWithoutPending(Itinerary itinerary){
        itinerary.setValidation(true);
        this.itineraryRepository.save(itinerary);
    }

    @PostMapping("/validate{id}{userId}")
    public ResponseEntity<?> validateItinerary(@PathParam(("userId")) int userId, @PathParam(("id")) int id, @RequestBody boolean choice) {
        BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
            if(user.getUserType().equals(UserRole.Curator)){
                Itinerary itinerary = itineraryRepository.findById(id).orElseThrow(ItinerariNotExistException::new);
                    if (choice) {
                        itinerary.setValidation(true);
                        itineraryRepository.save(itinerary);
                        return new ResponseEntity<>("Itinerary validated", HttpStatus.OK);
                    }
                    this.itineraryRepository.deleteById(id);
                    return new ResponseEntity<>("Itinerary deleted", HttpStatus.OK);
            }
        return new ResponseEntity<>("Itinerary not validated", HttpStatus.OK);
    }

    public long getItinerariesSize(){
        return itineraryRepository.findAll().parallelStream()
                .filter(itinerary -> !itinerary.isValidate())
                .count();
    }

    public List<Itinerary> getPendingItineraries(){
        return itineraryRepository.findAll().stream()
                .filter(itinerary -> !itinerary.isValidate()).toList();
    }

    @PostMapping("/search{title}")
    public Optional<Itinerary> searchPoint(@PathParam(("title"))String title) {
        return itineraryRepository.findByDescription(title);
    }

    @GetMapping(value ="/getAll/Points")
    public ResponseEntity<?> getItinerariesWithPoints(){
        Map<Integer, List<Point>> result = new HashMap<>();
        for (Itinerary itinerary : itineraryRepository.findAllItineraries()) {
            List<Point> points = new ArrayList<>();
            for (Integer id : itinerary.getPoints()) {
                pointRepository.findById(id).ifPresent(points::add);
            }
            result.put(itinerary.getId(), points);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value ="/getAll")
    public ResponseEntity<?> getItineraries(){
        if(itineraryRepository.findAllItineraries().isEmpty())
            return new ResponseEntity<>("No itineraries found", HttpStatus.OK);
        return new ResponseEntity<>(itineraryRepository.findAllItineraries(), HttpStatus.OK);
    }

    @GetMapping("/get/itinerary{id}")
    public ResponseEntity<?> getItinerary(@PathParam("id") int id){
        List<Point> points = new ArrayList<>();
        Itinerary itinerary = itineraryRepository.findById(id).orElseThrow(ItinerariNotExistException::new);
            for (Integer pointId : itinerary.getPoints()) {
                pointRepository.findById(pointId).ifPresent(points::add);
            }
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @DeleteMapping("/delete{itineraryId}{userId}")
    public ResponseEntity<?> deleteItinerary(@PathParam("itineraryId") int itineraryId , @PathParam("userId") int userId){
        BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(ItinerariNotExistException::new);
            if(user.getId() == itinerary.getAuthor() || user.getUserType().equals(UserRole.Curator))
                itineraryRepository.delete(itinerary);
        return new ResponseEntity<>("Itinerary deleted", HttpStatus.OK);
    }

}
