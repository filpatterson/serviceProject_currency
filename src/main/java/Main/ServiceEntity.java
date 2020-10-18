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
        if(args.length == 7) {
            GlobalParameters.databaseHost = args[0];
            GlobalParameters.databasePort = Integer.parseInt(args[1]);
            GlobalParameters.databaseName = args[2];
            GlobalParameters.gatewayHost = args[3];
            GlobalParameters.gatewayPort = args[4];
            GlobalParameters.serviceHost = args[5];
            GlobalParameters.servicePort = args[6];
        }

        //  establish connection to DB
        HttpConvertContextHandler customServer = new HttpConvertContextHandler(
                new CurrencyTools(
                        GlobalParameters.databaseHost,
                        GlobalParameters.databasePort,
                        GlobalParameters.databaseName
                )
        );

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
        jsonRequestForConnection.put(
                "address",
                "http://" + GlobalParameters.serviceHost + ":" + GlobalParameters.servicePort + "/convert"
        );
        HttpUtility httpUtility = new HttpUtility();
        httpUtility.sendHandshakeJsonPost(
                "http://" + GlobalParameters.gatewayHost + ":" + GlobalParameters.gatewayPort + "/",
                jsonRequestForConnection.toString()
        );

        //  create and start server
        HttpServer server = HttpServer.create(
                new InetSocketAddress("localhost", Integer.parseInt(GlobalParameters.servicePort)), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        server.createContext("/convert", customServer);
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println(" Server started on port 8002");
    }
}
