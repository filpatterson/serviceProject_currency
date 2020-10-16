package Http;

import java.io.IOException;

public class CustomClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpUtility httpUtility = new HttpUtility();
        httpUtility.sendJsonPost("http://localhost:8001/test", "convert", "100");
        httpUtility.sendJsonPut("http://localhost:8001/test", (long) 0, "firstCurrency", "Euro");
        httpUtility.sendJsonPut("http://localhost:8001/test", (long) 0, "secondCurrency", "Australian Dollar");
        httpUtility.sendJsonPut("http://localhost:8001/test", (long) 0, "source", "floatRatesCom");
        httpUtility.sendJsonPut("http://localhost:8001/test", (long) 0, "finalize", "true");
        httpUtility.sendJsonGet("http://localhost:8001/test?id=0");
    }
}
