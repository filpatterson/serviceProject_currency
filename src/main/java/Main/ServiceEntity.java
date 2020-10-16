package Main;

import Actor.Actor;
import Actor.Supervisor;
import BnmRate.BnmRatesHandler;
import FloatRatesCom.FloatRatesHandler;
import Http.HttpCustomServer;
import Http.HttpUtility;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServiceEntity {
    public static void main(String[] args) throws IOException, Actor.DeadException {
        HttpCustomServer customServer = new HttpCustomServer(new CurrencyTools("localhost", 27017, "myMongoDB"));

        BnmRatesHandler bnmRatesHandler = new BnmRatesHandler();
        FloatRatesHandler floatRatesHandler = new FloatRatesHandler();

        Supervisor.createActor("bnmRatesHandler", bnmRatesHandler.getBnmRatesGetterBehavior());
        Supervisor.createActor("floatRatesHandler", floatRatesHandler.getFloatRatesGetterBehavior());

        Supervisor.sendMessage("bnmRatesHandler", new HttpUtility());
        Supervisor.sendMessage("floatRatesHandler", new HttpUtility());

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8002), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        server.createContext("/test", customServer);
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println(" Server started on port 8002");
    }
}
