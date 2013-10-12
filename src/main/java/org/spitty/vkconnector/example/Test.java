package org.spitty.vkconnector.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spitty.vkconnector.api.VKAuth;
import org.spitty.vkconnector.api.VKMethod;
import org.spitty.vkconnector.gson.GsonHelper;
import org.spitty.vkconnector.gson.Response;
import org.spitty.vkconnector.gson.User;

/**
 *
 * @author spitty
 */
public class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        VKAuth auth = new VKAuth("3913198", "", "");
        {
            VKMethod getUsers = new VKMethod("users.get", auth);
            String users = getUsers.addParam("uids", "1").addParam("uids", auth.getUserID()).execute();
            LOGGER.debug("Users respons in JSON: {}", users);
            Gson gson = new Gson();
            Response usersResult = gson.fromJson(users, Response.class);
            if (usersResult.getError() != null) {
                String errorMessage = usersResult.getError().getErrorMessage();
                LOGGER.error("Error has occured during executiong {}", getUsers, errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            System.out.println("Get inforation about next users:");
            System.out.println(Arrays.asList(usersResult.getResponse()).toString());
        }
        {
            VKMethod getFriends = new VKMethod("friends.get", auth);
            String friends = getFriends.addParam("uid", auth.getUserID()).addParam("fields", "first_name").addParam("fields", "last_name").execute();
            LOGGER.debug("Friends response in JSON: {}", friends);
            Response friendsResponse = GsonHelper.convertJsonToResponse(friends);
            if (friendsResponse.getError() != null) {
                String errorMessage = friendsResponse.getError().getErrorMessage();
                LOGGER.error("Error has occured during executiong {}", getFriends, errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            System.out.println("Friends:");
            User[] friendsArray = friendsResponse.getResponse();
            for (User u : friendsArray) {
                System.out.println(u);
            }
            LOGGER.debug("Total friends: " + friendsArray.length);
        }
    }
}
