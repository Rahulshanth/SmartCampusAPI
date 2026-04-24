/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exceptions;

public class SensorUnavailableException extends RuntimeException {

    private final String sensorId;
    private final String currentStatus;

    public SensorUnavailableException(String sensorId, String currentStatus) {
        super("Sensor '" + sensorId + "' is currently in '" + currentStatus +
              "' state and cannot accept new readings.");
        this.sensorId      = sensorId;
        this.currentStatus = currentStatus;
    }

    public String getSensorId()      { return sensorId; }
    public String getCurrentStatus() { return currentStatus; }
}
