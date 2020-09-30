package BnmRate;

import Http.HttpUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class ExchangeBnmRateHandler {
    private static final String PATH_TO_BNM_RATE = "https://www.bnm.md/ru/official_exchange_rates?get_xml=1&date=";
    private static final String CURRENT_DATE = "29.09.2020";

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpUtility httpUtility = new HttpUtility();

        XmlMapper mapper = new XmlMapper();
        ExchangeRate valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_BNM_RATE + CURRENT_DATE),
                ExchangeRate.class);
        System.out.println(valutes);
    }
}