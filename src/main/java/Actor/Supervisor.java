package Actor;

import java.util.HashMap;

/**
 *  Class of singleton that manages all actors in system
 */
public class Supervisor {

    //  global storage of all available actors at moment
    private static final HashMap<String, Actor> actorStorage = new HashMap<>();

    //  amount of available actors for application
    static final short MAX_AMOUNT_OF_ACTORS = 10;

    //  constant for naming new assistant-actor
    public static String assistantNameConstant = " assistant";

    //  getters
    public static HashMap<String, Actor> getActorStorage() { return actorStorage; }

    //  setters
    public static void setCriticalMessageIndex(String nameOfActor, short criticalMessageIndex) {
        actorStorage.get(nameOfActor).setCriticalMessageIndex(criticalMessageIndex);
    }

    /**
     * remove dead actor from system and revive if it's master unit
     * @param nameOfActorToKill name of actor that performs suicide
     * @param isBehaviorRestoreRequired is required resurrection of actor (if it's master, then perform resurrection)
     */
    public static void actorSuicide(String nameOfActorToKill, boolean isBehaviorRestoreRequired) {
        if(isBehaviorRestoreRequired) {
            Behavior behaviorOfSuicide = actorStorage.get(nameOfActorToKill).getBehavior();
            actorStorage.remove(nameOfActorToKill);
            createActor(nameOfActorToKill, behaviorOfSuicide);
        } else
            actorStorage.remove(nameOfActorToKill);
    }

    /**
     * create actor and save him in system
     * @param name Name of actor to be created
     * @param behavior Actions that actor must perform using received messages
     */
    public static void createActor(String name, Behavior behavior) {
        Actor actor = new Actor(name, behavior);
        actorStorage.put(name, actor);
    }

    /**
     * try sending message to actor and check message to be received
     * @param nameOfReceiver name of actor that receives message
     * @param message info to receive
     * @return is message received or not
     * @throws Actor.DeadException inform if actor is already dead
     */
    public static boolean sendMessage(String nameOfReceiver, Object message) throws Actor.DeadException {
        Actor<Object> receiver = actorStorage.get(nameOfReceiver);

        if(receiver == null)
            System.out.println("Ehhh, actor was already removed from the system " + receiver);
        else
            if(!receiver.getMessage(message)) {
                System.out.println("Actor " + nameOfReceiver + " can't receive message");
                return false;
            }

        return true;
    }

    /**
     * create assistant for master unit or another assistant if there was request from actor
     * @param nameOfMasterActor name of actor that requested creation of assistant
     * @param behaviorOfMaster behavior of the master
     * @param masterCriticalValue critical amount of messages defined by master
     * @return boolean was assistant created or not
     */
    public static boolean createAssistant(String nameOfMasterActor, Behavior behaviorOfMaster, short masterCriticalValue) {
        //  check if amount of actors is too big for creating new assistants
        if(actorStorage.size() > Supervisor.MAX_AMOUNT_OF_ACTORS)
            return false;

        //  create new assistant
        String nameOfAssistant = nameOfMasterActor + assistantNameConstant;
        Actor actor = new Actor(nameOfAssistant, behaviorOfMaster);
        actor.setCriticalMessageIndex(masterCriticalValue);
        actor.isMaster = false;
        actorStorage.put(nameOfAssistant, actor);
        return true;
    }
}
