package stub;

import model.HelloWorld;
import model.StubRequestMessage;
import utill.Config;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HelloWorldClient extends StubClient {
    private final InetAddress inetAddress;
    private final int port;
    private final Responses responses;
    private DatagramSocket datagramSocket;

    public HelloWorldClient(Config config, InetAddress inetAddress, int port, Responses responses) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
        this.responses = responses;
    }

    public void sendAsyncOne(HelloWorld helloWorld, Consumer<HelloWorld> callback, Runnable timeoutCallback) {
        sendAsync(new StubRequestMessage(1, List.of(helloWorld)), inetAddress, port, (stubResponseMessage) -> {
        }, timeoutCallback);
    }

    public void sendAsyncTwo(HelloWorld helloWorld, Consumer<HelloWorld> callback, Runnable timeoutCallback) {
        sendAsync(datagramSocket, new StubRequestMessage(2,List.of(helloWorld)) , inetAddress, port, callback, timeoutCallback);
    }

    public byte[] send(byte[] bytes) throws SocketTimeoutException, SocketException {
        return send(bytes, inetAddress, port);
    }

    @Override
    public void run() {
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            receiveFromSendAsync(datagramSocket, responses.getMap());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static abstract class Responses {
        private final Consumer<Serializable> sendAsyncOneCallback;
        private final Consumer<Serializable> sendAsyncTwoCallback;

        public Responses(Consumer<Serializable> sendAsyncOneCallback, Consumer<Serializable> sendAsyncTwoCallback) {
            this.sendAsyncOneCallback = sendAsyncOneCallback;
            this.sendAsyncTwoCallback = sendAsyncTwoCallback;
        }

        protected Map<Integer, Consumer<Serializable>> getMap() {
            return Map.of(1, sendAsyncOneCallback, 2, sendAsyncTwoCallback);
        }
    }
}
