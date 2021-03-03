package helloWorldStub;

import stub.StubClient;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class HelloWorldClient extends StubClient implements HelloWorldApi {
    private final InetAddress inetAddress;
    private final int port;

    public HelloWorldClient(Config config, InetAddress inetAddress, int port) {
        super(config);
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public HelloWorld sendAndReceive(HelloWorld helloWorld) throws IOException, ClassNotFoundException {
        return sendAndReceive(1, helloWorld, inetAddress, port);
    }

    public HelloWorld sendAndReceive(HelloWorld helloWorld1, HelloWorld helloWorld2) throws IOException, ClassNotFoundException {
        return sendAndReceive(2, List.of(helloWorld1, helloWorld2), inetAddress, port);
    }

    public void sendAndReceiveAck(HelloWorld helloWorld) throws IOException, ClassNotFoundException {
        sendAndReceive(3, helloWorld, inetAddress, port);
    }
}
