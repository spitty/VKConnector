package org.spitty.vkconnector.gson;

/**
 * Simple POJO. It represents JSON response
 *
 * @author spitty
 */
public class Response {

    private User[] response;
    private Error error;

    public User[] getResponse() {
        return response;
    }

    public Error getError() {
        return error;
    }
}
