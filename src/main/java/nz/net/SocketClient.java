package nz.net;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class SocketClient extends WebSocketClient {
    private static final SocketClient client;
    public boolean login = false;
    public boolean match = false;
    public double x;
    public double y;
    public int id;
    public String name;
    public String activity;
    public boolean isThis = false;

    static {
        try {
            client = new SocketClient();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketClient() throws URISyntaxException {
        this(new URI("ws://192.168.3.216:10100/websocket"));
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
            match = true;
            id = mess.get("id");
            FXGL.set("playersNames", mess.get("players"));
            isThis = mess.get("next").equals(this.name);
        } else if (mess.getName().equals("act")) {
            x = mess.get("x");
            y = mess.get("y");
            activity = mess.get("activity");
        } else if (mess.getName().equals("roundOver")) {
            x = mess.get("x");
            y = mess.get("y");
            activity = mess.get("activity");

            isThis = mess.get("next").equals(this.name);
        }


        //服务器告诉客户端该操作了
        //player.setActivity("your_activity")

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
