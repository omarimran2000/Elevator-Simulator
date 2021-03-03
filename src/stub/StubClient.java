package stub;

import model.StubRequestMessage;
import model.StubResponseMessage;
import utill.Config;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.function.Consumer;

public abstract class StubClient implements Runnable {
    protected final Config config;

    protected StubClient(Config config) {
        this.config = config;
    }

    protected void sendAsync(DatagramSocket datagramSocket, byte[] data, InetAddress inetAddress, int port, Consumer<byte[]> callback, Runnable timeoutCallback) {
        new Thread(() -> {
            try {
                callback.accept(send(datagramSocket, data, inetAddress, port));
            } catch (SocketTimeoutException e) {
                timeoutCallback.run();
            }
        }).start();
    }


    protected void sendAsync(DatagramSocket datagramSocket, StubRequestMessage stubRequestMessage, InetAddress inetAddress, int port, Runnable timeoutCallback) {
        new Thread(() -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(stubRequestMessage);
                objectOutputStream.flush();
                send(datagramSocket, byteArrayOutputStream.toByteArray(), inetAddress, port);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }


    protected byte[] sendAndReceive(byte[] data, InetAddress inetAddress, int port) throws SocketTimeoutException, SocketException {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            return sendAndReceive(datagramSocket, data, inetAddress, port);
        }
    }

    protected byte[] sendAndReceive(DatagramSocket datagramSocket, byte[] data, InetAddress inetAddress, int port) throws SocketTimeoutException {
        byte[] buff = new byte[config.getIntProperty("maxMessageSize")];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        try {
            datagramSocket.setSoTimeout(config.getIntProperty("socketTimeout"));
            datagramSocket.send(new DatagramPacket(data, data.length, inetAddress, port));
            datagramSocket.receive(datagramPacket);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;
    }

    protected void send(DatagramSocket datagramSocket, byte[] data, InetAddress inetAddress, int port) {
        try {
            datagramSocket.setSoTimeout(config.getIntProperty("socketTimeout"));
            datagramSocket.send(new DatagramPacket(data, data.length, inetAddress, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void receiveFromSendAsync(DatagramSocket datagramSocket, Map<Integer, Consumer<Serializable>> callbacks) {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        StubResponseMessage stubResponseMessage;
        byte[] buff = new byte[config.getIntProperty("maxMessageSize")];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        try {
            while (!Thread.interrupted()) {
                datagramSocket.receive(datagramPacket);
                byteArrayInputStream = new ByteArrayInputStream(buff);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                stubResponseMessage = (StubResponseMessage) objectInputStream.readObject();
                callbacks.get(stubResponseMessage.getFunctionNumber()).accept(stubResponseMessage.getOutput());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
