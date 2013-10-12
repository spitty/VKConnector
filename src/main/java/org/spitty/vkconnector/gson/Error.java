package org.spitty.vkconnector.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Simple POJO. It represents "error" parameter of JSON response
 *
 * @author spitty
 */
public class Error {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("error_msg")
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
