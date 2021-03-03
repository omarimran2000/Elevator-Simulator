package stub;

import model.StubRequestMessage;
import utill.Config;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class StubServer implements Runnable {
    protected final Config config;

    protected StubServer(Config config) {
        this.config = config;
    }

    protected void receiveAsync(int port, Map<Integer, Function<List<Serializable>, Serializable>> callbacks) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        StubRequestMessage stubRequestMessage;
        byte[] buff;
        DatagramPacket datagramPacket;
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            while (!Thread.interrupted()) {
                buff = new byte[config.getIntProperty("maxMessageSize")];
                datagramPacket = new DatagramPacket(buff, buff.length);
                datagramSocket.receive(datagramPacket);
                byteArrayInputStream = new ByteArrayInputStream(buff);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                stubRequestMessage = (StubRequestMessage) objectInputStream.readObject();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(callbacks.get(stubRequestMessage.getFunctionNumber()).apply(stubRequestMessage.getArguments()));
                objectOutputStream.flush();
                buff = byteArrayOutputStream.toByteArray();
                datagramSocket.send(new DatagramPacket(buff, buff.length, datagramPacket.getAddress(), datagramPacket.getPort()));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
