package org.spitty.vkconnector.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 *
 */
public class SingleMethodApp {

    public static void main(String[] args) throws IOException {
        String client_id = "3913198";
        String redirect_uri = "http://oauth.vk.com/blank.html";
        String scope = "wall,friends";
        String email = ""; //Specify email
        String password = ""; //Specify password
        Map<String, String> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("redirect_uri", redirect_uri);
        params.put("scope", scope);
        params.put("display", "page");
        params.put("response_type", "token");

        Document doc = Jsoup.connect("http://oauth.vk.com/oauth/authorize")
                .data(params)
                .get();
        
        System.out.println("::" + doc);
        Element form = doc.select("form").first();
        // URL to execute request
        String action = form.attr("action");
        // Set required parameters
        form.select("input[name=email]").attr("value", email);
        form.select("input[name=pass]").attr("value", password);
        Elements inputs = doc.select("input[name]");
        Map<String, String> npars = new HashMap<>();

        for (Element e : inputs) {
            String varName = e.attr("name");
            System.out.println(varName + " = " + e.attr("value"));
            npars.put(varName, e.attr("value"));
        }
        System.out.println("Action: " + action);
        Connection.Response execute = Jsoup.connect(action)
                .data(npars)
                .execute();
        System.out.println("::::" + execute.headers());
        System.out.println("::URL::" + execute.url());
        String url = execute.url().toString();
        url = url.substring(url.indexOf("#") + 1);
        System.out.println(": " + url);
        Map<String, String> result = new HashMap<>();
        for (String keyValue : url.split("&")) {
            String s[] = keyValue.split("=");
            if (s.length == 2) {
                result.put(s[0], s[1]);
            }
        }
        System.out.println("res: " + result);
        Connection.Response userInfo = Jsoup.connect("https://api.vk.com/method/users.get?uids=1,11508656," + result.get("user_id"))
                .data("access_token", result.get("access_token"))
                .ignoreContentType(true)
                .execute();
        System.out.println("userInfo: " + userInfo.body());
    }
}
