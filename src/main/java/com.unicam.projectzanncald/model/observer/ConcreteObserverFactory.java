package com.unicam.projectzanncald.model.observer;

import com.unicam.projectzanncald.model.Factory.ObserverFactory;
import com.unicam.projectzanncald.model.observer.Observer;
import com.unicam.projectzanncald.model.observer.ConcreteObserver;

public class ConcreteObserverFactory implements ObserverFactory {
    @Override
    public Observer createObserver() {
        return new ConcreteObserver();
    }
}