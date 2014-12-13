package ru.spitty.vkconnector;

import com.google.gson.Gson;
import org.spitty.vkconnector.api.VKAuth;
import org.spitty.vkconnector.api.VKMethod;
import org.spitty.vkconnector.model.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

@Test
public class AppTest {

    @DataProvider(name = "accessTokenProvider")
    public Object[][] getAccessToken() {
        String accessToken = System.getProperty("accessToken");
        String userId = System.getProperty("userId");

        System.out.println(String.format("User ID:\t%s\nAccess token:\t%s", accessToken, userId));
        return new Object[][]{{accessToken, userId}};
    }

    @Test(dataProvider = "accessTokenProvider")
    public static void getUserById(String accessToken, String userId) throws IOException {
        VKAuth auth = new VKAuth(accessToken, userId);
        VKMethod getUsers = new VKMethod("users.get", auth);
        String users = getUsers
                .addParam("uids", "1")
                .addParam("uids", auth.getUserID())
                .execute();
        System.out.println(String.format("Users response in JSON: %s", users));
        Gson gson = new Gson();
        Response usersResult = gson.fromJson(users, Response.class);
        if (usersResult.getError() != null) {
            String errorMessage = usersResult.getError().getErrorMessage();
            System.err.println(String.format("Error has occurred during execution %s", getUsers));
            Assert.fail(String.format("Error has occurred during execution %s. Error message: %s", getUsers, errorMessage));
        }
        System.out.println("Get information about next users:");
        System.out.println(Arrays.asList(usersResult.getResponse()).toString());
    }
}
