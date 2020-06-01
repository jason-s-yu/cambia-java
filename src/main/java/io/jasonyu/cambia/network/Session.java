package io.jasonyu.cambia.network;

import okhttp3.MediaType;
import okhttp3.*;

import java.io.IOException;
import java.net.*;

public class Session {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String email;
    private String password;
    private String token;
    private CookieManager cm;

    public Session(String email, String password) {
        this.email = email;
        this.password = password;

        CookieManager cm = new CookieManager();
        CookieManager.setDefault(cm);
        this.cm = cm;
    }

    public boolean login() throws ProtocolException, IOException {
        OkHttpClient client = new OkHttpClient();

        String json = "{\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";

        RequestBody body = RequestBody.create(json, JSON);

        Request login = new Request.Builder().url("http://cambia.jasonyu.io/api/login").post(body).build();
        try (Response response = client.newCall(login).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Successfully logged in.");
                this.token = response.body().string();
                return true;
            } else {
                throw new Error(response.body().string());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public String getToken() {
        return this.token;
    }
}
