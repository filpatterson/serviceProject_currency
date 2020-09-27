package Actor;

/**
 * Interface of functions that require override by programmer
 * @param <Message> Type of data that is handled
 */
public interface Behavior<Message> {
    /**
     * @param self Self reference
     * @param msg Message that is sent to the actor
     * @return false - stop the actor; true - continue work;
     */
    boolean onReceive(Actor<Message> self, Message msg) throws Exception;

    /**
     * Exception that informs about death of the thread
     * @param self Self reference
     * @param e Exception that must be handled or thrown
     */
    void onException(Actor<Message> self, Exception e);
}
