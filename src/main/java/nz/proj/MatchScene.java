package nz.proj;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nz.net.SocketClient;

public class MatchScene extends SubScene {
    public MatchScene() {
        Text text = new Text("Matching");
        text.setStyle("-fx-font-size: 40;");

        text.setTranslateX(FXGL.getAppWidth() * 0.45);
        text.setTranslateY(FXGL.getAppHeight() * 0.45);

        VBox pane = new VBox(text);
        pane.setStyle("-fx-background-image: url('assets/textures/backg_4.png')");
        pane.setPrefHeight(FXGL.getAppHeight());
        pane.setPrefWidth(FXGL.getAppWidth());

        pane.setLayoutX(0);
        pane.setLayoutY(0);
        getContentRoot().getChildren().add(pane);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (SocketClient.getInstance().match) {
            FXGL.getSceneService().popSubScene();
        }
    }
}
