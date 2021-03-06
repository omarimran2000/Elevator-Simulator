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
    /**
     * Tests send and receive
     */
    public void sendAndReceive() throws IOException, ClassNotFoundException {
        int port = 8040;
        Config config = new Config();

        HelloWorldServer helloWorldServer = new HelloWorldServer(config, port, testStringInput1, testStringInput2, testStringOutput);
        helloWorldServer.start();


        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), port);
        //Checks to see that the client receivces the message with the proper information (function number 1)
        assertEquals(new HelloWorld(testStringOutput), helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1)));
        //Checks that the number of calls is 1 since only 1 message has been sent
        assertEquals(1, helloWorldServer.getNumCalls().get());
        //Checks to see that the client receivces the message with the proper information (function number 2)
        assertEquals(new HelloWorld(testStringOutput), helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1), new HelloWorld(testStringInput2)));
        //Checks that the number of calls has increased by 1 since last check
        assertEquals(2, helloWorldServer.getNumCalls().get());
        //Checks to see that the client receivces the message with the proper information (function number 3)
        helloWorldClient.sendAndReceiveAck(new HelloWorld(testStringInput1));
        //Checks that the number of calls has increased by 1 since last check
        assertEquals(3, helloWorldServer.getNumCalls().get());

        helloWorldServer.interrupt();
    }

    @Test
    @Order(3)
    /**
     * Tests timeout
     */
    public void timeout() throws IOException, InterruptedException {
        int port = 8041;
        Config config = new Config();

        HelloWorldClient helloWorldClient = new HelloWorldClient(config, InetAddress.getLocalHost(), port);
        assertThrows(SocketTimeoutException.class, () -> helloWorldClient.sendAndReceive(new HelloWorld(testStringInput1)));

        Thread.sleep(config.getIntProperty("maxMessageSize") * 2L);
        assertThrows(SocketTimeoutException.class, () -> helloWorldClient.sendAndReceiveAck(new HelloWorld(testStringInput1)));
    }

    @Test
    @Order(2)
    /**
     * Tests interrupt
     */
    public void interrupt() throws IOException, InterruptedException {
        int port = 8042;
        Config config = new Config();

        HelloWorldServer helloWorldServer = new HelloWorldServer(config, port, testStringInput1, testStringInput2, testStringOutput);
        helloWorldServer.start();
        helloWorldServer.interrupt();
        Thread.sleep(100);
        //Makes sure the thread is no longer active
        assertFalse(helloWorldServer.isAlive());
    }
}
