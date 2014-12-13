package org.spitty.vkconnector.util;

import com.google.gson.Gson;
import org.spitty.vkconnector.model.Response;

/**
 * Utility class containing static methods to converting {@link String} JSON in
 * Java {@link Object Objects}
 */
public class JsonUtils {

    private final static Gson GSON = new Gson();

    /**
     * Convert input JSON into {@link org.spitty.vkconnector.model.Response}
     *
     * @param stringResponse JSON {@link String}
     * @return {@link org.spitty.vkconnector.model.Response} corresponding input JSON
     */
    public static Response convertJsonToResponse(String stringResponse) {
        return GSON.fromJson(stringResponse, Response.class);
    }
}
