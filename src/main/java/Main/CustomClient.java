package Main;

import Http.HttpUtility;

import java.io.IOException;

public class CustomClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        for(int i = 0; i < 100 ; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println(finalI);
                HttpUtility httpUtility = new HttpUtility();
                try {
                    String id = httpUtility.sendJsonPost("http://localhost:8003/", "convert", "100" + finalI);
                    httpUtility.sendJsonPut("http://localhost:8003/", id, "firstCurrency", "Euro");
                    httpUtility.sendJsonPut("http://localhost:8003/", id, "secondCurrency", "Dolar S.U.A.");
                    httpUtility.sendJsonPut("http://localhost:8003/", id, "source", "bnmRates");
                    httpUtility.sendJsonPut("http://localhost:8003/", id, "finalize", "true");
                    httpUtility.sendJsonGet("http://localhost:8003/?id=" + id);


//                    httpUtility.sendJsonPost("http://localhost:8002/convert", "convert", "100" + finalI);
//                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "firstCurrency", "Euro");
//                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "secondCurrency", "Dolar S.U.A.");
//                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "source", "bnmRates");
//                    httpUtility.sendJsonPut("http://localhost:8002/convert", (long) finalI, "finalize", "true");
//                    httpUtility.sendJsonGet("http://localhost:8002/convert?id=" + finalI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
}
