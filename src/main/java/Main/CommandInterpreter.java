package Main;

import MongoDB.MongoDbUtility;
import org.bson.Document;

import java.util.ArrayList;

public class CommandInterpreter {
    private MongoDbUtility mongoDbUtility = new MongoDbUtility();

    CommandInterpreter(String host, int port, String databaseName){
        mongoDbUtility.establishConnectionToDB(host, port, databaseName);
    }

    public void interpretCommand(String command) throws Exception {
        String[] splitedCommand = command.split("[ ]+");
        switch(splitedCommand[0]){
            case("convert"):
                if(splitedCommand[3].equals("to") && splitedCommand[6].equals("from")){
                    System.out.println(convertCurrencies(splitedCommand[2], splitedCommand[4] + " " + splitedCommand[5], splitedCommand[7], Double.parseDouble(splitedCommand[1])));
                };
            case ("find"):
                if(splitedCommand[2].equals("from")) {
                    ArrayList<Document> result = mongoDbUtility.findElements(splitedCommand[3], "name", splitedCommand[1]);
                    System.out.println(result);
                }
        }
    }

    private double convertCurrencies(String fromCurrency, String toCurrency, String source, Double amount) throws Exception {
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
