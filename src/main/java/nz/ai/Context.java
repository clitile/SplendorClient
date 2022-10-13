package nz.ai;

import com.almasb.fxgl.entity.Entity;

public class Context {
    private Entity player;

    public Context(Entity player) {
        this.player = player;
    }

    public Entity getPlayer() {
        return player;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }
}
