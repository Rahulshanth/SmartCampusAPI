/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.mappers;

import com.smartcampus.exceptions.LinkedResourceNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundExceptionMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {

        Map<String, Object> error = new HashMap<>();
        error.put("status",              "error");
        error.put("errorCode",           "LINKED_RESOURCE_NOT_FOUND");
        error.put("httpStatus",          422);
        error.put("message",             exception.getMissingResourceType() +
                                         " with ID '" + exception.getMissingResourceId() +
                                         "' does not exist. Cannot link sensor to a non-existent room.");
        error.put("missingResource",     exception.getMissingResourceType());
        error.put("missingResourceId",   exception.getMissingResourceId());
        error.put("resolution",          "Create the room first using POST /api/v1/rooms, then register the sensor.");

        // 422 Unprocessable Entity
        // Note: Response.Status does not have 422 as a named constant in older JAX-RS,
        // so we use the numeric code directly.
        return Response
                .status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
