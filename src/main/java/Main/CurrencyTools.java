package Main;

import MongoDB.MongoDbUtility;
import org.bson.Document;

import java.util.ArrayList;

public class CurrencyTools {

    //  utility for connection with MongoDB
    private MongoDbUtility mongoDbUtility = new MongoDbUtility();

    public MongoDbUtility getMongoDbUtility() {
        return mongoDbUtility;
    }

    public void setMongoDbUtility(MongoDbUtility mongoDbUtility) {
        this.mongoDbUtility = mongoDbUtility;
    }

    public CurrencyTools(String host, int port, String databaseName){
        mongoDbUtility.establishConnectionToDB(host, port, databaseName);
    }

    /**
     * convert from one currency to another required amount from requested source
     * @param fromCurrency from which currency to convert
     * @param toCurrency to which convert currency
     * @param source from which source take rates
     * @param amount how much currency must be converted
     * @return result of conversion
     * @throws Exception error in work
     */
    public double convertCurrencies(String fromCurrency, String toCurrency, String source, Double amount) throws Exception {
        //  request currency values from DB
        ArrayList<Document> fromResult = mongoDbUtility.findElements(source, "name", fromCurrency);
        ArrayList<Document> toResult = mongoDbUtility.findElements(source, "name", toCurrency);

        if(fromResult.size() > 1 || toResult.size() > 1) {
            throw new Exception("there are too many elements with such name, try again");
        } else if (fromResult.size() == 0 || toResult.size() == 0){
            System.err.println("not fount currency");
            return 0;
        } else {
            double fromValue = Double.parseDouble((String) fromResult.get(0).get("rate"));
            double toValue = Double.parseDouble((String) toResult.get(0).get("rate"));

            return (amount * fromValue) / toValue;
        }
    }
}
