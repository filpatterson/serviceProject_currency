package Http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class for making http requests
 */
public class HttpUtility {
    //  setting HttpClient that will make requests
    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * send simple GET request without header specifications and additional info
     * @param destinationPage page for requesting data from
     * @return data obtained as result of GET request to the page
     * @throws IOException error in I/O streams of application
     * @throws InterruptedException error in threading
     */
    public String sendGet(String destinationPage) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(destinationPage)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
