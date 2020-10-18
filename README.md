# Service project: currency

This project contains exchange service that converts money from one currency to another one.
Exchange rates are taken from National Bank of Moldova and from open-source exchange rate
service. Links to exchange rates sources:
* https://www.bnm.md
* http://www.floatrates.com

Service is using actor multithreading system for getting information and writing it to MongoDB,
and standard Java http multithreading server with thread pool.

### Taking information from exchange rate sources

Service requests from services xml-formatted data containing exchange rates.

```java
    //  get current time
    LocalDateTime currentDate = LocalDateTime.now();

    //  get exchange rate based on current date, deserialize it
    BnmExchangeRate exchangeRate = xmlMapper.readValue(msg.
                    sendGet(PATH_TO_BNM_RATE + DATE_TIME_FORMATTER.format(currentDate).toString()),
            BnmExchangeRate.class);

    //  create local tool for interaction with database
    MongoDbUtility databaseHandler = new MongoDbUtility();

    //  establish connection with database and send obtained records to db
    databaseHandler.establishConnectionToDB("localhost", 27017, "myMongoDB");
    databaseHandler.upsertCurrencies(exchangeRate.getCurrencies());
```

### Writing information to DB
After getting information service deserializes incoming information from XML format and writes
received information into database. Considering that service updates rates every day program
must check if information must be either entered or updated. This was done using ```upsertCurrencies()```.

```java
    //  check list to be presented with FloatRatesCurrency class for correct handling
    if(currencies.get(0).getClass().equals(FloatCurrency.class)){
        establishConnectionToCollection("newFloatRate");
        for(int i = 0; i < size; i++) {
            //  get each element individually
            FloatCurrency currentCurrency = (FloatCurrency) currencies.get(i);

            //  insert if new and update if already present element in DB
            collection.updateOne(Filters.eq("name", currentCurrency.getTargetName()),
                    new Document("$set", new Document().
                            append("link", currentCurrency.getLink()).
                            append("value", currentCurrency.getExchangeRate())),
                    new UpdateOptions().upsert(true));
        }
    } else if (currencies.get(0).getClass().equals(BnmCurrency.class)) {
        establishConnectionToCollection("bnmRates");
        for(int i = 0; i < size; i++) {
            BnmCurrency currentCurrency = (BnmCurrency) currencies.get(i);

            collection.updateOne(Filters.eq("name", currentCurrency.getName()),
                    new Document("$set", new Document().
                            append("charCode", currentCurrency.getCharCode()).
                            append("value", currentCurrency.getValue())),
                    new UpdateOptions().upsert(true));
        }
    }
``` 

In the main class of application program creates required actors, appends desired behavior for
getting information and writing it to database, and activates them.

```java
    //  create handlers for interacting with currency data
    BnmRatesHandler bnmRatesHandler = new BnmRatesHandler();
    FloatRatesHandler floatRatesHandler = new FloatRatesHandler();

    //  create actors for receiving currency data from sources and writing it to DB
    Supervisor.createActor("bnmRatesHandler", bnmRatesHandler.getBnmRatesGetterBehavior());
    Supervisor.createActor("floatRatesHandler", floatRatesHandler.getFloatRatesGetterBehavior());

    //  start actors
    Supervisor.sendMessage("bnmRatesHandler", new HttpUtility());
    Supervisor.sendMessage("floatRatesHandler", new HttpUtility());
```

### Service operation

Service performs one big operation of convertation. Function called ```convert()``` waits for
4 arguments: amount of money that must be converted, from which currency money will be changed,
to which one currency money will be changed and from which of exchange rates sources take rates.

```java
        //  request currency values from DB
        ArrayList<Document> fromResult = mongoDbUtility.findElements(source, "name", fromCurrency);
        ArrayList<Document> toResult = mongoDbUtility.findElements(source, "name", toCurrency);

        if(fromResult.size() > 1 || toResult.size() > 1) {
            throw new Exception("there are too many elements with such name, try again");
        } else if (fromResult.size() == 0 || toResult.size() == 0){
            System.err.println("not found currency");
            return 0;
        } else if(source.equals("newFloatRates")) {
            double fromValue = Double.parseDouble((String) fromResult.get(0).get("rate"));
            double toValue = Double.parseDouble((String) toResult.get(0).get("rate"));

            return (amount * fromValue) / toValue;
        } else if(source.equals("bnmRates")) {
            double fromValue = (double) fromResult.get(0).get("value");
            double toValue = (double) toResult.get(0).get("value");

            return (amount * fromValue) / toValue;
        }
```

Considering amount of required arguments this function will be ideal for acting with multistage
request-response http command call.

### HTTP REST server

After all those operations application start http server that is working by REST principle
receiving requests and answering to them basing on appended context handler to this server.

First, program receives packet and checks which type of request it is. Depending on type of
request program chooses which handling function will be chosen.

```java
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
```

Second, after understanding which request was received, program gets payload from request and
performs deserialization from JSON formatted data (all requests must come with JSON-formatted
payload).

```java
    //  start deserialization of json payload
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode node = objectMapper.readValue(requestPayload, ObjectNode.class);

    System.out.println(requestPayload);

    //  start process with taken from JSON command name
    Process initializedProcess = new Process(node.get("functionName").asText());
    initializedProcess.getProcessArguments().put("amount", node.get("amount").asText());
    processStorage.put(initializedProcess.getId(), initializedProcess);

    //  form response JSON payload
    String response;
    response = objectMapper.writeValueAsString(initializedProcess);

    sendResponse(httpExchange, response);
```

Considering that principle of laboratory work was to make server that performs actions basing
on multiple requests and answering to them by next principle:
* POST request starts process with receiving first argument for function;
* PUT requests add required arguments to function basing on received from first request ID of
process;
* GET request finalizes process, requesting response with result of function. This can be done
only after receiving all required arguments.

For better understanding of how program operates with requests is shown function of handling PUT
request.

```java
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
```

If principles of getting, receiving and interacting with service is common for all services,
then reaction to those requests are unique for each service.

All requests have payloads except for GET request. The problem was to send such request to service
from client that service will understand from which process client is waiting for result. The only
way to do this was to send argument via url:

```java
httpUtility.sendJsonGet("http://localhost:8003/?id=" + id);
```

Program gets request containing url to which process is directed and decomposes it to get ID:

```java
    //  get from uri required process ID
    String requestedIndex = httpExchange.getRequestURI().toString().
            split("\\?")[1].
            split("=")[1];

    //  check if there is such id
    if(processStorage.containsKey(requestedIndex)) {
```

Server is started from main class via initializing threadPool:

```java
    //  create and start server
    HttpServer server = HttpServer.create(
            new InetSocketAddress("localhost", Integer.parseInt(GlobalParameters.servicePort)), 0);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    server.createContext("/convert", customServer);
    server.setExecutor(threadPoolExecutor);
    server.start();
    System.out.println(" Server started on port 8002");
```

### Handshaking with Gateway

Service must be registered at gateway for directing all requests to it. The problem is that gateway
does not know all possible services and their addresses. The only way to do this is via registering
service to gateway using handshake POST request with payload containing address for backward
communication and special headers that will inform gateway about the fact that this request is coming
from service.

```java
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
```

This is done via ```sendHandshakeJsonPost()```:

```java
    //  make POST request
    HttpPost request = new HttpPost(destinationPage);

    //  append headers to request
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-Type", "application/json");
    request.setHeader("Service-Call", "true");
    request.setEntity(entity);
```