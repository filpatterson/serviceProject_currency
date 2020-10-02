package FloatRatesCom;

import BnmRate.ExchangeRate;
import Http.HttpUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;

public class ExchangeFloatRatesComHandler {
    private static final String PATH_TO_FLOAT_COM_RATE = "http://www.floatrates.com/daily/usd.xml";

    public static void main(String[] args) throws IOException, InterruptedException {
        //  initialize utility for making http requests
        HttpUtility httpUtility = new HttpUtility();

        //  set xml mapper and read values from requested XML currency rates file
        XmlMapper mapper = new XmlMapper();
        Channel valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_FLOAT_COM_RATE),
                Channel.class);

        //  connect to mongodb, choose db and collection from db
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("myMongoDB");
        MongoCollection<Document> collection = database.getCollection("newFloatRates");

        //  make name as unique key in collection (throws error if is repeating)
        collection.createIndex(new Document("name", 1), new IndexOptions().unique(true));

        //  append all obtained information to the db
        for(int i = 0; i < valutes.getCurrencies().size(); i++) {
            CurrencyItem currentCurrency = valutes.getCurrencies().get(i);

            //  insert if new and update if already present element in DB
            collection.updateOne(Filters.eq("name", currentCurrency.getTargetName()),
                    new Document("$set", new Document().
                            append("link", currentCurrency.getLink()).
                            append("rate", currentCurrency.getExchangeRate())),
                    new UpdateOptions().upsert(true));

        }

        //  make a search request in DB, first find elements to iterate through and then adapt them to interpreter
        FindIterable<Document> cursor = collection.find(Filters.eq("name", "Euro"));
        MongoCursor<Document> mongoCursor = cursor.iterator();
        try {
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
        } finally {
            mongoCursor.close();
        }

        System.out.println(valutes);
    }
}
