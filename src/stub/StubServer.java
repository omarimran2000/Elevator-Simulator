package stub;

import model.StubRequestMessage;
import utill.Config;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract stub server for rpc.
 */
public abstract class StubServer {
    /**
     * The application configuration file loader.
     */
    protected final Config config;

    /**
     * The default stub server constructor.
     *
     * @param config The application configuration file loader.
     */
    protected StubServer(Config config) {
        this.config = config;
    }

    /**
     * receiveAsync binds to a socket and then uses callbacks to handle requests
     * <p>
     * The expected implementation of this method is to use the callbacks to specify the server's response to requests.
     * The key of callbacks is the function number and specifies which function was requested.
     * The value of callbacks is the function to be called.
     * For a function with one argument, the callback will be called with a list of length one.
     * For void functions, the callback should return AckMessage.
     *
     * @param port      The port number to bind to.
     * @param callbacks The function number mapped to the sever callback function.
     * @throws IOException            IOException is thrown if the server fails to send, receive, or bind to the port.
     * @throws ClassNotFoundException ClassNotFoundException is thrown if serialization failed.
     * @see model.AckMessage
     */
    protected void receiveAsync(int port, Map<Integer, Function<List<Serializable>, Serializable>> callbacks) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        StubRequestMessage stubRequestMessage;
        Serializable response;
        byte[] buff;
        DatagramPacket datagramPacket;

        //using try-with-resources to close the datagram socket
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            while (!Thread.interrupted()) { //loop until the thread is interrupted
                //reset buff between requests
                buff = new byte[config.getIntProperty("maxMessageSize")];
                datagramPacket = new DatagramPacket(buff, buff.length);

                //receive request
                datagramSocket.receive(datagramPacket);

                //deserialize request
                byteArrayInputStream = new ByteArrayInputStream(buff);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                stubRequestMessage = (StubRequestMessage) objectInputStream.readObject();

                //run the appropriate callback to get response
                response = callbacks.get(stubRequestMessage.getFunctionNumber()).apply(stubRequestMessage.getArguments());

                //serialize response
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(response);
                objectOutputStream.flush();
                buff = byteArrayOutputStream.toByteArray();

                //send response
                datagramSocket.send(new DatagramPacket(buff, buff.length, datagramPacket.getAddress(), datagramPacket.getPort()));
            }
        }
    }


}
