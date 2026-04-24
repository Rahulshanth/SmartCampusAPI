/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import com.smartcampus.resources.DiscoveryResource;
import com.smartcampus.resources.RoomResource;
import com.smartcampus.resources.SensorResource;

// ── PART 5 IMPORTS ───────────────────────────────────────────────────────────
import com.smartcampus.mappers.RoomNotEmptyExceptionMapper;
import com.smartcampus.mappers.LinkedResourceNotFoundExceptionMapper;
import com.smartcampus.mappers.SensorUnavailableExceptionMapper;
import com.smartcampus.mappers.GlobalExceptionMapper;
import com.smartcampus.filters.LoggingFilter;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Resources
        register(DiscoveryResource.class);
        register(RoomResource.class);
        register(SensorResource.class);
        register(JacksonFeature.class);

        // ── PART 5: Exception Mappers ────────────────────────────────────────
        register(RoomNotEmptyExceptionMapper.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
        register(SensorUnavailableExceptionMapper.class);
        register(GlobalExceptionMapper.class);

        // ── PART 5: Logging Filter ───────────────────────────────────────────
        register(LoggingFilter.class);
    }
}