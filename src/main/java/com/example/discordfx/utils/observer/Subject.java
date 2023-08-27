package com.example.discordfx.utils.observer;

import java.util.ArrayList;
import java.util.List;

public interface Subject
{
    List<Observer> observers = new ArrayList<>();

    /**
     * Introduces a new observer
     *
     * @param o The new observer
     * @return True if the observer was successfully introduced, false otherwise.
     */
    boolean addObserver(Observer o);

    /**
     * Removes an already existing observer
     *
     * @param o The observer to be removed
     * @return True if the observer was successfully removed, false otherwise
     */
    boolean removeObserver(Observer o);

    /**
     * Notifies all the observers that a change has occurred
     */
    void notifyObservers();
}
