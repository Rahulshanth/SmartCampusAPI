/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exceptions;

public class LinkedResourceNotFoundException extends RuntimeException {

    private final String missingResourceType; // e.g., "Room"
    private final String missingResourceId;   // e.g., "LIB-999"

    public LinkedResourceNotFoundException(String missingResourceType, String missingResourceId) {
        super(missingResourceType + " with ID '" + missingResourceId + "' was not found.");
        this.missingResourceType = missingResourceType;
        this.missingResourceId   = missingResourceId;
    }

    public String getMissingResourceType() { return missingResourceType; }
    public String getMissingResourceId()   { return missingResourceId; }
}
