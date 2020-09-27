package Actor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Actor<Message> implements Runnable {

    //  actor's name
    private final String name;

    //  mailbox of incoming messages
    private final BlockingQueue<Message> queue;

    //  interface's unit, containing scenario of message processing
    private final Behavior<Message> behavior;

    //  is current actor's living required
    private boolean isThreadAlive;

    //  critical value of messages after which actor creates assistant to handle all incoming messages
    private short criticalMessageIndex = 1000;

    //  is created assistant for this actor
    private boolean isAssistantCreated = false;

    //  is current actor master or it is assistant
    boolean isMaster = true;

    @SuppressWarnings("serial")
    public static class DeadException extends Exception {}

    // getters
    public String getName() { return this.name; }
    Behavior<Message> getBehavior() { return this.behavior; }

    // setters
    void setCriticalMessageIndex (short criticalMessageIndex) { this.criticalMessageIndex = criticalMessageIndex; }

    /**
     * create new actor
     * @param nameOfActor name for new actor
     * @param behavior required scenario of handling incoming messages
     */
    Actor(String nameOfActor, Behavior<Message> behavior) {
        this.behavior = behavior;
        this.queue = new LinkedBlockingQueue<>();
        this.name = nameOfActor;
        isThreadAlive = true;
        new Thread(this).start();
    }

    /**
     * process all mailbox and check if destruction is required
     */
    public void run() {
        try {
            //  queue.take() - this is taking message from the queue for processing
            while (behavior.onReceive(this, queue.take()) && isThreadAlive) {
                //  destroy and stop work of assistant if it completed its work
                if(!isMaster && queue.size() == 0) {
                    suicide();
                    break;
                }
            }
        } catch (InterruptedException exception) {
            behavior.onException(this, exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * suicide actor and inform supervisor about its death
     */
    public void suicide() {
        isThreadAlive = false;
        this.queue.clear();
        try {
            Supervisor.actorSuicide(this.name, this.isMaster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * receive message and perform action basing on messages amount
     * @param message incoming message
     * @return is message received or not
     * @throws DeadException if there is error of dead actor
     */
    public boolean getMessage(Message message) throws DeadException {
        if(!isThreadAlive)
            throw new DeadException();

        short currentMailboxSize = (short) queue.size();

        //  check if there wasn't previously created assistant and if mailbox size is too big, then request for assistant
        if(!isAssistantCreated && (currentMailboxSize > criticalMessageIndex)) {
            if (Supervisor.createAssistant(this.name, this.behavior, this.criticalMessageIndex))
                isAssistantCreated = true;
        }

        // if there is assistant and mailbox is critically filled, then retransmit message to assistant
        if(isAssistantCreated && currentMailboxSize > criticalMessageIndex)
            if(!Supervisor.sendMessage(this.name + Supervisor.assistantNameConstant, message))
                isAssistantCreated = false;

        //  try to add message to the mailbox
        return queue.offer(message);
    }
}