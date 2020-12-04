package Http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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

    /**
     * send request to establish connection with gateway
     * @param destinationPage address of request
     * @param jsonRequest json request
     * @throws IOException i/o error
     */
    public void sendHandshakeJsonPost(String destinationPage, String jsonRequest) throws IOException {

        //  append json content type property
        StringEntity entity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

        //  initialize client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //  make POST request
        HttpPost request = new HttpPost(destinationPage);

        //  append headers to request
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Service-Call", "true");
        request.setEntity(entity);

        //  wait for response and handle it as String
        CloseableHttpResponse response = httpClient.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String textResponse = handler.handleResponse(response);
        System.out.println(textResponse);
    }

    /**
     * send broadcast to all services available for gateway
     * @param destinationPage where to send
     * @param jsonRequest what to send
     * @throws IOException
     */
    public void sendServiceCall(String destinationPage, String jsonRequest) throws IOException {
        //  append json content type property
        StringEntity entity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

        //  initialize client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //  make POST request
        HttpPost request = new HttpPost(destinationPage);

        //  append headers to request
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Service-Call", "broadcast:all");
        request.setEntity(entity);

        //  wait for response and handle it as String
        CloseableHttpResponse response = httpClient.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String textResponse = handler.handleResponse(response);
        System.out.println(textResponse);
    }

    /**
     * send broadcast to services that are attached to the required command
     * @param destinationPage where to send
     * @param jsonRequest what to send
     * @param commandRequired command services of which are required
     * @throws IOException
     */
    public void sendServiceCallToServicesWithCommand(String destinationPage, String jsonRequest, String commandRequired) throws IOException {
        //  append json content type property
        StringEntity entity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

        //  initialize client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //  make POST request
        HttpPost request = new HttpPost(destinationPage);

        //  append headers to request
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Service-Call", "broadcast:" + commandRequired);
        request.setEntity(entity);

        //  wait for response and handle it as String
        CloseableHttpResponse response = httpClient.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String textResponse = handler.handleResponse(response);
        System.out.println(textResponse);
    }

    /**
     * send json POST request that will initialize process on service
     * @param destinationPage where to send request
     * @param functionName json identifier of method
     * @param firstArgument value appended to identifier
     * @throws IOException i/o error
     */
    public String sendJsonPost(String destinationPage, String functionName, String firstArgument) throws IOException {
        //  generate payload for request based on parameters
        String payload = "{\"functionName\":\"" + functionName +"\",\"amount\":"+ firstArgument +"}";

        //  append json content type property
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_JSON);

        // initialize client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //  make PUT request
        HttpPost request = new HttpPost(destinationPage);

        //  append headers to request
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        //  wait for response and handle it as String
        CloseableHttpResponse response = httpClient.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String textResponse = handler.handleResponse(response);
        System.out.println(textResponse);


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(textResponse, ObjectNode.class);

        return node.get("id").asText();
    }

    /**
     * send json PUT request that will continue work with process
     * @param destinationPage where is directed request
     * @param id id of request to continue
     * @param nameOfArgument key of argument
     * @param argumentValue value of argument
     * @throws IOException i/o exception
     */
    public String sendJsonPut(String destinationPage, String id, String nameOfArgument, String argumentValue) throws IOException {
        //  generate payload for request based on parameters
        String payload = "{\"id\":" + id + ",\"" + nameOfArgument + "\":\"" + argumentValue + "\"}";

        //  append content type propety to payload
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_JSON);

        //  initialize client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //  generate PUT request
        HttpPut request = new HttpPut(destinationPage);

        //  append headers to request
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        //  wait for response and handle it as String
        CloseableHttpResponse response = httpClient.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String textResponse = handler.handleResponse(response);
        System.out.println(textResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(textResponse, ObjectNode.class);

        return node.get("id").asText();
    }

    /**
     * send GET request that will finish work of process and get result
     * @param destinationPageWithId where is directed request and ID of process to close and get
     * @throws IOException i/o error
     */
    public void sendJsonGet(String destinationPageWithId) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(destinationPageWithId);

        CloseableHttpResponse response = httpClient.execute(request);
        ResponseHandler<String> handler = new BasicResponseHandler();
        System.out.println(handler.handleResponse(response));
    }
}
