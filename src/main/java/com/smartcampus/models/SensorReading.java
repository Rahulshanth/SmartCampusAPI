/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.models;

/**
 *
 * @author optyo
 */
public class SensorReading {
 
    private String id;         // Unique reading ID (e.g., "READ-001")
    private long timestamp;    // Epoch time in milliseconds when reading was captured
    private double value;      // The actual measured value (e.g., 23.5 for temperature)
 
    // ── Default constructor ──────────────────────────────────────────────────
    // Jackson REQUIRES this to convert incoming JSON → SensorReading object
    public SensorReading() {}
 
    // ── Parameterized constructor ────────────────────────────────────────────
    public SensorReading(String id, long timestamp, double value) {
        this.id        = id;
        this.timestamp = timestamp;
        this.value     = value;
    }
 
    // ── Getters ──────────────────────────────────────────────────────────────
    public String getId()        { return id; }
    public long getTimestamp()   { return timestamp; }
    public double getValue()     { return value; }
 
    // ── Setters ──────────────────────────────────────────────────────────────
    public void setId(String id)             { this.id = id; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setValue(double value)       { this.value = value; }
}
