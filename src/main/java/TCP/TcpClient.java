package TCP;

import Actor.Actor;
import Actor.Behavior;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * TCP client for sending information to the server
 */
public class TcpClient {
    private final Socket socket;
    private Behavior<Object> clientBehavior;

    /**
     * constructor defining message delivery destination
     * @param destinationIP IP address of destination point
     * @param destinationPort port of destination point
     * @throws IOException error in I/O stream
     */
    public TcpClient(String destinationIP, int destinationPort) throws IOException {
        System.out.println(destinationPort);
        socket = new Socket(destinationIP, destinationPort);

        //  behavior for client model, where actor will send information to destination point
        clientBehavior = new Behavior<Object>() {
            @Override
            public boolean onReceive(Actor<Object> self, Object msg) throws Exception {
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(msg);

                return true;
            }

            @Override
            public void onException(Actor<Object> self, Exception e) {
                System.out.println("Ehhh, error sending message from " + self.getName());
            }
        };
    }

    /**
     * set destination of message delivery
     * @param destinationIp IP address of destination point
     * @param destinationPort port of destination point
     * @throws IOException error in I/O stream
     */
    public void setDestination(String destinationIp, int destinationPort) throws IOException {
        SocketAddress socketAddress = new InetSocketAddress(destinationIp, destinationPort);
        socket.bind(socketAddress);
        socket.connect(socketAddress);
    }

    /**
     * get behavior of TCP client
     * @return behavior of client
     */
    public Behavior<Object> getClientBehavior() {
        return clientBehavior;
    }

    /**
     * set new behavior to tcp client
     * @param behavior behavior to set for tcp client
     */
    public void setClientBehavior(Behavior behavior) {
        this.clientBehavior = behavior;
    }

    /**
     * get connection socket from client
     * @return client's socket
     */
    public Socket getSocket() {
        return socket;
    }
}
