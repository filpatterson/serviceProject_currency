package Main;

import Actor.Supervisor;
import BnmRate.BnmRatesHandler;
import FloatRatesCom.FloatRatesHandler;
import Http.HttpUtility;

import java.util.Scanner;

public class CommandListener {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        CommandInterpreter commandInterpreter = new CommandInterpreter("localhost", 27017, "myMongoDB");

        BnmRatesHandler bnmRatesHandler = new BnmRatesHandler();
        FloatRatesHandler floatRatesHandler = new FloatRatesHandler();

        Supervisor.createActor("bnmRatesHandler", bnmRatesHandler.getBnmRatesGetterBehavior());
        Supervisor.createActor("floatRatesHandler", floatRatesHandler.getFloatRatesGetterBehavior());

        Supervisor.sendMessage("bnmRatesHandler", new HttpUtility());
        Supervisor.sendMessage("floatRatesHandler", new HttpUtility());

        while(true) {
            System.out.println("enter your command: ");
            commandInterpreter.interpretCommand(scanner.nextLine());
        }
    }
}
