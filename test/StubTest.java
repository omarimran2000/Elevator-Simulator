import helloWorldStub.HelloWorld;
import helloWorldStub.HelloWorldClient;
import helloWorldStub.HelloWorldServer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import utill.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class StubTest {
    private static final String testStringInput1 = "Hello world in 1";
    private static final String testStringInput2 = "Hello world in 2";
    private static final String testStringOutput = "Hello world out";

    @Test
    @Order(1)
    public void sendAndReceive() throws IOException, ClassNotFoundException {
        int port = 8040;
        Config config = new Config();

        HelloWorldServer helloWorldServer = new HelloWorldServer(config, port, testStringInput1, testStringInput2, testStringOutput);
        helloWorldServer.start();


        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), port);
        assertEquals(new HelloWorld(testStringOutput), helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1)));
        assertEquals(1, helloWorldServer.getNumCalls().get());
        assertEquals(new HelloWorld(testStringOutput), helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1), new HelloWorld(testStringInput2)));
        assertEquals(2, helloWorldServer.getNumCalls().get());
        helloWorldClient.sendAndReceiveAck(new HelloWorld(testStringInput1));
        assertEquals(3, helloWorldServer.getNumCalls().get());

        helloWorldServer.interrupt();
    }

    @Test
    @Order(3)
    public void timeout() throws IOException, InterruptedException {
        int port = 8041;
        Config config = new Config();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), port);
        assertThrows(SocketTimeoutException.class, () -> helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1)));
        //FIXME socket timeout bleeds across sockets
        Thread.sleep(config.getIntProperty("maxMessageSize") * 2L);
        assertThrows(SocketTimeoutException.class, () -> helloWorldClient.sendAndReceiveAck(new HelloWorld(testStringInput1)));
    }

    @Test
    @Order(2)
    public void interrupt() throws IOException, InterruptedException {
        int port = 8042;
        Config config = new Config();

        HelloWorldServer helloWorldServer = new HelloWorldServer(config, port, testStringInput1, testStringInput2, testStringOutput);
        helloWorldServer.start();
        helloWorldServer.interrupt();
        Thread.sleep(100);
        assertFalse(helloWorldServer.isAlive());
    }
}
