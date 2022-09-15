package nz.proj;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import nz.net.SocketClient;

public class MatchScene extends SubScene {
    public MatchScene() {

    }

    @Override
    protected void onUpdate(double tpf) {
        if (SocketClient.getInstance().match) {
            FXGL.getSceneService().popSubScene();
        }
    }
}
