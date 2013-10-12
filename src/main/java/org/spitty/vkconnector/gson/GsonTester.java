package org.spitty.vkconnector.gson;

import com.google.gson.Gson;

/**
 *
 * @author spitty
 */
public class GsonTester {

    public static void main(String[] args) {
        String jsonResponse = "{\"response\":[{\"uid\":1,\"first_name\":\"Павел\",\"last_name\":\"Дуров\"},{\"uid\":11508656,\"first_name\":\"Дарья\",\"last_name\":\"Беляева\"}]}";
        Gson gson = new Gson();
        Response res = gson.fromJson(jsonResponse, Response.class);
        for (User u : res.getResponse()) {
            System.out.println(u);
        }
    }
}
