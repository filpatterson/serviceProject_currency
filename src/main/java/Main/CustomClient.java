package Main;

import Http.HttpUtility;

import java.io.IOException;

public class CustomClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        for(int i = 0; i < 1000 ; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println(finalI);
                HttpUtility httpUtility = new HttpUtility();
                try {
                    httpUtility.sendJsonPost("http://localhost:8003/", "convert", "100" + finalI);
                    httpUtility.sendJsonPut("http://localhost:8003/", (long) finalI, "firstCurrency", "Euro");
                    httpUtility.sendJsonPut("http://localhost:8003/", (long) finalI, "secondCurrency", "Australian Dollar");
                    httpUtility.sendJsonPut("http://localhost:8003/", (long) finalI, "source", "newFloatRates");
                    httpUtility.sendJsonPut("http://localhost:8003/", (long) finalI, "finalize", "true");
                    httpUtility.sendJsonGet("http://localhost:8003/?id=" + finalI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
}
