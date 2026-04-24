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

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.io.IOException;

public class Main {

    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig();

        // Resources
        rc.register(DiscoveryResource.class);
        rc.register(RoomResource.class);
        rc.register(SensorResource.class);
        rc.register(JacksonFeature.class);

        // ── PART 5: Exception Mappers ────────────────────────────────────────
        rc.register(RoomNotEmptyExceptionMapper.class);
        rc.register(LinkedResourceNotFoundExceptionMapper.class);
        rc.register(SensorUnavailableExceptionMapper.class);
        rc.register(GlobalExceptionMapper.class);

        // ── PART 5: Logging Filter ───────────────────────────────────────────
        rc.register(LoggingFilter.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("==============================================");
        System.out.println("  Smart Campus API is RUNNING!");
        System.out.println("  Visit: " + BASE_URI);
        System.out.println("  Press ENTER to stop the server...");
        System.out.println("==============================================");
        System.in.read();
        server.stop();
        System.out.println("Server stopped. Goodbye!");
    }
}