package SSE;

import java.io.EOFException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SseClient {
    //  destination from which data must be read
    private final URL destination;

    //  interface unit, requires override
    private final EventListener listener;

    /**
     * client constructor, supports lambda for listener
     * @param destination Port/address of connection
     * @param listener Listener of the events coming from source
     */
    public SseClient(URL destination, EventListener listener) {
        this.destination = destination;
        this.listener = listener;
    }

    /**
     * Check incoming input to be equal to the end character (line feed = 10)
     * @param inputSSE string builder that must be analyzed not to be equal to the end characters
     * @return true if there is end of the event or false if not
     */
    boolean isEndOfEvent(StringBuilder inputSSE) {
        return (
                //  event is not ended if there is no input at the moment
                (inputSSE.length() > 0) && (
                        //  if length of the event is less than 2 symbols and first character is line feed - event is finished
                        ((inputSSE.length() < 2) && inputSSE.codePointAt(0) == 10) ||

                                //  if the last character is line feed - event is finished
                                (inputSSE.codePointAt(inputSSE.length() - 1) == 10) &&
                                        (inputSSE.codePointAt(inputSSE.length() - 2) == 125)
                )
        );
    }

    /**
     * get sse-event and transform in into string, calls message() that must be overridden by programmer
     * @param input possible SSE event that must be handled
     */
    boolean getSSEEvent(InputStream input){
        try {
            String type = "message";
            StringBuilder payload = new StringBuilder();

            while(true) {
                //  build a string by appending incoming data to it
                StringBuilder sBuilder = new StringBuilder();

                while(!isEndOfEvent(sBuilder)) {
                    //  read incoming data
                    int incomingData = input.read();

                    //  check not to be end of transmittion stream
                    if(incomingData == -1)
                        throw new EOFException("End of the event stream");

                    //  transform incoming data into character
                    sBuilder.append((char) incomingData);
                }

                if(sBuilder.toString().equals("\n")) { break; }
                String[] line = sBuilder.toString().split("\n");

                if(line[0].startsWith("event:")) { type = line[0].substring(7); }
                if(line[2].startsWith("data:")) {
                    if(line[2].startsWith("data: {\"message\": <SensorReadings ")){
//                        payload.append(line[2].substring(6, 55));
//                        payload.append("\"");
//                        payload.append(line[2].substring(line[2].lastIndexOf("=") + 1, line[2].lastIndexOf(">")));
//                        payload.append("\">");
                        payload.append(line[2].substring(6).replaceAll("'", "\""));
                    } else {
                        payload.append(line[2].substring(6));
                    }
                }
                if(line.length > 3) {
                    for(short i = 3; i < line.length; i++) {
                        payload.append(line[i].trim());
                    }
                }
            }

            SseEvent event = new SseEvent();
            event.setType(type);
            event.setData(payload.toString());
            listener.message(event);
            return true;
        } catch (Exception exception) {
            System.out.println("Ehhh, too fast read, you're Sanic or what?\t" + exception);
            return false;
        }
    }

    /**
     *  connect to the URL, receive input from it. If there is error in receiving - restart connection, reinitialize
     * buffer for input
     * @throws Exception Throws exception if there is any problem with connection or I/O
     */
    public void connect() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) destination.openConnection();
        InputStream input = connection.getInputStream();
        while(true){
            boolean isConnectionOk = getSSEEvent(input);
            if(!isConnectionOk) {
                connection.disconnect();
                input.close();
                connection = (HttpURLConnection) destination.openConnection();
                input = connection.getInputStream();
            }
        }
    }
}
