package io.jasonyu.cambia.network;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class Lobby {
    private String code;
    private int players;
    private String[] otherIds;
    private String[] otherSocketIds;

    public Lobby(String code, int players, String[] otherIds, String[] otherSocketIds) {
        this.code = code;
        this.players = players;
        this.otherIds = otherIds;
        this.otherSocketIds = otherSocketIds;
    }

    public String getCode() { return this.code; }
    public String[] getOtherPlayers() { return this.otherIds; }
    public String[] getOtherSocketIds() { return this.otherSocketIds; }
}
