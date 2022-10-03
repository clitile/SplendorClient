package nz.net;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import nz.proj.Config;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class SocketClient extends WebSocketClient {
    private static final SocketClient client;
    public boolean login = false;
    public boolean match = false;
    public double x;
    public double y;
    public int id;
    public String name;
    public String activity = "";
    public boolean isThis = false;
    public Random r;
    public boolean roomStop = false;
    public boolean info_corr = true;
    public boolean round_begin = false;
    public String playing = "";
    public ArrayList<String> players;
    public ArrayList<String> allP = new ArrayList<>();
    public boolean loss = false;
    public String winner = "";


    static {
        try {
            client = new SocketClient();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketClient() throws URISyntaxException {
        this(new URI("ws://%s:%s/websocket".formatted(Config.IP, Config.PORT)));
    }

    public static SocketClient getInstance() {
        return client;
    }

    private SocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        Bundle mess = bytes2Bundle(bytes);
        if (mess.getName().equals("login")) {
            login = true;
            FXGL.set("login", true);
            System.out.println("login" + FXGL.getb("login"));
        } else if (mess.getName().equals("matchFind")) {
            playing = mess.get("next");
            players = mess.get("players");
            allP.addAll(players);
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).equals(name)) {
                    players.remove(i);
                    break;
                }
            }
            match = true;
            id = mess.get("id");
            int seed = mess.get("seed");
            System.out.println(seed);
            r = FXGLMath.getRandom(seed);
            FXGL.set("playersNames", mess.get("players"));
            isThis = mess.get("next").equals(this.name);
            round_begin = isThis;
            System.out.println("myRound: " + isThis);
        } else if (mess.getName().equals("act")) {
            x = mess.get("x");
            y = mess.get("y");
            activity = mess.get("activity");
        } else if (mess.getName().equals("roundOver")) {
            x = mess.get("x");
            y = mess.get("y");
            playing = mess.get("next");
            isThis = mess.get("next").equals(this.name);
            activity = mess.get("activity");
        } else if (mess.getName().equals("roomStop")) {
            this.roomStop = true;
            id = 0;
            match = false;
            Config.MODE_SCENE.mode = 0;
        } else if (mess.getName().equals("false")) {
            info_corr = false;
        }
    }

    private Bundle bytes2Bundle(ByteBuffer bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes.array());
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Bundle) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        login = false;
        match = false;
    }

    @Override
    public void onError(Exception ex) {

    }

    public void send(Bundle bundle) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(bundle);
            this.send(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
