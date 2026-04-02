package com.guessgame.client;

import java.util.Vector;

class RoomManager {
    private static RoomManager instance;
    private Vector<String> rooms = new Vector<>();

    private RoomManager() {}

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public void addRoom(String roomName) {
        rooms.add(roomName);
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public boolean roomExists(String roomName) {
        return rooms.contains(roomName);
    }

    public Vector<String> getRooms() {
        return rooms;
    }

};
