package com.unicam.projectzanncald.model.content;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un'implementazione di base della classe {@link Content}.
 * Questa classe fornisce metodi riguardanti i concorsi.
 */
@Entity
public class Contest extends Content {
    @ElementCollection
    @CollectionTable(name = "contest_users", joinColumns = @JoinColumn(name = "contenst_id"))
    @Column(name = "users_id")
    private List<Integer> allowedUsers;
    @ElementCollection
    @CollectionTable(name = "contest_users", joinColumns = @JoinColumn(name = "contenst_id"))
    @Column(name = "multimedia_id")
    private List<Integer> multimediaList;
    private boolean privacy;

    public Contest(int id, String name, String description, boolean privacy) {
        super(name,description);
        this.privacy = privacy;
        this.multimediaList=new ArrayList<Integer>();
    }

    public Contest() {}

    public List<Integer> getAllowedUsers() {
        return this.allowedUsers;
    }

    public boolean getPrivacy(){ return this.privacy;}

    public void deleteMultimedia(int id){
        for(int currentId : multimediaList){
            if(currentId == id){
                multimediaList.remove(id);
            }
        }
    }

    public void addAllowedUsers(int user){
        this.allowedUsers.add(user);
    }

    public List<Integer> getMultimediaList() {
        return multimediaList;
    }

    public void addMultimedia(int multimedia){
        this.multimediaList.add(multimedia);
    }
}
