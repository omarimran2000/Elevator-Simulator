package helloWorldStub;

import stub.StubServer;
import utill.Config;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldServer extends StubServer {
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

    private HelloWorld sendAndReceive(HelloWorld helloWorld) {
        assertEquals(testStringInput1, helloWorld.getString());
        numCalls.incrementAndGet();
        return new HelloWorld(testStringOutput);
    }

    private HelloWorld sendAndReceive(HelloWorld helloWorld1, HelloWorld helloWorld2) {
        assertEquals(testStringInput1, helloWorld1.getString());
        assertEquals(testStringInput2, helloWorld2.getString());
        numCalls.incrementAndGet();
        return new HelloWorld(testStringOutput);
    }


    @Override
    public void run() {
        receiveAsync(port, Map.of(
                1, input -> sendAndReceive((HelloWorld) input.get(0)),
                2, input -> sendAndReceive((HelloWorld) input.get(0), (HelloWorld) input.get(1))));
    }
}
