/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.smartcampus.resources;
 
import com.smartcampus.models.Sensor;
import com.smartcampus.models.Room;
 
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {
 
    // ── In-memory sensor store ───────────────────────────────────────────────
    // Key   = Sensor ID (e.g., "SENS-001")
    // Value = Sensor object
    // "static" ensures all requests share the same data store
    private static final Map<String, Sensor> sensorStore = new HashMap<>();
 
 
    // ════════════════════════════════════════════════════════════════════════
    // PART 4.1 — SUB-RESOURCE LOCATOR
    // ════════════════════════════════════════════════════════════════════════
    //
    // This method is a "Sub-Resource Locator".
    // It does NOT handle the request itself.
    // Instead, it passes control to a dedicated SensorReadingResource class.
    //
    // URL pattern handled: /api/v1/sensors/{sensorId}/readings
    //
    // IMPORTANT: There is NO @GET / @POST / @PUT here — only @Path.
    // JAX-RS sees the @Path and knows to delegate further processing
    // to whatever object this method returns.
    // ════════════════════════════════════════════════════════════════════════
    @Path("{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
 
        // We pass the sensorId AND the sensorStore to SensorReadingResource
        // so it can:
        //   1. Look up the sensor to validate it exists
        //   2. Update the sensor's currentValue after a new reading is posted
        return new SensorReadingResource(sensorId, sensorStore);
    }
 
 
    // ════════════════════════════════════════════════════════════════════════
    // EXISTING METHODS (unchanged from Part 3)
    // ════════════════════════════════════════════════════════════════════════
 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
 
        // ── Validation 1: Body must not be null ──────────────────────────────
        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Request body is missing or malformed.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
 
        // ── Validation 2: id and type are required ───────────────────────────
        if (sensor.getId()   == null || sensor.getId().trim().isEmpty() ||
            sensor.getType() == null || sensor.getType().trim().isEmpty()) {
 
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Fields 'id' and 'type' are required and cannot be blank.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
 
        // ── Validation 3: roomId must be provided ────────────────────────────
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Field 'roomId' is required. A sensor must belong to a room.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
 
        // ── Validation 4: roomId must EXIST in the room store (Business Rule) ─
        Room targetRoom = RoomResource.roomStore.get(sensor.getRoomId());
 
        if (targetRoom == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Room with ID '" + sensor.getRoomId() +
                                 "' does not exist. Please create the room first.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
 
        // ── Validation 5: Prevent duplicate sensor IDs ───────────────────────
        if (sensorStore.containsKey(sensor.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "A sensor with ID '" + sensor.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
 
        // ── All validations passed ───────────────────────────────────────────
 
        // Set a default status if the client did not provide one
        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("active");
        }
 
        // Save the sensor in the sensor store
        sensorStore.put(sensor.getId(), sensor);
 
        // Link sensor ID to the room
        targetRoom.getSensorIds().add(sensor.getId());
 
        // Build a success response
        Map<String, Object> success = new HashMap<>();
        success.put("status",  "success");
        success.put("message", "Sensor registered and linked to room '" + sensor.getRoomId() + "' successfully.");
        success.put("sensor",  sensor);
 
        return Response.status(Response.Status.CREATED).entity(success).build(); // HTTP 201
    }
 
 
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
 
        List<Sensor> allSensors = new ArrayList<>(sensorStore.values());
 
        // ── No filter provided → return everything ───────────────────────────
        if (type == null || type.trim().isEmpty()) {
            return Response.ok(allSensors).build(); // HTTP 200
        }
 
        // ── Filter provided → return only matching sensors ───────────────────
        List<Sensor> filtered = new ArrayList<>();
        for (Sensor s : allSensors) {
            if (s.getType() != null && s.getType().equalsIgnoreCase(type.trim())) {
                filtered.add(s);
            }
        }
 
        Map<String, Object> response = new HashMap<>();
        response.put("filterApplied", type);
        response.put("count",         filtered.size());
        response.put("sensors",       filtered);
 
        return Response.ok(response).build(); // HTTP 200
    }
}