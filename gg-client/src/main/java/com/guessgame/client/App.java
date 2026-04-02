package com.guessgame.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.guessgame.client.socket.TCPClient;
import com.guessgame.client.command.Command;
import com.guessgame.client.command.ServerCommand;
import com.guessgame.client.command.ServerResponse;
import com.guessgame.client.command.CommandFactory;

public class App
{
    public static void parseInput(String input) {
        String[] parts = input.split("\\|");
        if (parts.length < 2 || !parts[0].equals("GG")) {
            throw new IllegalArgumentException("Invalid command format");
        }
        String[] args = new String[parts.length - 2];
        for (int i = 2; i < parts.length; i++) {
            args[i - 2] = parts[i];
        }
        Command command = CommandFactory.getInstance().createCommand(parts[1], args);
        command.execute();
        NetworkManager.getInstance().sendCommand(command.dump());
    }

    public static void main(String[] args)
    {
        TCPClient client = new TCPClient("localhost", 8080);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line;
            try {
                line = reader.readLine();
                parseInput(line);
                NetworkManager.getInstance().flush();
            } catch (Exception e) {
                ExceptionManager.handle(e);
            }
        }
    }
}
