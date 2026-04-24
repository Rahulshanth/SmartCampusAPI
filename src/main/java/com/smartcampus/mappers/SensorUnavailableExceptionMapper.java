/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.mappers;

import com.smartcampus.exceptions.SensorUnavailableException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class SensorUnavailableExceptionMapper
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {

        Map<String, Object> error = new HashMap<>();
        error.put("status",        "error");
        error.put("errorCode",     "SENSOR_UNAVAILABLE");
        error.put("httpStatus",    403);
        error.put("message",       "Sensor '" + exception.getSensorId() +
                                   "' is currently in '" + exception.getCurrentStatus() +
                                   "' mode. It is physically disconnected and cannot accept new readings.");
        error.put("sensorId",      exception.getSensorId());
        error.put("currentStatus", exception.getCurrentStatus());
        error.put("resolution",    "Change sensor status to 'ACTIVE' before posting new readings.");

        return Response
                .status(Response.Status.FORBIDDEN)   // HTTP 403
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}