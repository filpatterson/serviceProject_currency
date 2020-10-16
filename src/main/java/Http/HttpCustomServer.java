package Http;

import Http.Unit.Process;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpCustomServer implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestBody;
        if("POST".equals(httpExchange.getRequestMethod())) {
            if ((requestBody = handlePostRequest(httpExchange)) == null) {
                System.err.println("Couldn't take content of POST request");
                return;
            }
            handlePostResponse(httpExchange, requestBody);
        }
//        } else if("PUT".equals(httpExchange.getRequestMethod())) {
//            if((requestBody = handlePutRequest(httpExchange)) == null) {
//                System.err.println("Couldn't take content of PUT request");
//                return;
//            }
//            System.out.println(requestBody);
//            handlePutResponse(httpExchange, requestBody);
//        } else if("GET".equals(httpExchange.getRequestMethod())) {
//            if((requestBody = handleGetRequest(httpExchange)) == null) {
//                System.err.println("Couldn't take content of PUT request");
//            }
//        }
//        handleResponse(httpExchange,requestParamValue);
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
        //  check that there is specified content type of request
        if(httpExchange.getRequestHeaders().containsKey("Content-Type")) {
            //  check content to be equal to json formatted data
            if(httpExchange.getRequestHeaders().get("Content-Type").get(0).equals("application/json")){
                //  open stream for getting UTF-8 formatted characters
                InputStreamReader inputStreamReader = new InputStreamReader(
                        httpExchange.getRequestBody(),
                        StandardCharsets.UTF_8);

                //  insert stream to buffer for reading through input stream
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //  initialize current character and buffer for appending all incoming characters
                int currentCharacter;
                StringBuilder buffer = new StringBuilder(512);

                //  while it's not the end of all stream, read char-by-char all incoming data
                while((currentCharacter = bufferedReader.read()) != -1) {
                    buffer.append((char) currentCharacter);
                }

                //  close buffer and input stream
                bufferedReader.close();
                inputStreamReader.close();

                //  return string-formatted data
                return buffer.toString();
            } else {
                System.err.println("Unknown content-type");
            }
        } else {
            System.err.println("No content-type specified");
        }
        return null;
    }

    private void handlePostResponse(HttpExchange httpExchange, String requestBody)  throws  IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(requestBody, ObjectNode.class);

        if(node.has("functionName")) {
            Process initializedProcess = new Process(node.get("functionName").asText());

            String jsonCodedString = null;
            jsonCodedString = objectMapper.writeValueAsString(initializedProcess);

            // this line is a must
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, jsonCodedString.length());
            System.out.println(jsonCodedString);
            System.out.println(httpExchange.getResponseHeaders().keySet());
            System.out.println(httpExchange.getResponseHeaders().entrySet());
            System.out.println(httpExchange.getHttpContext().getPath());
            System.out.println(httpExchange.getResponseBody());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(jsonCodedString.getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            System.err.println("There is no specification of function");
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.createContext("/test", new HttpCustomServer());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println(" Server started on port 8001");
    }
}