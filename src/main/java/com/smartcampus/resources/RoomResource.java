/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * RoomResource.java  ── UPDATED FOR PART 3
 *
 * ONE CHANGE from Part 2:
 *   "private static" → "public static" on roomStore
 *
 *   WHY? SensorResource needs to look up rooms when a sensor is being
 *   registered (to validate the roomId). Making roomStore public allows
 *   SensorResource to access it directly: RoomResource.roomStore.get(roomId)
 *   This is the simplest way to share in-memory data without a database.
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
import com.smartcampus.exceptions.RoomNotEmptyException;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomResource {

    public static final Map<String, Room> roomStore = new HashMap<>();

    // ── Sample data loaded once when the server starts ───────────────────────
    static {
        Room sample = new Room("LIB-301", "Library Quiet Study", 50);
        roomStore.put(sample.getId(), sample);
    }

    // =========================================================================
    // PART 2.1 — GET /api/v1/rooms
    // =========================================================================
    @GET
    public Response getAllRooms() {
        List<Room> rooms = new ArrayList<>(roomStore.values());
        return Response.ok(rooms).build();
    }

    // ===========================
    // PART 2.1 — GET /api/v1/rooms/{roomId}
    // ============================
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {

        Room room = roomStore.get(roomId);

        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Room with ID '" + roomId + "' was not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        return Response.ok(room).build();
    }

    // =========================
    // PART 2.1 — POST /api/v1/rooms
    // ===========================
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Request body is missing or malformed.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (room.getId() == null || room.getId().trim().isEmpty() ||
            room.getName() == null || room.getName().trim().isEmpty()) {

            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "Fields 'id' and 'name' are required and cannot be blank.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (roomStore.containsKey(room.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("status",  "error");
            error.put("message", "A room with ID '" + room.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }

        roomStore.put(room.getId(), room);

        Map<String, Object> success = new HashMap<>();
        success.put("status",  "success");
        success.put("message", "Room created successfully.");
        success.put("room",    room);

        return Response.status(Response.Status.CREATED).entity(success).build();
    }


    // =========================
    // PART 2.2 — DELETE /api/v1/rooms/{roomId}
    // =========================
    @DELETE
@Path("/{roomId}")
public Response deleteRoom(@PathParam("roomId") String roomId) {

    Room room = roomStore.get(roomId);

    // Room not found → 404
    if (room == null) {
        Map<String, String> error = new HashMap<>();
        error.put("status",  "error");
        error.put("message", "Room with ID '" + roomId + "' was not found.");
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

    // ── PART 5.1: Room has sensors → throw RoomNotEmptyException ────────────
    // Instead of manually building a response here,
    // we THROW our custom exception.
    // JAX-RS will catch it and send it to RoomNotEmptyExceptionMapper automatically.
    if (!room.getSensorIds().isEmpty()) {
        throw new RoomNotEmptyException(roomId, room.getSensorIds());
    }

    // All clear → delete the room
    roomStore.remove(roomId);

    Map<String, String> success = new HashMap<>();
    success.put("status",  "success");
    success.put("message", "Room '" + roomId + "' has been successfully deleted.");
    return Response.ok(success).build();
}
}