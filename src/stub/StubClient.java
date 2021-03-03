package stub;

import model.StubRequestMessage;
import utill.Config;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public abstract class StubClient {
    protected final Config config;

    protected StubClient(Config config) {
        this.config = config;
    }

    protected <T> T sendAndReceive(int functionNumber, Serializable argument, InetAddress inetAddress, int port) throws IOException, ClassNotFoundException {
        return sendAndReceive(new StubRequestMessage(functionNumber, List.of(argument)), inetAddress, port);
    }

    protected <T> T sendAndReceive(int functionNumber, List<Serializable> arguments, InetAddress inetAddress, int port) throws IOException, ClassNotFoundException {
        return sendAndReceive(new StubRequestMessage(functionNumber, arguments), inetAddress, port);
    }

    protected <T> T sendAndReceive(StubRequestMessage stubRequestMessage, InetAddress inetAddress, int port) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        objectOutputStream.writeObject(stubRequestMessage);
        objectOutputStream.flush();
        byteArrayInputStream = new ByteArrayInputStream(sendAndReceive(byteArrayOutputStream.toByteArray(), inetAddress, port));
        objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (T) objectInputStream.readObject();
    }

    protected byte[] sendAndReceive(byte[] data, InetAddress inetAddress, int port) throws IOException {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            return sendAndReceive(datagramSocket, data, inetAddress, port);
        }
    }

    protected byte[] sendAndReceive(DatagramSocket datagramSocket, byte[] data, InetAddress inetAddress, int port) throws IOException {
        byte[] buff = new byte[config.getIntProperty("maxMessageSize")];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        datagramSocket.setSoTimeout(config.getIntProperty("socketTimeout"));
        datagramSocket.send(new DatagramPacket(data, data.length, inetAddress, port));
        datagramSocket.receive(datagramPacket);
        return buff;
    }
}
