package MongoDB;

import Actor.Actor;
import Actor.Behavior;
import Actor.Supervisor;

import java.util.List;

public class DatabaseRecorder {
    private MongoDbUtility mongoDbUtility;

    DatabaseRecorder(String host, int port, String databaseName) {
        MongoDbUtility databaseHandler = new MongoDbUtility();
        databaseHandler.establishConnectionToDB(host, port, databaseName);
    }

    private Behavior<List<?>> recordingInformationToDbBehavior = new Behavior<List<?>>() {
        @Override
        public boolean onReceive(Actor<List<?>> self, List<?> msg) throws Exception {
            mongoDbUtility.upsertCurrencies(msg);

            return false;
        }

        @Override
        public void onException(Actor<List<?>> self, Exception e) {
            e.printStackTrace();
        }
    };

    public void startDatabaseRecorder() {
        Supervisor.createActor("databaseRecorder", recordingInformationToDbBehavior);
    }
}
