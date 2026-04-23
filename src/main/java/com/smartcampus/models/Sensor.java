/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.models;
 
public class Sensor {
 
    private String id;
    private String type;
    private String status;
    private double currentValue;
    private String roomId;
 
    // ── Default constructor ──────────────────────────────────────────────────
    // Jackson REQUIRES this empty constructor to convert incoming JSON → Sensor
    public Sensor() {}
 
    // ── Parameterized constructor ────────────────────────────────────────────
    // Useful for quickly creating Sensor objects in code (e.g., sample data)
    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id           = id;
        this.type         = type;
        this.status       = status;
        this.currentValue = currentValue;
        this.roomId       = roomId;
    }
 
    // ── Getters (read the value of a field) ─────────────────────────────────
    public String getId()           { return id; }
    public String getType()         { return type; }
    public String getStatus()       { return status; }
    public double getCurrentValue() { return currentValue; }
    public String getRoomId()       { return roomId; }
 
    // ── Setters (update the value of a field) ────────────────────────────────
    public void setId(String id)                   { this.id = id; }
    public void setType(String type)               { this.type = type; }
    public void setStatus(String status)           { this.status = status; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }
    public void setRoomId(String roomId)           { this.roomId = roomId; }
}