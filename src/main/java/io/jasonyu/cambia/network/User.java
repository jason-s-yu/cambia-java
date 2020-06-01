package io.jasonyu.cambia.network;

import io.jasonyu.cambia.controllers.DashboardController;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class User {
    private String email;
    private String password;
    private String username;
    private Session session;
    private Socket connection;
    private Lobby currentLobby;
    private boolean inLobby;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String create() {
        OkHttpClient client = new OkHttpClient();

        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"username\":\"" + username + "\"}";

        RequestBody body = RequestBody.create(json, Session.JSON);

        Request create = new Request.Builder().url("http://cambia.jasonyu.io/api/user/create").post(body).build();
        try (Response response = client.newCall(create).execute()) {
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
                return "success";
            } else {
                return response.body().string();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "fail";
    }

    public boolean createAndLogin() throws IOException {
        if (this.create().equalsIgnoreCase("success")) {
            this.session = new Session(this.email, this.password);
            return this.session.login();
        }
        return false;
    }

    public boolean login() throws IOException, URISyntaxException {
        this.session = new Session(this.email, this.password);
        boolean success = this.session.login();
        if (success) {
            // create connection to socket
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.query = "token=" + session.getToken();
            Socket socket = IO.socket("http://cambia.jasonyu.io", opts);
            this.connection = socket;
            socket.connect();
        }
        return success;
    }

    public void connect() {
        this.connection.connect();
    }

    public void disconnect() {
        this.connection.disconnect();
    }

    public Socket getSocket() { return this.connection; }

    public boolean findMatch(DashboardController controller) {
        this.connection.emit("looking for match")
                .on("match found", (args) -> {
                    System.out.println("Match found!");
                    JSONObject response = (JSONObject) args[0];
                    Lobby lobby = new Lobby((String) response.get("code"), 2, new String[]{(String) response.get("otherPlayer")}, new String[]{(String) response.get("otherSocketId")});
                    this.currentLobby = lobby;

                    if (lobby != null) {
                        controller.matchFound();
                    }
                }).on("verify match", (args) -> {
                    System.out.println("Both accepted. Starting match.");
                    this.inLobby = true;
                    this.connection.disconnect();
                    return;
        });
        return this.inLobby;
    }

    public void setLobby(Lobby lobby) {
        this.currentLobby = lobby;
    }

    public Lobby getCurrentLobby() {
        return this.currentLobby;
    }

    public void leaveQueue() {
        this.connection.emit("leave queue").off();
    }
}
