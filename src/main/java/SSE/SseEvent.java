package SSE;

/**
 * class defining sse event unit
 */
public class SseEvent {
    //  Type of info that can be transmitted via SSE-formatted input
    private String data;
    private String type;

    //  getters
    public String getData() { return data; }
    public String getType() { return type; }

    //  setters
    public void setData(String data) { this.data = data; }
    public void setType(String type) { this.type = type; }
}
