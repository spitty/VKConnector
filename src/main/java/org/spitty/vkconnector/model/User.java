package org.spitty.vkconnector.model;

import com.google.gson.annotations.SerializedName;

/**
 * Simple POJO. It represents "error" parameter of JSON response
 *
 * @author spitty
 */
public class User {

    private int uid;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;

    public int getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "User{" + "uid=" + uid + ", first_name=" + firstName + ", last_name=" + lastName + '}';
    }
}
