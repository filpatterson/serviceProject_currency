package BnmRate;

import Http.HttpUtility;
import MongoDB.MongoDbHandler;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class ExchangeBnmRateHandler {
    private static final String PATH_TO_BNM_RATE = "https://www.bnm.md/ro/official_exchange_rates?get_xml=1&date=";
    private static final String CURRENT_DATE = "02.10.2020";

    public static void main(String[] args) throws Exception {
        HttpUtility httpUtility = new HttpUtility();

        XmlMapper mapper = new XmlMapper();
        ExchangeRate valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_BNM_RATE + CURRENT_DATE),
                ExchangeRate.class);

        MongoDbHandler databaseHandler = new MongoDbHandler();

        databaseHandler.establishConnectionToDB("localhost", 27017, "myMongoDB");
        databaseHandler.upsertCurrencies(valutes.getCurrencies());

        System.out.println(databaseHandler.findElements("bnmRates", "name", "Euro"));
        System.out.println(valutes);
    }
}