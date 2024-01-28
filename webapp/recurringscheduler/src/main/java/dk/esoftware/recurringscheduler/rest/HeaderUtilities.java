package dk.esoftware.recurringscheduler.rest;

import jakarta.ws.rs.core.HttpHeaders;

public class HeaderUtilities {

    static String getAuthorizationHeader(HttpHeaders headers) {
        final String headerString = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (headerString == null || headerString.isBlank()) {
            return null;
        }

        return headerString.replace("Bearer ", "");
    }

}
