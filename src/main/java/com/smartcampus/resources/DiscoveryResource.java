/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/*
 * DiscoveryResource - Handles GET /api/v1
 *
 * This is your API's "welcome page" for developers.
 * When someone visits GET /api/v1, they get a JSON map showing:
 *   - What version this API is
 *   - Who to contact for help
 *   - What endpoints are available (and their URLs)
 *
 * This concept is called HATEOAS (Hypermedia As The Engine Of Application State).
 * Instead of forcing clients to guess or read separate docs,
 * the API itself tells clients where everything is.
 *
 * ANNOTATIONS EXPLAINED:
 *   @Path("/")       → This class handles requests to /api/v1/ (the root)
 *                      Remember: @ApplicationPath already added "/api/v1"
 *                      So @Path("/") here means: /api/v1 + / = /api/v1
 *
 *   @GET             → This method responds to HTTP GET requests
 *   @Produces(...)   → This method returns JSON (tells the client what format to expect)
 */
@Path("/")
public class DiscoveryResource {

    /*
     * GET /api/v1
     * Returns a JSON "discovery" document with API metadata and available links.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {

        // We build the response as a nested Map.
        // Jersey + Jackson will automatically convert this Map into JSON for us.

        // --- Links section (this is the HATEOAS part) ---
        // We tell clients: "here are the URL paths you can explore"
        Map<String, String> links = new HashMap<>();
        links.put("rooms",    "/api/v1/rooms");
        links.put("sensors",  "/api/v1/sensors");

        // --- Top-level response object ---
        Map<String, Object> response = new HashMap<>();
        response.put("apiVersion",     "1.0");
        response.put("description",    "Smart Campus Sensor & Room Management API");
        response.put("adminContact",   "smartcampus-admin@westminster.ac.uk");
        response.put("status",         "running");
        response.put("links",          links);  // embed the links map inside the response

        // Return HTTP 200 OK with the JSON body
        return Response.ok(response).build();
    }
}
