package Main;

import Http.HttpUtility;
import org.json.JSONObject;

import java.io.IOException;

public class CustomClient {
    public static void main(String[] args) throws IOException, InterruptedException {
//        for(int i = 0; i < 100 ; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                System.out.println(finalI);
//                HttpUtility httpUtility = new HttpUtility();
//                try {
//                    //  perform basic operations over service
//                    String id = httpUtility.sendJsonPost("http://localhost:8003/", "convert", "100" + finalI);
//                    httpUtility.sendJsonPut("http://localhost:8003/", id, "firstCurrency", "Euro");
//                    httpUtility.sendJsonPut("http://localhost:8003/", id, "secondCurrency", "Dolar S.U.A.");
//                    httpUtility.sendJsonPut("http://localhost:8003/", id, "source", "bnmRates");
//                    httpUtility.sendJsonPut("http://localhost:8003/", id, "finalize", "true");
//                    httpUtility.sendJsonGet("http://localhost:8003/?id=" + id);
//
//                    //  try to get cached response
//                    httpUtility.sendJsonGet("http://localhost:8003/?id=" + id);
//
//
////                    httpUtility.sendJsonPost("http://localhost:8002/convert", "convert", "100" + finalI);
////                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "firstCurrency", "Euro");
////                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "secondCurrency", "Dolar S.U.A.");
////                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "source", "bnmRates");
////                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "finalize", "true");
////                    httpUtility.sendJsonGet("http://localhost:8002/convert?id=" + finalI);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//        }

        JSONObject jsonRequestForConnection = new JSONObject();
        jsonRequestForConnection.put("functionName", "convert");
        jsonRequestForConnection.put(
                "address",
                "http://" + GlobalParameters.serviceHost + ":" + GlobalParameters.servicePort + "/convert"
        );
        HttpUtility httpUtility = new HttpUtility();
        httpUtility.sendServiceCallToServicesWithCommand(
                "http://" + GlobalParameters.gatewayHost + ":" + GlobalParameters.gatewayPort + "/",
                jsonRequestForConnection.toString(), "convert"
        );
    }
}
