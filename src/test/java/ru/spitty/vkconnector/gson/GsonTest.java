package ru.spitty.vkconnector.gson;

import com.google.gson.Gson;
import org.spitty.vkconnector.model.Response;
import org.spitty.vkconnector.model.User;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class GsonTest {

    public static void testGson() {

        String jsonResponse = "{\"response\":[{\"uid\":1,\"first_name\":\"Павел\",\"last_name\":\"Дуров\"},{\"uid\":11508656,\"first_name\":\"Дарья\",\"last_name\":\"Беляева\"}]}";
        Gson gson = new Gson();
        Response res = gson.fromJson(jsonResponse, Response.class);

        Assert.assertEquals(res.getResponse().length, 2);

        User user1 = res.getResponse()[0];
        Assert.assertEquals(user1.getUid(), 1);
        Assert.assertEquals(user1.getFirstName(), "Павел");
        Assert.assertEquals(user1.getLastName(), "Дуров");

        User user2 = res.getResponse()[1];
        Assert.assertEquals(user2.getUid(), 11508656);
        Assert.assertEquals(user2.getFirstName(), "Дарья");
        Assert.assertEquals(user2.getLastName(), "Беляева");

        for (User u : res.getResponse()) {
            System.out.println(u);
        }
    }
}
