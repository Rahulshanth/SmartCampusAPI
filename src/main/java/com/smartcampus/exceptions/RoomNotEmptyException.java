/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exceptions;

import java.util.List;

public class RoomNotEmptyException extends RuntimeException {

    private final String roomId;
    private final List<String> sensorIds;

    // Constructor — stores the room ID and its sensor list for use in the mapper
    public RoomNotEmptyException(String roomId, List<String> sensorIds) {
        super("Room '" + roomId + "' cannot be deleted because it still has sensors assigned.");
        this.roomId    = roomId;
        this.sensorIds = sensorIds;
    }

    public String getRoomId()          { return roomId; }
    public List<String> getSensorIds() { return sensorIds; }
}
