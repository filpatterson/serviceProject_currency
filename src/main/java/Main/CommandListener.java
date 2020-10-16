package Main;

import org.bson.Document;

import java.util.ArrayList;

public class CommandListener {

    public void interpretCommand(String command, CurrencyTools currencyTools) throws Exception {
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

                System.out.println(currencyTools.convertCurrencies(firstArgument.toString().trim(), secondArgument.toString().trim(),
                        splitedCommand[lastIndex], Double.parseDouble(splitedCommand[1])));

            case ("find"):
                if(splitedCommand[2].equals("from")) {
                    ArrayList<Document> result = currencyTools.getMongoDbUtility().
                            findElements(splitedCommand[3], "name", splitedCommand[1]);
                    System.out.println(result);
                }
        }
    }
}
