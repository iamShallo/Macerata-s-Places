package com.unicam.projectzanncald.model.observer;

public class ConcreteObserver implements Observer {
    @Override
    public void update(String message) {
        System.out.println("ConcreteObserver received message: " + message);
    }
}