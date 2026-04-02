package com.guessgame.client.command;

import com.guessgame.client.NetworkManager;

public class ServerCommand {
    public static class Connect implements Command {
        private Class<? extends Response> responseClass = ServerResponse.Connected.class;
        Response response;

        private String player_name;

        public Connect() {}

        @Override
        public void setArgs(String[] args) {
            player_name = args[0];
        }

        @Override
        public void execute() {
            NetworkManager.getInstance().setConnectionInfo("localhost", 8080);
            NetworkManager.getInstance().connect();
        }

        public void setResponse(Response response) {
            if (!responseClass.isInstance(response)) {
                throw new IllegalArgumentException("Invalid response type");
            }
            this.response = response;
        }

        @Override
        public String dump() {
            String command = String.format("GG|CONNECT|%s\n", player_name);
            return command;
        }
    }

    public static class CreateRoom implements Command {
        private Class<? extends Response> responseClass = ServerResponse.RoomCreated.class;
        Response response;

        private String room_name;

        public CreateRoom() {}

        @Override
        public void setArgs(String[] args) {
            room_name = args[0];
        }

        @Override
        public void execute() {}

        public void setResponse(Response response) {
            if (!responseClass.isInstance(response)) {
                throw new IllegalArgumentException("Invalid response type");
            }
            this.response = response;
        }

        @Override
        public String dump() {
            String command = String.format("GG|CREATE_ROOM|%s\n", room_name);
            return command;
        }
    }

    // Create other game commands
}
