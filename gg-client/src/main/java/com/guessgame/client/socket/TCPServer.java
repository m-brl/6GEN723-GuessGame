package com.guessgame.client.socket;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;

public class TCPServer {
    private int port;
    private String ip;

    private InetAddress inetAddress;
    private SocketAddress socketAddress;
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private boolean isRunning = false;

    public TCPServer() {}

    public TCPServer(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (isRunning) {
            throw new IllegalStateException("Cannot change port while server is running");
        }
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        if (isRunning) {
            throw new IllegalStateException("Cannot change IP while server is running");
        }
        this.ip = ip;
    }

    public void listen() {
        try {
            this.inetAddress = InetAddress.getByName(ip);
            this.socketAddress = new InetSocketAddress(inetAddress, port);

            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.bind(socketAddress);
            this.serverSocketChannel.configureBlocking(false);

            this.selector = Selector.open();
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TCPServer: " + e.getMessage(), e);
        }
        isRunning = true;
    }

    public void close() {
        try {
            if (selector != null) selector.close();
            if (serverSocketChannel != null) serverSocketChannel.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close TCPServer: " + e.getMessage(), e);
        }
        isRunning = false;
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();

            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            clientChannel.register(selector, SelectionKey.OP_WRITE);
        } catch(Exception e) {
            throw new RuntimeException("Failed to accept connection: " + e.getMessage(), e);
        }
    }

};
