package nz.proj;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nz.net.SocketClient;

public class MatchScene extends SubScene {
    public MatchScene() {
        VBox pane = new VBox(new Text("matching"));
        pane.setStyle("-fx-background-image: url('assets/textures/backg_4.png')");
        pane.setPrefHeight(FXGL.getAppHeight() * 0.6);
        pane.setPrefWidth(FXGL.getAppWidth() * 0.5);

        pane.setLayoutX(FXGL.getAppWidth() / 2.0 - pane.getPrefWidth() / 2.0);
        pane.setLayoutY(FXGL.getAppHeight() / 2.0 - pane.getPrefHeight() / 2.0);
        getContentRoot().getChildren().add(pane);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (SocketClient.getInstance().match) {
            FXGL.getSceneService().popSubScene();
        }
    }
}
