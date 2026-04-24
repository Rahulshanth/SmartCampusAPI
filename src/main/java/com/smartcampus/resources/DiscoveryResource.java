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

@Path("/")
public class DiscoveryResource {

    /*
     * GET /api/v1
     * Returns a JSON "discovery" document with API metadata and available links.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        
        // TEMPORARY TEST LINE — remove after recording
        //if (true) throw new NullPointerException("Simulated crash for testing");

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
