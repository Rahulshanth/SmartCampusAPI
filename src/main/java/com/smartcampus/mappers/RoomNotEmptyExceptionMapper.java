/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.smartcampus.mappers;

import com.smartcampus.exceptions.RoomNotEmptyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;


@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {

        // Build a clean JSON error body
        Map<String, Object> error = new HashMap<>();
        error.put("status",          "error");
        error.put("errorCode",       "ROOM_NOT_EMPTY");
        error.put("httpStatus",      409);
        error.put("message",         "Cannot delete room '" + exception.getRoomId() +
                                     "'. It is currently occupied by active hardware. " +
                                     "Please remove all sensors from this room before deleting it.");
        error.put("assignedSensors", exception.getSensorIds());
        error.put("resolution",      "DELETE each sensor first, then retry deleting the room.");

        return Response
                .status(Response.Status.CONFLICT)   // HTTP 409
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
