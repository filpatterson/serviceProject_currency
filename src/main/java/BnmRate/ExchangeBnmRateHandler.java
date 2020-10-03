package BnmRate;

import Http.HttpUtility;
import MongoDB.MongoDbUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExchangeBnmRateHandler {
    private static final String PATH_TO_BNM_RATE = "https://www.bnm.md/ro/official_exchange_rates?get_xml=1&date=";

    public static void main(String[] args) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime currentDate = LocalDateTime.now();

        HttpUtility httpUtility = new HttpUtility();

        XmlMapper mapper = new XmlMapper();
        BnmExchangeRate valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_BNM_RATE + dateTimeFormatter.format(currentDate).toString()),
                BnmExchangeRate.class);

        System.out.println(dateTimeFormatter.format(currentDate).toString());

        MongoDbUtility databaseHandler = new MongoDbUtility();

        databaseHandler.establishConnectionToDB("localhost", 27017, "myMongoDB");
        databaseHandler.upsertCurrencies(valutes.getCurrencies());

        System.out.println(databaseHandler.findElements("bnmRates", "name", "Euro"));
        System.out.println(valutes);
    }
}