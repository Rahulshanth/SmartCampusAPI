/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;


@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // One logger for this whole class
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /*
     * filter(ContainerRequestContext)
     *
     * This method runs automatically BEFORE every single request
     * reaches any resource method (RoomResource, SensorResource, etc.)
     *
     * We log:
     *   - HTTP method  (GET, POST, DELETE, etc.)
     *   - Full request URI  (e.g., http://localhost:8080/api/v1/rooms)
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String method = requestContext.getMethod();
        String uri    = requestContext.getUriInfo().getRequestUri().toString();

        LOGGER.info(">>> INCOMING REQUEST  | Method: " + method + " | URI: " + uri);
    }

    /*
     * filter(ContainerRequestContext, ContainerResponseContext)
     *
     * This method runs automatically AFTER every response is ready
     * to be sent back to the client.
     *
     * We log:
     *   - HTTP status code  (e.g., 200, 201, 404, 409)
     */
    @Override
    public void filter(ContainerRequestContext  requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        int statusCode = responseContext.getStatus();

        LOGGER.info("<<< OUTGOING RESPONSE | Status: " + statusCode);
    }
}
