package FloatRatesCom;

import Actor.Actor;
import Actor.Behavior;
import Http.HttpUtility;
import MongoDB.MongoDbUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class FloatRatesHandler {
    private static final String PATH_TO_FLOAT_COM_RATE = "http://www.floatrates.com/daily/usd.xml";
    private int updateRate;

    private Behavior<HttpUtility> floatRatesGetterBehavior = new Behavior<HttpUtility>() {
        @Override
        public boolean onReceive(Actor<HttpUtility> self, HttpUtility httpUtility) throws Exception {
            XmlMapper xmlMapper = new XmlMapper();

            try {
                while (true) {
                    //  get exchange rate
                    FloatExchangeRate exchangeRate = xmlMapper.readValue(httpUtility.sendGet(PATH_TO_FLOAT_COM_RATE), FloatExchangeRate.class);
                    //  create local tool for interaction with database
                    MongoDbUtility databaseHandler = new MongoDbUtility();

                    //  establish connection with database and send obtained records to db
                    databaseHandler.establishConnectionToDB("localhost", 27017, "myMongoDB");
                    databaseHandler.upsertCurrencies(exchangeRate.getCurrencies());

                    Thread.sleep(updateRate);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        public void onException(Actor<HttpUtility> self, Exception e) {
            e.printStackTrace();
        }
    };

    public FloatRatesHandler() {
        //  make update rate by default once per hour
        updateRate = 3600000;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

    public Behavior<HttpUtility> getFloatRatesGetterBehavior() {
        return floatRatesGetterBehavior;
    }
}
