package Http;

import java.io.IOException;

public class CustomClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpUtility httpUtility = new HttpUtility();
        httpUtility.sendJsonPost("http://localhost:8001/test?name=sam");
    }
}
