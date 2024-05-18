package controller;

import com.unicam.projectzanncald.Exception.*;
import com.unicam.projectzanncald.controller.Repository.ContestRespository;
import com.unicam.projectzanncald.controller.Repository.UtentiRepository;
import com.unicam.projectzanncald.model.content.Contest;
import com.unicam.projectzanncald.model.utenti.BaseUser;
import com.unicam.projectzanncald.model.utenti.UserRole;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * La classe ContestController gestisce le operazioni relative ai concorsi.
 * Questo controller fornisce endpoint per aggiungere, rimuovere, invitare utenti e gestire i concorsi.
 */
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/contest")
public class ContestController {

    private final ContestRespository contestList;
    private final UtentiRepository users;

    @Autowired
    public ContestController(ContestRespository contestList, UtentiRepository users) {
        this.contestList = contestList;
        this.users = users;
    }

    @PostMapping("/add{userId}")
    public ResponseEntity<?> addContest(@RequestBody Contest contest, @PathParam(("userId")) int userId) {
        contest.setAuthor(userId);
        contest.setValidation(true);
        BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
        if (user.getUserType().equals(UserRole.Animator)) {
            this.contestList.save(contest);
            return new ResponseEntity<>("Contest created", HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    @DeleteMapping("/delete{userId}")
    public ResponseEntity<?> removeContest(@RequestBody int contestId, @PathParam(("userId")) int userId) {
        Contest contest = contestList.findById(contestId).orElseThrow(ContestNotExistException::new);
        BaseUser user = users.findById(userId).orElseThrow(UtenteNotExistException::new);
        if (user.getUserType().equals(UserRole.Animator)) {
            this.contestList.delete(contest);
            return new ResponseEntity<>("Contest deleted", HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    @RequestMapping(value = "/validateMultimedia{contestId}{multimediaId}{animatorId}{choice}", method = RequestMethod.PUT)
    public ResponseEntity<?> validateMultimedia(@PathParam(("multimediaId")) int multimediaId,
    @PathParam(("contestId")) int contestId, @PathParam(("animatorId")) int animatorId, @PathParam(("choice")) boolean choice)
    {
        BaseUser animator = users.findById(animatorId).orElseThrow(UtenteNotExistException::new);
        Contest contest = contestList.findById(contestId).orElseThrow(ContestNotExistException::new);

        if (!animator.getUserType().equals(UserRole.Animator) || !contest.getMultimediaList().contains(multimediaId))
            return new ResponseEntity<>("Multimedia not validated", HttpStatus.OK);
        if (choice) {
            contestList.save(contest);
            return new ResponseEntity<>("multimedia validated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Multimedia eliminated", HttpStatus.OK);
    }

    @RequestMapping(value = "/search{contestName}", method = RequestMethod.POST)
    public Optional<Contest> searchContest(@PathParam(("contestName")) String contestName) {
        return Optional.of(contestList.findAllByTitle(contestName));
    }

    @GetMapping(value ="/get")
    public ResponseEntity<?> getContest(){
        return new ResponseEntity<>(contestList.findAll(), HttpStatus.OK);
    }

    @GetMapping(value ="/get{contestId}")
    public ResponseEntity<?> getMultimedia(@PathParam("contestId") int contestId){
        Contest contest = contestList.findById(contestId).orElseThrow(ContestNotExistException::new);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
