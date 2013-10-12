package org.spitty.vkconnector.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author spitty
 */
public class VKAuth {

    private static final Logger LOGGER = LoggerFactory.getLogger(VKAuth.class);
    public static final String DEFAULT_REDIRECT_URL = "http://oauth.vk.com/blank.html";
    private String clientID;
    private String email;
    private String password;
    private String redirectURL = DEFAULT_REDIRECT_URL;
    private String accessToken;
    private String userID;
    private boolean authSuccessful = false;
    private boolean authProcessed = false;

    public VKAuth(String clientID, String email, String password) {
        this.clientID = clientID;
        this.email = email;
        this.password = password;
        authProcessed = false;
        authSuccessful = false;
        accessToken = null;
    }

    /**
     * This constructor takes {@code access_token} and {@code user_id} which can be specified without using plain text
     * credentials.<br/>
     * You can obtain accessToken by yourself with this link:
     * http://oauth.vk.com/oauth/authorize?client_id=3913198&redirect_uri=http://oauth.vk.com/blank.html&scope=wall,friends&display=page&response_type=token
     *
     * @param accessToken access_token
     * @param userID      user_id
     */
    public VKAuth(String accessToken, String userID) {
        this.accessToken = accessToken;
        this.userID = userID;
        authSuccessful = true;
        authProcessed = true;
    }

    public String getClientID() {
        return clientID;
    }

    /**
     * Used in requests to VK API methods
     *
     * @return {@link String} access_token
     */
    public String getAccessToken() {
        checkAuthSuccess();
        return accessToken;
    }

    public String getUserID() {
        checkAuthSuccess();
        return userID;
    }

    public boolean isAuthSuccessful() {
        return authSuccessful;
    }

    private String getEmail() {
        return email;
    }

    private String getPassword() {
        return password;
    }

    public void authenticate() {
        try {
            Map<String, String> params = new HashMap<>();
            if (getClientID() == null || getPassword() == null || getEmail() == null) {
                throw new IllegalStateException("Please specify credential");
            }

            params.put("client_id", getClientID());
            params.put("redirect_uri", redirectURL == null ? DEFAULT_REDIRECT_URL : redirectURL);
            params.put("scope", "wall,friends");
            params.put("display", "page");
            params.put("response_type", "token");
            LOGGER.debug("Try to obtain page by next params: {}", params);
            Connection getLoginPageRequest = Jsoup.connect("http://oauth.vk.com/oauth/authorize");
            getLoginPageRequest.data(params);
            Document doc = getLoginPageRequest.get();

            LOGGER.trace("Login page (raw HTML): {}", doc);
            // We want to find <form> element on the page. BTW we know it is the only one
            Element form = doc.select("form").first();
            // Action filed of <form> tag contains URL of target page
            String action = form.attr("action");
            LOGGER.debug("Action: " + action);
            // Child <input> elements of our form represent parameters we have to send

            // Get all parameters which will be sent (only ones with not empty "name" attribute)
            Elements inputs = doc.select("input[name]");
            Map<String, String> paramsToSend = new HashMap<>();
            for (Element e : inputs) {
                paramsToSend.put(e.attr("name"), e.attr("value"));
                LOGGER.debug("{} = \"{}\"", e.attr("name"), e.attr("value"));
            }
            LOGGER.debug("Next parameters will be sent: {}", paramsToSend);
            // Set values for "email" and "pass"
            paramsToSend.put("email", email);
            paramsToSend.put("pass", password);

            // We use Connection.Response here because we want to obtain URL parameters
            Connection.Response execute = Jsoup.connect(action)
                    .data(paramsToSend)
                    .execute();
            String url = execute.url().toString();
            // Check if we are authenticated
            if (!url.contains("access_token")) {
                LOGGER.warn("\"access_token\" absents. Probably credentials are wrong");
                LOGGER.trace("Response body: {}", execute.body());
                Document errorLoginPage = execute.parse();
                String warnMessage = errorLoginPage.select("div.service_msg_warning").text();
                authProcessed = true;
                authSuccessful = false;
                throw new IllegalArgumentException("Authentification fails. Please check credentials. VK warn message is "
                        + "\"" + warnMessage + "\"");
            }
            // Parse URL parameters
            String urlParams = url.substring(url.indexOf("#") + 1);
            LOGGER.debug("URL params: {}", urlParams);
            Map<String, String> result = new HashMap<>();
            for (String keyValue : urlParams.split("&")) {
                String s[] = keyValue.split("=");
                if (s.length == 2) {
                    result.put(s[0], s[1]);
                }
            }
            LOGGER.debug("Parsed params: {}", result);

            // Get useful parameters: access_token and user_id
            accessToken = result.get("access_token");
            userID = result.get("user_id");
            LOGGER.debug("Access token: {}", accessToken);
            LOGGER.debug("User ID: {}", userID);
            authSuccessful = true;
        } catch (IOException ex) {
            LOGGER.error("Can't authenticate", ex);
            authSuccessful = false;
        }
        authProcessed = true;
    }

    private void checkAuthSuccess() throws IllegalStateException {
        if (!authProcessed) {
            authenticate();
        }
        if (!authSuccessful) {
            throw new IllegalStateException("Can't authenticate with specified credentials");
        }
    }
}
