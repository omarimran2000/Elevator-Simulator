package helloWorldStub;

import model.AckMessage;
import stub.StubServer;
import utill.Config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldServer extends StubServer implements Runnable, HelloWorldApi {
    private final String testStringInput1;
    private final String testStringInput2;
    private final String testStringOutput;
    private final int port;
    private final AtomicInteger numCalls;

    public HelloWorldServer(Config config, int port, String testStringInput1, String testStringInput2, String testStringOutput) {
        super(config);
        this.testStringInput1 = testStringInput1;
        this.testStringInput2 = testStringInput2;
        this.testStringOutput = testStringOutput;
        this.port = port;
        numCalls = new AtomicInteger(0);
    }

    public AtomicInteger getNumCalls() {
        return numCalls;
    }


    public HelloWorld sendAndReceive(HelloWorld helloWorld) {
        assertEquals(testStringInput1, helloWorld.getString());
        numCalls.incrementAndGet();
        return new HelloWorld(testStringOutput);
    }

    public HelloWorld sendAndReceive(HelloWorld helloWorld1, HelloWorld helloWorld2) {
        assertEquals(testStringInput1, helloWorld1.getString());
        assertEquals(testStringInput2, helloWorld2.getString());
        numCalls.incrementAndGet();
        return new HelloWorld(testStringOutput);
    }

    public void sendAndReceiveAck(HelloWorld helloWorld) {
        assertEquals(testStringInput1, helloWorld.getString());
        numCalls.incrementAndGet();
    }

    @Override
    public void run() {
        try {
            receiveAsync(port, Map.of(
                    1, input -> sendAndReceive((HelloWorld) input.get(0)),
                    2, input -> sendAndReceive((HelloWorld) input.get(0), (HelloWorld) input.get(1)),
                    3, input -> {
                        sendAndReceive((HelloWorld) input.get(0));
                        return new AckMessage();
                    }));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
