package com.unicam.projectzanncald.controller;


import com.unicam.projectzanncald.Exception.*;
import com.unicam.projectzanncald.controller.Repository.ContestRespository;
import com.unicam.projectzanncald.controller.Repository.MultimediaRepository;
import com.unicam.projectzanncald.controller.Repository.PointRepository;
import com.unicam.projectzanncald.controller.Repository.UtentiRepository;
import com.unicam.projectzanncald.model.content.Content;
import com.unicam.projectzanncald.model.content.Contest;
import com.unicam.projectzanncald.model.content.Multimedia;
import com.unicam.projectzanncald.model.content.Point;
import com.unicam.projectzanncald.model.observer.MultimediaListener;
import com.unicam.projectzanncald.model.utenti.BaseUser;
import com.unicam.projectzanncald.model.utenti.UserRole;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

    private final MultimediaRepository multimediaRepository;
    private final UtentiRepository userRepository;
    private final ContestRespository contestRespository;
    private final PointRepository pointRepository;
    public static MultimediaListener listener;

    @Autowired
    public MultimediaController(MultimediaRepository multimediaRepository, UtentiRepository userRepository, ContestRespository contestRepository, PointRepository pointRepository) {
        this.multimediaRepository = multimediaRepository;
        this.userRepository = userRepository;
        this.contestRespository = contestRepository;
        this.pointRepository = pointRepository;
        listener = new MultimediaListener();
    }

    @RequestMapping(value="/add" , method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPointMultimedia(@RequestParam("file") MultipartFile file,
                                                @RequestParam("name") String name,
                                                @RequestParam("description") String description,
                                                @RequestParam("path") String path,
                                                @RequestParam("userId") Integer userId,
                                                @RequestParam("puntoId") Integer pointId)
    {

        Multimedia multimedia = new Multimedia(name,description,path);
        BaseUser user = userRepository.findById(userId).orElseThrow(UtenteNotExistException::new);
        Point point = pointRepository.findById(pointId).orElseThrow(PointNotExistException::new);
        multimedia.setAuthor(user.getId());
        multimedia.setPointId(point.getId());

        if (!(user.getUserType().equals(UserRole.Tourist) || user.getUserType().equals(UserRole.PlatformManager))){
            if (user.getUserType().equals(UserRole.Curator) || user.getUserType().equals(UserRole.ContributorAuthorized)) {
                addContentNoPending(multimedia);
            } else {
                addContentPending(multimedia);
            }
            this.addFile(file, path);
        }else throw new UtenteBadTypeException();
        return new ResponseEntity<>("Multimedia created", HttpStatus.OK);
    }

    public void addContentNoPending(Multimedia content) {
        content.setValidation(true);
        multimediaRepository.save(content);
        listener.notifyObservers("Multimedia: " + content.getName());
    }

    private void addContentPending(Multimedia content) {
        content.setValidation(false);
        multimediaRepository.save(content);
    }

    @RequestMapping(value="/validate{choice}{multimediaId}{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> validateContent(@PathParam(("userId")) int userId, @PathParam(("choice")) boolean choice,
    @PathParam(("multimediaId")) int multimediaId) {
        BaseUser user = userRepository.findById(userId).orElseThrow(UtenteNotExistException::new);
        Multimedia multimedia = multimediaRepository.findById(multimediaId).orElseThrow(MultimediaNotFoundException::new);
        if (user.getUserType().equals(UserRole.Curator)){
            if (choice) {
                multimedia.setValidation(true);
                multimediaRepository.save(multimedia);
                return new ResponseEntity<>("Multimedia validated", HttpStatus.OK);
            }
            multimediaRepository.deleteById(multimediaId);
            return new ResponseEntity<>("Multimedia eliminated", HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    public long getContentListSize() {
        return multimediaRepository.count();
    }

    @RequestMapping(value="/modify{description}{id}{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifyDescription(@PathParam("description") String description,@PathParam("id") int id,@PathParam("userId") int userId){
        Multimedia multimedia = this.multimediaRepository.findById(id).orElseThrow(MultimediaNotFoundException::new);
        BaseUser user = userRepository.findById(userId).orElseThrow(UtenteNotExistException::new);
        if (multimedia.getAuthor() == userId) {
            if (user.getUserType().equals(UserRole.Curator) || user.getUserType().equals(UserRole.ContributorAuthorized)){
                multimedia.setDescription(description);
                multimediaRepository.save(multimedia);
                return new ResponseEntity<>("Multimedia modified", HttpStatus.OK);
            }
            multimedia.setDescription(description);
            multimedia.setValidation(false);
            multimediaRepository.save(multimedia);
        }else throw new UtenteBadTypeException();
        return new ResponseEntity<>("Multimedia not modified", HttpStatus.OK);
    }

    @DeleteMapping("/delete{id}{userId}")
    public ResponseEntity<?> deleteContent(@PathParam("userId") int userId,@PathParam("id") int id){
        Multimedia multimedia = multimediaRepository.findById(id).orElseThrow(MultimediaNotFoundException::new);
        if (userRepository.findById(userId).orElseThrow(UtenteNotExistException::new).getUserType().equals(UserRole.Curator)){
            for (Contest contest: contestRespository.findAll()) {
                if(contest.getMultimediaList().contains(id)){
                    contest.deleteMultimedia(id);
                }
            }
            multimediaRepository.delete(multimedia);
            return new ResponseEntity<>("Multimedia deleted", HttpStatus.OK);
        } else throw new UtenteBadTypeException();
    }

    @RequestMapping(value="/signal{userId}{multimediaId}", method = RequestMethod.PUT)
    public ResponseEntity<?> signalContent(@PathParam(("userId")) int userId,@PathParam(("multimediaId")) int multimediaId) {
        Multimedia multimedia = multimediaRepository.findById(multimediaId).orElseThrow(MultimediaNotFoundException::new);
        BaseUser user = userRepository.findById(userId).orElseThrow(UtenteNotExistException::new);
        if (!(user.getUserType().equals(UserRole.Curator) || user.getUserType().equals(UserRole.PlatformManager)
                || user.getUserType().equals(UserRole.Animator))){
            multimedia.setSignaled(true);
            multimediaRepository.save(multimedia);
            return new ResponseEntity<>("Multimedia signaled",HttpStatus.OK);
        }else throw new UtenteBadTypeException();
    }

    @GetMapping(value ="/getAll")
    public ResponseEntity<?> getMultimedia(){
        return new ResponseEntity<>(multimediaRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value ="/getAllAuthorized")
    public ResponseEntity<?> getAuthorizedMultimedia(){
        return new ResponseEntity<>(multimediaRepository.findAll().stream().filter(Content::isValidate), HttpStatus.OK);
    }

    @RequestMapping(value="/add/contest" , method=RequestMethod.POST, consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addWithPending(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("path") String path,
                                                      @RequestParam("userId") Integer userId,
                                                      @RequestParam("contestId") Integer contestId) {
        Multimedia multimedia = new Multimedia(name,description,path);
        Contest contest = contestRespository.findById(contestId).orElseThrow(ContestNotExistException::new);
        BaseUser user = userRepository.findById(userId).orElseThrow(UtenteNotExistException::new);
        multimedia.setAuthor(user.getId());
        this.addContentPending(multimedia);
        contest.addMultimedia(multimedia.getId());
        contestRespository.save(contest);
        this.addFile(file, multimedia.getPath());
        return new ResponseEntity<>("Multimedia added", HttpStatus.OK);
    }

    @Value("${upload.directory}")
    private String uploadDirectory;

    private void addFile(MultipartFile file,String path){
        String projectDirectory = System.getProperty("user.dir");
        String finalPath = projectDirectory + File.separator + uploadDirectory + File.separator + path+ ".jpg";
        try {
            file.transferTo(new File(finalPath));
        } catch (IOException e) {
            throw new FileException();
        }
    }

}

