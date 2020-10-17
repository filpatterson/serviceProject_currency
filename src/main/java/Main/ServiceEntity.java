package Main;

import Actor.Actor;
import Actor.Supervisor;
import BnmRateMd.BnmRatesHandler;
import FloatRatesCom.FloatRatesHandler;
import Http.HttpConvertContextHandler;
import Http.HttpUtility;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServiceEntity {
    public static void main(String[] args) throws IOException, Actor.DeadException {
        //  establish connection to DB
        HttpConvertContextHandler customServer = new HttpConvertContextHandler(new CurrencyTools("localhost", 27017, "myMongoDB"));

        //  create handlers for interacting with currency data
        BnmRatesHandler bnmRatesHandler = new BnmRatesHandler();
        FloatRatesHandler floatRatesHandler = new FloatRatesHandler();

        //  create actors for receiving currency data from sources and writing it to DB
        Supervisor.createActor("bnmRatesHandler", bnmRatesHandler.getBnmRatesGetterBehavior());
        Supervisor.createActor("floatRatesHandler", floatRatesHandler.getFloatRatesGetterBehavior());

        //  start actors
        Supervisor.sendMessage("bnmRatesHandler", new HttpUtility());
        Supervisor.sendMessage("floatRatesHandler", new HttpUtility());

        //  handshake connection of service with gateway
        JSONObject jsonRequestForConnection = new JSONObject();
        jsonRequestForConnection.put("functionName", "convert");
        jsonRequestForConnection.put("address", "http://localhost:8002/convert");
        HttpUtility httpUtility = new HttpUtility();
        httpUtility.sendHandshakeJsonPost("http://localhost:8003/", jsonRequestForConnection.toString());

        //  create and start server
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8002), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        server.createContext("/convert", customServer);
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println(" Server started on port 8002");
    }
}
