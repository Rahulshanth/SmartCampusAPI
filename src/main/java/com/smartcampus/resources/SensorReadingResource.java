/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
 
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
 
    // ── In-memory readings store ─────────────────────────────────────────────
    // Key   = Sensor ID  (e.g., "SENS-001")
    // Value = List of SensorReading objects recorded for that sensor
    //
    // "static" means ALL instances of this class share the SAME map,
    // so readings are never lost between requests.
    private static final Map<String, List<SensorReading>> readingsStore = new HashMap<>();
 
    // ── Fields set by the sub-resource locator ───────────────────────────────
    // SensorResource passes these values when it creates this object
    private final String              sensorId;    // which sensor we are working with
    private final Map<String, Sensor> sensorStore; // shared reference to all sensors
 
    // ── Constructor ──────────────────────────────────────────────────────────
    // Called by SensorResource's sub-resource locator method
    public SensorReadingResource(String sensorId, Map<String, Sensor> sensorStore) {
        this.sensorId    = sensorId;
        this.sensorStore = sensorStore;
    }
 
 
    // ════════════════════════════════════════════════════════════════════════
    // PART 4.2 — GET /api/v1/sensors/{sensorId}/readings
    // Returns the full reading history for a specific sensor
    // ════════════════════════════════════════════════════════════════════════
    @GET
    public Response getReadings() {
 
        // ── Step 1: Check that the sensor actually exists ────────────────────
        Sensor sensor = sensorStore.get(sensorId);
 
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Sensor with ID '" + sensorId + "' does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build(); // HTTP 404
        }
 
        // ── Step 2: Get the readings list for this sensor ────────────────────
        // If no readings have been posted yet, return an empty list (not an error)
        List<SensorReading> readings = readingsStore.getOrDefault(sensorId, new ArrayList<>());
 
        // ── Step 3: Build a descriptive response ─────────────────────────────
        Map<String, Object> response = new HashMap<>();
        response.put("sensorId",     sensorId);
        response.put("totalReadings", readings.size());
        response.put("currentValue",  sensor.getCurrentValue()); // latest value on the sensor
        response.put("readings",      readings);
 
        return Response.ok(response).build(); // HTTP 200
    }
 
 
    // ════════════════════════════════════════════════════════════════════════
    // PART 4.2 — POST /api/v1/sensors/{sensorId}/readings
    // Adds a new reading and updates the parent sensor's currentValue
    // ════════════════════════════════════════════════════════════════════════
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
 
        // ── Step 1: Check that the sensor exists ─────────────────────────────
        Sensor sensor = sensorStore.get(sensorId);
 
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Sensor with ID '" + sensorId + "' does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build(); // HTTP 404
        }
 
        // ── Step 2: Validate the request body ───────────────────────────────
        if (reading == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Request body is missing or malformed.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build(); // HTTP 400
        }
 
        // Reading ID is required
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Field 'id' is required for a reading.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build(); // HTTP 400
        }
 
        // ── Step 3: Set the timestamp automatically if not provided ──────────
        // System.currentTimeMillis() gives the current time as epoch milliseconds
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }
 
        // ── Step 4: Store the reading ────────────────────────────────────────
        // If this is the first reading for this sensor, create a new empty list
        readingsStore.putIfAbsent(sensorId, new ArrayList<>());
        readingsStore.get(sensorId).add(reading);
 
        // ── Step 5: SIDE EFFECT — Update the parent sensor's currentValue ────
        // This is the key requirement from Part 4.2:
        // After every new reading, we update the sensor so that
        // GET /api/v1/sensors always shows the LATEST value.
        sensor.setCurrentValue(reading.getValue());
 
        // ── Step 6: Build a success response ─────────────────────────────────
        Map<String, Object> success = new HashMap<>();
        success.put("status",       "success");
        success.put("message",      "Reading added successfully for sensor '" + sensorId + "'.");
        success.put("sensorId",     sensorId);
        success.put("reading",      reading);
        success.put("currentValue", sensor.getCurrentValue()); // confirm the update
 
        return Response.status(Response.Status.CREATED).entity(success).build(); // HTTP 201
    }
}
