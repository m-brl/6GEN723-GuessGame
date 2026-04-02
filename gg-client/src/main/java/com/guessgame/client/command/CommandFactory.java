package com.guessgame.client.command;

import java.util.Map;
import java.util.HashMap;

public class CommandFactory {
    private static CommandFactory instance;

    Map<String, Class<? extends Command>> commandMap;
    Map<String, Class<? extends Response>> responseMap;

    private CommandFactory() {
        this.commandMap = new HashMap<>();
        commandMap.put("CONNECT", ServerCommand.Connect.class);

        this.responseMap = new HashMap<>();
        responseMap.put("CONNECTED", ServerResponse.Connected.class);
        responseMap.put("ROOM_CREATED", ServerResponse.RoomCreated.class);
    }

    public static synchronized CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    public Command createCommand(String commandName, String[] args) {
        Class<? extends Command> commandClass = commandMap.get(commandName);
        if (commandClass == null) {
            throw new IllegalArgumentException("Unknown command: " + commandName);
        }
        Command command = commandClass.getDeclaredConstructor().newInstance();
        command.setArgs(args);
        return command;
    }
};
