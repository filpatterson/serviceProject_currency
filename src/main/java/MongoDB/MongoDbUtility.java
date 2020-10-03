package MongoDB;

import BnmRate.BnmCurrency;
import FloatRatesCom.FloatCurrency;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDbUtility {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    /**
     * establish connection to database
     * @param host host machine where database is located
     * @param port port where database is located
     * @param databaseName name of database
     */
    public void establishConnectionToDB(String host, int port, String databaseName) {
        client = new MongoClient(host, port);
        database = client.getDatabase(databaseName);
    }

    /**
     * establish connection to collection of database
     * @param collectionName name of collection to get
     * @return requested collection
     */
    public MongoCollection<Document> establishConnectionToCollection(String collectionName) {
        collection = database.getCollection(collectionName);
        return collection;
    }

    /**
     * set unique index attached to the field of collection not to repeat elements
     * @param uniqueFieldName name of field that must have unique elements
     */
    public void setUniqueIndex(String uniqueFieldName) {
        collection.createIndex(new Document(uniqueFieldName, 1), new IndexOptions().unique(true));
    }

    /**
     * update elements if are present or insert if there are no such records in db
     * @param currencies list of elements to insert/update in db
     */
    public void upsertCurrencies(List<?> currencies) throws Exception {
        int size = currencies.size();

        //  no need to operate if was sent empty list (throw error)
        if(size > 0) {
            //  check list to be presented with FloatRatesCurrency class for correct handling
            if(currencies.get(0).getClass().equals(FloatCurrency.class)){
                establishConnectionToCollection("newFloatRate");
                for(int i = 0; i < size; i++) {
                    //  get each element individually
                    FloatCurrency currentCurrency = (FloatCurrency) currencies.get(i);

                    //  insert if new and update if already present element in DB
                    collection.updateOne(Filters.eq("name", currentCurrency.getTargetName()),
                            new Document("$set", new Document().
                                    append("link", currentCurrency.getLink()).
                                    append("value", currentCurrency.getExchangeRate())),
                            new UpdateOptions().upsert(true));
                }
            } else if (currencies.get(0).getClass().equals(BnmCurrency.class)) {
                establishConnectionToCollection("bnmRates");
                for(int i = 0; i < size; i++) {
                    BnmCurrency currentCurrency = (BnmCurrency) currencies.get(i);

                    collection.updateOne(Filters.eq("name", currentCurrency.getName()),
                            new Document("$set", new Document().
                                    append("charCode", currentCurrency.getCharCode()).
                                    append("value", currentCurrency.getValue())),
                            new UpdateOptions().upsert(true));
                }
            } else {
                throw new Exception("there is no collection for such class");
            }
        } else {
            throw new Exception("empty list sent to upsert");
        }
    }

    /**
     * find list of records in database
     * @param collectionName name of collection where element must be located
     * @param fieldName name of field where search is required
     * @param value value of mentioned field that is requested
     * @return list of records with requested value in required field
     */
    public ArrayList<Document> findElements(String collectionName, String fieldName, String value) {
        establishConnectionToCollection(collectionName);
        //  find elements in db inside required field with requested value and create iterator to navigate through results
        FindIterable<Document> cursor = collection.find(Filters.eq(fieldName, value));
        MongoCursor<Document> mongoCursor = cursor.iterator();

        //  create array of documents to which will be attached all elements
        ArrayList<Document> listOfDocuments = new ArrayList<>();

        //  try appending elements to array
        try {
            while(mongoCursor.hasNext())
                listOfDocuments.add(mongoCursor.next());
        } finally {
            mongoCursor.close();
        }

        return listOfDocuments;
    }


}
