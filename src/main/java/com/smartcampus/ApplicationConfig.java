/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;
 
import com.smartcampus.resources.DiscoveryResource;
import com.smartcampus.resources.RoomResource;
import com.smartcampus.resources.SensorResource;   // ← NEW IMPORT
 
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
 
import javax.ws.rs.ApplicationPath;
 
@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {
 
    public ApplicationConfig() {
        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);    // ← NEW LINE
        register(JacksonFeature.class);
    }
}