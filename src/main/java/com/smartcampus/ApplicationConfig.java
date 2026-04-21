/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import com.smartcampus.resources.DiscoveryResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.resources.RoomResource;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Manually register all resource classes here
        register(DiscoveryResource.class);
        register(JacksonFeature.class);
        register(DiscoveryResource.class);
        register(RoomResource.class);       // ← ADD THIS LINE
        register(JacksonFeature.class);
    }
}