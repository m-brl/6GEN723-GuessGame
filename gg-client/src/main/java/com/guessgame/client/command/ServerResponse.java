package com.guessgame.client.command;

public class ServerResponse {
    public static class Connected implements Response {
        public Connected() {}

        @Override
        public void setArgs(String[] args) {}
        @Override
        public void execute() {}
    };

    public static class RoomCreated implements Response {
        public RoomCreated(/* let empty */) {}

        @Override
        public void setArgs(String[] args) {}
        @Override
        public void execute() {}
    };
}
