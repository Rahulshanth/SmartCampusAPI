/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Logger writes the real error to the server console (for developers)
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {

        // ── Log the full error on the SERVER SIDE (developers can see this) ─
        // We use SEVERE level because this is an unexpected crash
        LOGGER.log(Level.SEVERE,
                   "Unexpected server error: " + exception.getMessage(),
                   exception);  // This logs the full stack trace to the server console

        // ── Build a SAFE, GENERIC response for the client ────────────────────
        // Client sees NO stack trace, NO class names, NO internal details
        Map<String, Object> error = new HashMap<>();
        error.put("status",     "error");
        error.put("errorCode",  "INTERNAL_SERVER_ERROR");
        error.put("httpStatus", 500);
        error.put("message",    "An unexpected error occurred on the server. " +
                                "Please contact the system administrator.");
        error.put("resolution", "If this problem persists, contact smartcampus-admin@westminster.ac.uk");

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)   // HTTP 500
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
