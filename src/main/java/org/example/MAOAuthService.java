package org.example;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.util.Map;

public class MAOAuthService {

    private static String accessToken;
    private static long tokenCreatedTime;

    // 2 saat = 7200000 ms
    private static final long TOKEN_VALIDITY =
            2 * 60 * 60 * 1000;

    public static String getAccessToken() throws Exception {

        if (accessToken != null &&
                System.currentTimeMillis() - tokenCreatedTime < TOKEN_VALIDITY) {

            return accessToken;
        }


        accessToken = generateToken();
        tokenCreatedTime = System.currentTimeMillis();

        return accessToken;
    }

    private static String generateToken() throws Exception {

        try (Playwright playwright = Playwright.create()) {

            APIRequestContext requestContext =
                    playwright.request().newContext();
            String clientId = "Postman_Testing";
            String clientSecret = "EY/sUEbxu7ZK}45N";

            String credentials =
                    Base64.getEncoder().encodeToString(
                            (clientId + ":" + clientSecret)
                                    .getBytes(StandardCharsets.UTF_8)
                    );
            APIResponse response = requestContext.post(
                    "https://upgts-auth.omni.manh.com/oauth/token",
                    RequestOptions.create()
                            .setHeader(
                                    "Authorization",
                                    "Basic " + credentials
                            )
                            .setForm(
                                    FormData.create()
                                            .set("grant_type", "password")
                                            .set("username", "aliyildiz_penti-turkey")
                                            .set("password", "Penti1995.tr346789")
                            )
            );


            ObjectMapper mapper = new ObjectMapper();
            System.out.println("STATUS = " + response.status());
            System.out.println("BODY = " + response.text());
            JsonNode json =
                    mapper.readTree(response.text());

            return json.get("access_token").asText();
        }
    }
}
