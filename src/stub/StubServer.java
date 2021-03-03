package stub;

import model.StubRequestMessage;
import model.StubResponseMessage;
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
        byte[] buff = new byte[config.getIntProperty("maxMessageSize")];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            while (!Thread.interrupted()) {
                datagramSocket.receive(datagramPacket);
                byteArrayInputStream = new ByteArrayInputStream(buff);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                stubRequestMessage = (StubRequestMessage) objectInputStream.readObject();
                StubResponseMessage stubResponseMessage = new StubResponseMessage(
                        stubRequestMessage.getFunctionNumber(),
                        callbacks.get(stubRequestMessage.getFunctionNumber()).apply(stubRequestMessage.getArguments()));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(stubResponseMessage);
                objectOutputStream.flush();
                buff = byteArrayOutputStream.toByteArray();
                datagramSocket.send(new DatagramPacket(buff, buff.length, datagramPacket.getAddress(), datagramPacket.getPort()));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
