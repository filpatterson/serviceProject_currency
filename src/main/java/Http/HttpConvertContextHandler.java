package Http;

import Http.Unit.Process;
import Main.CurrencyTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpConvertContextHandler implements HttpHandler {
    HashMap<String, Process> processStorage = new HashMap<>();
    CurrencyTools currencyTools = null;

    public HttpConvertContextHandler(CurrencyTools currencyTools) {
        this.currencyTools = currencyTools;
    }

    public CurrencyTools getCurrencyTools() {
        return currencyTools;
    }

    /**
     * handle for all incoming requests
     * @param httpExchange REST service connector
     */
    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            //  check if this is GET request
            if("GET".equals(httpExchange.getRequestMethod())) {
                handleGetResponse(httpExchange);
                return;
            }

            //  otherwise, request must have payload in its body
            String requestBody;
            //  try to get payload from request body
            if ((requestBody = getRequestPayload(httpExchange)) == null) {
                System.err.println("Couldn't take content of request");
                return;
            }

            //  handle request basing on type of request
            if("POST".equals(httpExchange.getRequestMethod())) {
                handlePostResponse(httpExchange, requestBody);
            } else if("PUT".equals(httpExchange.getRequestMethod())) {
                handlePutResponse(httpExchange, requestBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get payload from incoming request
     * @param httpExchange REST service connector
     * @return string object containing payload of incoming request
     * @throws IOException error in process of reading request
     */
    private String getRequestPayload(HttpExchange httpExchange) throws IOException {
        //  check that there is specified content type of request
        if(httpExchange.getRequestHeaders().containsKey("Content-Type")) {
            //  check content to be equal to json formatted data
            if(httpExchange.getRequestHeaders().get("Content-Type").get(0).equals("application/json")){
                //  open stream for getting UTF-8 formatted characters
                InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);

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

    /**
     * generate response to POST request and send it back to client
     * @param httpExchange REST service connector
     * @param requestPayload payload of received request
     * @throws IOException i/o exception
     */
    private void handlePostResponse(HttpExchange httpExchange, String requestPayload)  throws  IOException {
        //  start deserialization of json payload
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(requestPayload, ObjectNode.class);

        System.out.println(requestPayload);

        //  check this command to be equal to the broadcast trigger
        if(httpExchange.getRequestHeaders().containsKey("Service-Call")) {
            if(httpExchange.getRequestHeaders().get("Service-Call").get(0).equals("broadcast")) {
                System.out.println("received broadcast: " + node);
                sendResponse(httpExchange, "well, it is broadcast");
                return;
            }
        }

        //  start process with taken from JSON command name
        Process initializedProcess = new Process(node.get("functionName").asText());
        initializedProcess.getProcessArguments().put("amount", node.get("amount").asText());
        processStorage.put(initializedProcess.getId(), initializedProcess);

        //  form response JSON payload
        String response;
        response = objectMapper.writeValueAsString(initializedProcess);

        sendResponse(httpExchange, response);
    }

    /**
     * generate response to PUT request and send it back to client
     * @param httpExchange REST service connector
     * @param requestPayload payload of received request
     * @throws IOException i/o exception
     */
    private void handlePutResponse(HttpExchange httpExchange, String requestPayload) throws IOException {
        //  start deserialization of json payload
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.readValue(requestPayload, ObjectNode.class);

        //  check if such process exists in storage
        if(processStorage.containsKey(node.get("id").asText())) {
            //  get existing process from storage
            Process requestedProcess = processStorage.get(node.get("id").asText());

            //  set execution process status
            requestedProcess.setStatus("building");

            //  perform actions depending on payload fields
            if(node.has("firstCurrency")) {
                requestedProcess.getProcessArguments().put("firstCurrency", node.get("firstCurrency").asText());
            } else if (node.has("secondCurrency")) {
                requestedProcess.getProcessArguments().put("secondCurrency", node.get("secondCurrency").asText());
            } else if (node.has("source")) {
                requestedProcess.getProcessArguments().put("source", node.get("source").asText());
            } else if (node.has("finalize")) {
                requestedProcess.setStatus("processing");
            }

            //  make json-formatted string
            String response;
            response = objectMapper.writeValueAsString(requestedProcess);

            sendResponse(httpExchange, response);
        } else {
            System.err.println("there is no such process");
        }
    }

    /**
     * generate response to GET request and send it back to client
     * @param httpExchange REST service connector
     * @throws Exception i/o exception
     */
    private void handleGetResponse(HttpExchange httpExchange) throws Exception {
        //  get from uri required process ID
        String requestedIndex = httpExchange.getRequestURI().toString().
                split("\\?")[1].
                split("=")[1];

        //  check if there is such id
        if(processStorage.containsKey(requestedIndex)) {
            //  get requested id
            Process requestedProcess = processStorage.get(requestedIndex);

            //  if process is not ready for transmission then inform client about it
            if(!requestedProcess.getStatus().equals("processing")){
                System.err.println("process has not received all required arguments");

                //  inform client that process is unfinished
                String response = "{\"response\":undone}";
                sendResponse(httpExchange, response);
                return;
            }

            //  set status of process to "done"
            requestedProcess.setStatus("done");

            //  get all arguments of process
            HashMap<String, String> arguments = requestedProcess.getProcessArguments();

            //  calculate result
            double result = currencyTools.convertCurrencies(arguments.get("firstCurrency"),
                    arguments.get("secondCurrency"),
                    arguments.get("source"),
                    Double.parseDouble(arguments.get("amount")));

            //  give response to client
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("response", result);
            jsonResponse.put("id", requestedIndex);

            sendResponse(httpExchange, jsonResponse.toString());

            //  remove process from storage
            processStorage.remove(requestedIndex);
        } else {
            System.err.println("there is no such process");
            System.out.println(processStorage.keySet());
            System.out.println(requestedIndex);
        }
    }

    /**
     * send response to client
     * @param httpExchange REST service connector
     * @param response generated response for client
     * @throws IOException i/o exception
     */
    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        //  set headers of response
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length());
        System.out.println(response);
        System.out.println(httpExchange.getRemoteAddress());

        //  send response to the client
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}