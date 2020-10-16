package Main;

import MongoDB.MongoDbUtility;
import org.bson.Document;

import java.util.ArrayList;

public class CurrencyTools {
    private MongoDbUtility mongoDbUtility = new MongoDbUtility();

    public CurrencyTools(String host, int port, String databaseName){
        mongoDbUtility.establishConnectionToDB(host, port, databaseName);
    }

    public void interpretCommand(String command) throws Exception {
        String[] splitedCommand = command.split("[ ]+");
        switch(splitedCommand[0]){
            case("convert"):
                byte lastIndex = (byte) (splitedCommand.length - 1);
                byte sizeOfFirstArgument = 0;
                byte sizeOfSecondArgument = 0;
                StringBuilder firstArgument = new StringBuilder();
                StringBuilder secondArgument = new StringBuilder();

                for(byte i = 2; i < splitedCommand.length; i++) {
                    if (splitedCommand[i].equals("to"))
                        sizeOfFirstArgument = i;
                    else if (splitedCommand[i].equals("from"))
                        sizeOfSecondArgument = i;
                }

                for(byte i = 2; i < sizeOfFirstArgument; i++) {
                    firstArgument.append(" ").append(splitedCommand[i]);
                }

                for(byte i = (byte) (sizeOfFirstArgument + 1); i < sizeOfSecondArgument; i++) {
                    secondArgument.append(" ").append(splitedCommand[i]);
                }

                System.out.println(convertCurrencies(firstArgument.toString().trim(), secondArgument.toString().trim(),
                        splitedCommand[lastIndex], Double.parseDouble(splitedCommand[1])));

            case ("find"):
                if(splitedCommand[2].equals("from")) {
                    ArrayList<Document> result = mongoDbUtility.findElements(splitedCommand[3], "name", splitedCommand[1]);
                    System.out.println(result);
                }
        }
    }

    public double convertCurrencies(String fromCurrency, String toCurrency, String source, Double amount) throws Exception {
        ArrayList<Document> fromResult = mongoDbUtility.findElements(source, "name", fromCurrency);
        ArrayList<Document> toResult = mongoDbUtility.findElements(source, "name", toCurrency);

        if(fromResult.size() > 1 || toResult.size() > 1) {
            throw new Exception("there are too many elements with such name, try again");
        } else {
            double fromValue = Double.parseDouble((String) fromResult.get(0).get("value"));
            double toValue = Double.parseDouble((String) toResult.get(0).get("value"));

            return (amount * fromValue) / toValue;
        }
    }
}
