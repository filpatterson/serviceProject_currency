package FloatRatesCom;

import BnmRate.ExchangeRate;
import Http.HttpUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

public class ExchangeFloatRatesComHandler {
    private static final String PATH_TO_FLOAT_COM_RATE = "http://www.floatrates.com/daily/usd.xml";

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpUtility httpUtility = new HttpUtility();

        XmlMapper mapper = new XmlMapper();
        Channel valutes = mapper.readValue(httpUtility.sendGet(PATH_TO_FLOAT_COM_RATE),
                Channel.class);
        System.out.println(valutes);
    }
}
