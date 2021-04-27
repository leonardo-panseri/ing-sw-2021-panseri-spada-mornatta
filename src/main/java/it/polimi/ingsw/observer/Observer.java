package it.polimi.ingsw.observer;

/**
 * Represent an object that can receive notifications from an Observable.
 *
 * @param <T> the type of the messages that this observer will be notified of
 */
public interface Observer<T> {
    /**
     * Processes the message.
     *
     * @param message the message that will be processed
     */
    void update(T message);
}