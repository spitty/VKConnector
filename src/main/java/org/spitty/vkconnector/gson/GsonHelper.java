package org.spitty.vkconnector.gson;

import com.google.gson.Gson;

/**
 * Utility class containing static methods to converting {@link String} JSON in
 * Java {@link Object Objects}
 */
public class GsonHelper {

    private final static Gson GSON = new Gson();

    /**
     * Convert input JSON into {@link Response}
     *
     * @param stringResponse JSON {@link String}
     * @return {@link Response} corresponding input JSON
     */
    public static Response convertJsonToResponse(String stringResponse) {
        return GSON.fromJson(stringResponse, Response.class);
    }
}
