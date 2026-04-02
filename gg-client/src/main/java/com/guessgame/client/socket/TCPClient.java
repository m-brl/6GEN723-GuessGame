package com.guessgame.client.socket;

import com.guessgame.client.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.nio.ByteBuffer;

public class TCPClient {
    private String ip;
    private int port;

    private InetAddress inetAddress;
    private Socket socket;

    Boolean connected = false;

    private RingBuffer readBuffer;
    private RingBuffer writeBuffer;

    public TCPClient() {
        this.readBuffer = new RingBuffer(1024, (byte) '\n');
        this.writeBuffer = new RingBuffer(1024, (byte) '\n');
    }

    public TCPClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.readBuffer = new RingBuffer(1024, (byte) '\n');
        this.writeBuffer = new RingBuffer(1024, (byte) '\n');
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        try {
            inetAddress = InetAddress.getByName(ip);
            socket = new Socket(inetAddress, port);
            connected = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to server: " + e.getMessage(), e);
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            connected = false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to disconnect from server: " + e.getMessage(), e);
        }
    }

    public int write(ByteBuffer data) {
        return writeBuffer.write(data);
    }

    public int write(byte[] data, int length) {
        return writeBuffer.write(data, length);
    }

    public int read(ByteBuffer dest) {
        return readBuffer.read(dest);
    }

    public int read(byte[] dest, int length) {
        return readBuffer.read(dest, length);
    }

    public void flush() {
        if (!connected) {
            return;
        }
        ByteBuffer tempBuffer = ByteBuffer.allocate(writeBuffer.getCapacity());
        Logger.getInstance().debug("Flushing data to server: " + writeBuffer.getCapacity() + " bytes");

        try {
            int bytesToWrite = writeBuffer.read(tempBuffer);
            socket.getOutputStream().write(tempBuffer.array(), 0, bytesToWrite);
            Logger.getInstance().debug("Flushed " + bytesToWrite + " bytes to server");
        } catch (Exception e) {
            throw new RuntimeException("Failed to flush data to server: " + e.getMessage(), e);
        }
        tempBuffer.clear();

        try {
            int bytesRead = socket.getInputStream().read(tempBuffer.array(), 0, tempBuffer.capacity());
            if (readBuffer.write(tempBuffer) < bytesRead) {
                throw new RuntimeException("Read buffer overflow: received " + bytesRead + " bytes but only " + readBuffer.getCapacity() + " bytes available");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read data from server: " + e.getMessage(), e);
        }
    }
}
