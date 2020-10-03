package FloatRatesCom;

import Http.HttpUtility;
import MongoDB.MongoDbUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ExchangeFloatRatesComHandler {
    private static final String PATH_TO_FLOAT_COM_RATE = "http://www.floatrates.com/daily/usd.xml";

    public static void main(String[] args) throws Exception {
        //  initialize utility for making http requests
        HttpUtility httpUtility = new HttpUtility();

        //  set xml mapper and read values from requested XML currency rates file
        XmlMapper mapper = new XmlMapper();
        FloatExchangeRate valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_FLOAT_COM_RATE),
                FloatExchangeRate.class);

        //  connect to mongodb, choose db and collection from db
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase database = mongoClient.getDatabase("myMongoDB");
//        MongoCollection<Document> collection = database.getCollection("newFloatRates");
        MongoDbUtility databaseHandler = new MongoDbUtility();
        databaseHandler.establishConnectionToDB("localhost", 27017, "myMongoDB");
        databaseHandler.establishConnectionToCollection("newFloatRates");
        databaseHandler.setUniqueIndex("name");

//        //  make name as unique key in collection (throws error if is repeating)
//        collection.createIndex(new Document("name", 1), new IndexOptions().unique(true));

        System.out.println(valutes.getCurrencies().get(0).getClass());

        //  append all obtained information to the db
//        for(int i = 0; i < valutes.getCurrencies().size(); i++) {
//            CurrencyItem currentCurrency = valutes.getCurrencies().get(i);
//
//            //  insert if new and update if already present element in DB
//            collection.updateOne(Filters.eq("name", currentCurrency.getTargetName()),
//                    new Document("$set", new Document().
//                            append("link", currentCurrency.getLink()).
//                            append("rate", currentCurrency.getExchangeRate())),
//                    new UpdateOptions().upsert(true));
//
//        }
        databaseHandler.upsertCurrencies(valutes.getCurrencies());

        //  make a search request in DB, first find elements to iterate through and then adapt them to interpreter
        System.out.println(databaseHandler.findElements("newFloatRates", "name", "Euro"));
        System.out.println(valutes);
    }
}
