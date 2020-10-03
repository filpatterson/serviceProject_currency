package BnmRate;

import Actor.Actor;
import Actor.Behavior;
import Http.HttpUtility;
import MongoDB.MongoDbUtility;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BnmRatesHandler {
    //  link to National Bank of Moldova web-site
    private static final String PATH_TO_BNM_RATE = "https://www.bnm.md/ro/official_exchange_rates?get_xml=1&date=";

    //  date-time formatter for attaching required postfix to the link
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //  refresh rate
    private int updateRate;

    /**
     * Behavior to get information from BNM page, deserialize it and send to mongoDB
     */
    private Behavior<HttpUtility> bnmRatesGetterBehavior = new Behavior<HttpUtility>() {
        @Override
        public boolean onReceive(Actor<HttpUtility> self, HttpUtility msg) throws Exception {
            //  xml mapper for deserialization of incoming data
            XmlMapper xmlMapper = new XmlMapper();

            try {
                while (true) {
                    //  get current time
                    LocalDateTime currentDate = LocalDateTime.now();

                    //  get exchange rate based on current date, deserialize it
                    BnmExchangeRate exchangeRate = xmlMapper.readValue(msg.
                                    sendGet(PATH_TO_BNM_RATE + DATE_TIME_FORMATTER.format(currentDate).toString()),
                            BnmExchangeRate.class);

                    //  create local tool for interaction with database
                    MongoDbUtility databaseHandler = new MongoDbUtility();

                    //  establish connection with database and send obtained records to db
                    databaseHandler.establishConnectionToDB("localhost", 27017, "myMongoDB");
                    databaseHandler.upsertCurrencies(exchangeRate.getCurrencies());

                    //  wait for another refresh time
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

    //  standard getters, setters, constructors

    //  constructor with default value of refresh rate
    public BnmRatesHandler() {
        //  make update rate by default once per hour
        updateRate = 3600000;
    }

    /**
     * set new required refresh rate
     * @param updateRate time of refresh rate in milliseconds
     */
    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

    /**
     * get current update rate
     * @return update rate in milliseconds
     */
    public int getUpdateRate() {
        return updateRate;
    }

    /**
     * get behavior of actor for getting exchange rates from BNM
     * @return behavior for getting exchange rates from BNM
     */
    public Behavior<HttpUtility> getBnmRatesGetterBehavior() {
        return bnmRatesGetterBehavior;
    }
}
