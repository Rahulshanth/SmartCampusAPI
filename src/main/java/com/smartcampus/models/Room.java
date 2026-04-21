

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.models;

import java.util.ArrayList;
import java.util.List;

/*
 * Room - The data model (POJO) for a Campus Room.
 *
 * This class is a simple blueprint that describes what a "Room" looks like.
 * Every room stored in our system will be an object created from this class.
 *
 * WHY DO WE NEED GETTERS AND SETTERS?
 *   Jackson (our JSON library) needs them to:
 *     - Convert a Room object → JSON  (uses getters)
 *     - Convert JSON → Room object    (uses setters)
 *   Without them, your API will return empty {} in responses!
 *
 * FIELDS EXPLAINED:
 *   id         → Unique identifier,  e.g., "LIB-301"
 *   name       → Human-readable name, e.g., "Library Quiet Study"
 *   capacity   → Max number of people allowed in the room
 *   sensorIds  → List of sensor IDs currently deployed in this room
 */
public class Room {

    private String id;
    private String name;
    private int capacity;
    private List<String> sensorIds;

    // ── Default constructor ──────────────────────────────────────────────────
    // Jackson REQUIRES this empty constructor to convert incoming JSON → Room
    public Room() {
        this.sensorIds = new ArrayList<>();
    }

    // ── Parameterized constructor ────────────────────────────────────────────
    // Useful for quickly creating Room objects in code (e.g., sample data)
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.sensorIds = new ArrayList<>();
    }

    // ── Getters (read the value of a field) ─────────────────────────────────
    public String getId()              { return id; }
    public String getName()            { return name; }
    public int getCapacity()           { return capacity; }
    public List<String> getSensorIds() { return sensorIds; }

    // ── Setters (update the value of a field) ────────────────────────────────
    public void setId(String id)                     { this.id = id; }
    public void setName(String name)                 { this.name = name; }
    public void setCapacity(int capacity)            { this.capacity = capacity; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }
}
