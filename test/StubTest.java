import model.HelloWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stub.HelloWorldClient;
import utill.Config;

import java.io.*;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StubTest {
    private static final String testStringInput = "Hello world in";
    private static final String testStringOutput = "Hello world out";

    @Test
    void sendAndReceiveTest() throws IOException, ClassNotFoundException {
        Config config = new Config();
        AtomicInteger numCalls = new AtomicInteger();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), 8040, responses);

        helloWorldClient.receiveAsync((helloWorld) -> {
            assertEquals(testStringInput, helloWorld.getString());
            numCalls.getAndIncrement();
            return new HelloWorld(testStringOutput);
        });

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(new HelloWorld(testStringInput));
        objectOutputStream.flush();

        byte[] helloWorld = helloWorldClient.send(byteArrayOutputStream.toByteArray());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(helloWorld);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        assertEquals(new HelloWorld(testStringOutput), objectInputStream.readObject());

        assertEquals(1, numCalls.get());
    }

    @Test
    void sendAsyncAndReceiveTest() throws IOException {
        Config config = new Config();
        AtomicInteger numCalls = new AtomicInteger();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), 8041, responses);

        helloWorldClient.receiveAsync((helloWorld) -> {
            assertEquals(testStringInput, helloWorld.getString());
            numCalls.getAndIncrement();
            return new HelloWorld(testStringOutput);
        });

        helloWorldClient.sendAsync(new HelloWorld(testStringInput),
                (helloWorld) -> {
                    assertEquals(testStringOutput, helloWorld.getString());
                    assertEquals(1, numCalls.get());
                },
                Assertions::fail);

    }

    @Test
    void timeout() throws IOException, InterruptedException {
        Config config = new Config();
        AtomicInteger numCalls = new AtomicInteger();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), 8042, responses);

        helloWorldClient.sendAsync(new HelloWorld(testStringInput), (helloWorld) -> fail(), numCalls::getAndIncrement);
        Thread.sleep(config.getIntProperty("socketTimeout")* 2L);
        assertEquals(1, numCalls.get());
    }
}
