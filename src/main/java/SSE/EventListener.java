package SSE;

/**
 * interface for overriding
 */
public interface EventListener {
    void message(SseEvent event) throws Exception;
}