package BnmRate;

import Http.HttpUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class XMLFormatter {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpUtility requestForExchangeRate = new HttpUtility();

        XmlMapper mapper = new XmlMapper();
        ExchangeRate valutes = mapper.readValue(requestForExchangeRate.
                sendGet("https://www.bnm.md/ru/official_exchange_rates?get_xml=1&date=29.09.2020"),
                ExchangeRate.class);
        System.out.println(valutes);
    }
}