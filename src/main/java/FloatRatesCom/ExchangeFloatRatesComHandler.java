package FloatRatesCom;

import BnmRate.ExchangeRate;
import Http.HttpUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;

public class ExchangeFloatRatesComHandler {
    private static final String PATH_TO_FLOAT_COM_RATE = "http://www.floatrates.com/daily/usd.xml";

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpUtility httpUtility = new HttpUtility();

        XmlMapper mapper = new XmlMapper();
        Channel valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_FLOAT_COM_RATE),
                Channel.class);

        //  connect to mongodb
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("myMongoDB");
        MongoCollection<Document> collection = database.getCollection("FloatRatesCom");

        for(int i = 0; i < valutes.getCurrencies().size(); i++) {
            Document document = new Document();
            CurrencyItem currentCurrency = valutes.getCurrencies().get(i);
            document.append("name", currentCurrency.getTargetName() + valutes.getBaseCurrency());
            document.append("link", currentCurrency.getLink());
            document.append("rate", currentCurrency.getExchangeRate());
            collection.insertOne(document);
        }

        System.out.println(valutes);
    }
}
