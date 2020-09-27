package TCP;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * TCP server for listening to incoming messages
 */
public class TcpServer {
    //  port of server
    private int serverPort;

    //  socket of server
    private ServerSocket serverSocket;

    /**
     * contructor for server, defining open port for listening
     * @param whichPortToOpen port which will listen
     * @throws IOException error in I/O
     */
    public TcpServer(int whichPortToOpen) throws IOException {
        serverSocket = new ServerSocket(whichPortToOpen);
        this.serverPort = whichPortToOpen;
    }

    /**
     * get port defined for server
     * @return port defined for server
     */
    public int getServerPort() { return serverPort; }

    /**
     * get socket of connection to server
     * @return socket of connection to server
     */
    public ServerSocket getServerSocket() { return serverSocket; }
}
