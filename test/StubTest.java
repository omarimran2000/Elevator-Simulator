import helloWorldStub.HelloWorld;
import helloWorldStub.HelloWorldClient;
import helloWorldStub.HelloWorldServer;
import org.junit.jupiter.api.Test;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class StubTest {
    private static final String testStringInput1 = "Hello world in 1";
    private static final String testStringInput2 = "Hello world in 2";
    private static final String testStringOutput = "Hello world out";

    @Test
    public void sendAndReceive() throws IOException, ClassNotFoundException {
        int port = 8040;
        Config config = new Config();

        HelloWorldServer helloWorldServer = new HelloWorldServer(config, port, testStringInput1, testStringInput2, testStringOutput);
        Thread thread = new Thread(helloWorldServer);
        thread.start();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), port);
        assertEquals(new HelloWorld(testStringOutput), helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1)));
        assertEquals(1, helloWorldServer.getNumCalls().get());
        assertEquals(new HelloWorld(testStringOutput), helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1), new HelloWorld(testStringInput2)));
        assertEquals(2, helloWorldServer.getNumCalls().get());

        thread.interrupt();
    }

    @Test
    public void timeout() throws IOException {
        int port = 8041;
        Config config = new Config();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), port);
        assertThrows(SocketTimeoutException.class, () -> helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1)));
    }

    @Test
    public void interrupt() throws IOException, InterruptedException {
        int port = 8041;
        Config config = new Config();

        HelloWorldServer helloWorldServer = new HelloWorldServer(config, port, testStringInput1, testStringInput2, testStringOutput);
        Thread thread = new Thread(helloWorldServer);
        thread.start();
        thread.interrupt();
        Thread.sleep(100);
        assertFalse(thread.isAlive());
    }
}
