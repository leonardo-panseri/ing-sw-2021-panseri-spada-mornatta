package it.polimi.ingsw;

/**
 * Represents a packet that can be processed.
 *
 * @param <T> the type of the controller responsible of processing the packet
 */
public interface IProcessablePacket<T> {
    /**
     * Processes the packet.
     *
     * @param controller the controller that will process the packet
     */
    void process(T controller);
}
