/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.smartcampus.resources;

import com.smartcampus.models.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * RoomResource - Handles all requests under /api/v1/rooms
 *
 * This class manages the full lifecycle of a Room:
 *   GET    /api/v1/rooms            → list all rooms
 *   POST   /api/v1/rooms            → create a new room
 *   GET    /api/v1/rooms/{roomId}   → get one specific room
 *   DELETE /api/v1/rooms/{roomId}   → delete a room (only if no sensors assigned)
 *
 * IN-MEMORY STORAGE EXPLAINED:
 *   We use a static HashMap called "roomStore" to hold all rooms.
 *   "static" is CRITICAL here — JAX-RS creates a brand new RoomResource
 *   object for EVERY incoming request. Without "static", the map would
 *   be wiped clean on every request and you'd lose all your data!
 *   Making it static means all requests share the exact same map.
 *
 * ANNOTATIONS EXPLAINED:
 *   @Path("/rooms")    → All methods in this class live under /api/v1/rooms
 *   @Produces(...)     → All responses from this class are JSON by default
 *   @Consumes(...)     → This class expects incoming request bodies in JSON
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // ── In-memory data store ─────────────────────────────────────────────────
    // Key   = Room ID (e.g., "LIB-301")
    // Value = Room object
    private static final Map<String, Room> roomStore = new HashMap<>();

    // ── Sample data loaded once when the server starts ───────────────────────
    // This gives you something to see immediately when you test GET /rooms
    static {
        Room sample = new Room("LIB-301", "Library Quiet Study", 50);
        roomStore.put(sample.getId(), sample);
    }


    // =========================================================================
    // PART 2.1 — GET /api/v1/rooms
    // Returns a list of ALL rooms currently in memory
    // =========================================================================
    /*
     * getAllRooms()
     *
     * Fetches every room stored in roomStore and returns them as a JSON array.
     * We copy the map's values into an ArrayList so Jackson can serialize
     * them into a proper JSON array format.
     *
     * HTTP 200 OK → always returned (an empty list is still a valid response)
     */
    @GET
    public Response getAllRooms() {

        List<Room> rooms = new ArrayList<>(roomStore.values());

        return Response.ok(rooms).build(); // HTTP 200
    }


    // =========================================================================
    // PART 2.1 — GET /api/v1/rooms/{roomId}
    // Returns ONE specific room by its ID
    // =========================================================================
    /*
     * getRoomById(@PathParam roomId)
     *
     * @PathParam "roomId" pulls the value straight from the URL.
     * Example: GET /api/v1/rooms/LIB-301 → roomId = "LIB-301"
     *
     * HTTP 200 OK       → room found, returned in response body
     * HTTP 404 Not Found → no room with that ID exists
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {

        Room room = roomStore.get(roomId);

        // Room does not exist → return 404 with a helpful error message
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Room with ID '" + roomId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.ok(room).build(); // HTTP 200
    }


    // =========================================================================
    // PART 2.1 — POST /api/v1/rooms
    // Creates a new room and saves it to the in-memory store
    // =========================================================================
    /*
     * createRoom(Room room)
     *
     * JAX-RS + Jackson automatically converts the incoming JSON body
     * into a Room object for us — that is what the "Room room" parameter does.
     *
     * We validate three things before saving:
     *   1. The request body must not be empty/null
     *   2. The "id" and "name" fields are required
     *   3. The ID must not already exist (no duplicates allowed)
     *
     * HTTP 201 Created  → room successfully created
     * HTTP 400 Bad Request → missing required fields
     * HTTP 409 Conflict    → a room with that ID already exists
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

        // Validation 1: Check request body is not empty
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Request body is missing or malformed.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        // Validation 2: id and name are required fields
        if (room.getId() == null || room.getId().trim().isEmpty() ||
            room.getName() == null || room.getName().trim().isEmpty()) {

            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Fields 'id' and 'name' are required and cannot be blank.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        // Validation 3: Prevent duplicate room IDs
        if (roomStore.containsKey(room.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "A room with ID '" + room.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        // All validations passed → save the room
        roomStore.put(room.getId(), room);

        // Build a success response including the newly created room object
        Map<String, Object> success = new HashMap<>();
        success.put("status",  "success");
        success.put("message", "Room created successfully.");
        success.put("room",    room);

        return Response.status(Response.Status.CREATED).entity(success).build(); // HTTP 201
    }


    // =========================================================================
    // PART 2.2 — DELETE /api/v1/rooms/{roomId}
    // Deletes a room ONLY if it has no sensors assigned to it
    // =========================================================================
    /*
     * deleteRoom(@PathParam roomId)
     *
     * This method enforces a key business rule:
     *   A room CANNOT be deleted if it still has sensors assigned.
     *   This prevents "orphaned" sensors — sensors with no valid room parent.
     *
     * The check is simple: if the room's sensorIds list is not empty, we block
     * the deletion and return HTTP 409 Conflict with a clear explanation.
     *
     * HTTP 200 OK       → room deleted successfully
     * HTTP 404 Not Found → no room with that ID exists
     * HTTP 409 Conflict  → room still has sensors assigned, deletion blocked
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {

        Room room = roomStore.get(roomId);

        // Check 1: Room must exist before we try to delete it
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Room with ID '" + roomId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        // Check 2: Block deletion if sensors are still assigned (Business Rule)
        if (!room.getSensorIds().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status",          "error");
            error.put("message",         "Cannot delete room '" + roomId + "'. " +
                                         "It still has " + room.getSensorIds().size() +
                                         " sensor(s) assigned. Remove all sensors first.");
            error.put("assignedSensors", room.getSensorIds());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        // All checks passed → remove the room from memory
        roomStore.remove(roomId);

        Map<String, String> success = new HashMap<>();
        success.put("status",  "success");
        success.put("message", "Room '" + roomId + "' has been successfully deleted.");

        return Response.ok(success).build(); // HTTP 200
    }
}