package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an object that can be observed. This object can notify messages of type <T> to all it's observers.
 *
 * @param <T> the type of the messages that this observable notifies to its observers
 */
public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Adds a new Observer that will receive updates from this Observable.
     *
     * @param observer the observer that will be added
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Notifies a message to all observers.
     *
     * @param message the message that will be notified
     */
    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }
}
