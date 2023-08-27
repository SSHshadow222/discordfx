package com.example.discordfx.service;

import com.example.discordfx.utils.observer.Observer;
import com.example.discordfx.utils.observer.Subject;

public abstract class Service implements Subject
{
    @Override
    public boolean addObserver(Observer o) {
        boolean found = false;
        for(Observer _o: observers){
            if (_o.equals(o)) {
                found = true;
                break;
            }
        }

        if (!found){
            observers.add(o);
        }

        return found;
    }

    @Override
    public boolean removeObserver(Observer o) {
        return observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}