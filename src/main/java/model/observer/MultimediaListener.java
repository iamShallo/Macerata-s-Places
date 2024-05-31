/**
 * The MultimediaListener class represents a subject in the Observer design pattern,
 * responsible for managing a list of observers and notifying them when a new multimedia post is published.
 */
package model.observer;

import java.util.ArrayList;
import java.util.List;

public class MultimediaListener {

    private List<Observer> observers;

    public MultimediaListener(){
        this.observers = new ArrayList<>();
    }

    public void publishPost(String post) {
        notifyObservers(post);
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
