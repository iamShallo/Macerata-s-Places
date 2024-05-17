package controller;

import com.unicam.projectzanncald.Exception.UtenteAlreadyInException;
import com.unicam.projectzanncald.Exception.UtenteBadTypeException;
import com.unicam.projectzanncald.Exception.UtenteNotExistException;
import com.unicam.projectzanncald.controller.Repository.UtentiRepository;
import com.unicam.projectzanncald.model.utenti.BaseUser;
import com.unicam.projectzanncald.model.utenti.UserRole;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Classe controller per la gestione di utenti.
 */
@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/users")
public class UtentiController {
    UtentiRepository utentiRepository;

    @Autowired
    public UtentiController(UtentiRepository utentiRepository){
        this.utentiRepository = utentiRepository;
    }

    @PostMapping("/addCurator{email}")
    public ResponseEntity<?> addCurator(@RequestBody String managerEmail, @PathParam("email") String email) {
        BaseUser manager = this.getUserByEmail(managerEmail);
        if(manager.getUserType().equals(UserRole.PlatformManager)){
            BaseUser user = getUserByEmail(email);
            user.setRole(UserRole.Curator);
            utentiRepository.save(user);
            return new ResponseEntity<>("Curator added", HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    @PostMapping("/addAnimator{email}")
    public ResponseEntity<?> addAnimator(@RequestBody String managerEmail, @PathParam("email") String email) {
        BaseUser manager = this.getUserByEmail(managerEmail);
        if(manager.getUserType().equals(UserRole.PlatformManager)){
            BaseUser user = getUserByEmail(email);
            user.setRole(UserRole.Curator);
            utentiRepository.save(user);
            return new ResponseEntity<>("Animator added", HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    @PostMapping("/updateContributor{contributorEmail}")
    public void changeRole(@RequestBody String managerEmail, @PathParam("contributorEmail") String email) {
        BaseUser manager = this.getUserByEmail(managerEmail);
        if(manager.getUserType().equals(UserRole.PlatformManager)){
            BaseUser user = getUserByEmail(email);
            user.setRole(UserRole.ContributorAuthorized);
            utentiRepository.save(user);
        }else throw new UtenteAlreadyInException();
    }

    @GetMapping(value ="/getAll")
    public ResponseEntity<Object> getUsers(){
        return new ResponseEntity<>(utentiRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody BaseUser user){
        if(utentiRepository.countByEmail(user.getEmail()) == 0){
            utentiRepository.save(user);
                        return new ResponseEntity<>("User created", HttpStatus.OK);
        }else throw new UtenteAlreadyInException();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String email){
        BaseUser user = this.getUserByEmail(email);
        this.utentiRepository.delete(user);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    private BaseUser getUserByEmail(String email){
       if(utentiRepository.countByEmail(email) == 0)
           throw new UtenteNotExistException();
       return utentiRepository.selectByEmail(email);
    }
}
