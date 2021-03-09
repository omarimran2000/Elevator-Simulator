package helloWorldStub;

import java.io.IOException;

public interface HelloWorldApi {
    HelloWorld sendAndReceive(HelloWorld helloWorld) throws IOException, ClassNotFoundException;

    HelloWorld sendAndReceive(HelloWorld helloWorld1, HelloWorld helloWorld2) throws IOException, ClassNotFoundException;

    void sendAndReceiveAck(HelloWorld helloWorld) throws IOException, ClassNotFoundException;
}
