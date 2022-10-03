package nz.proj;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nz.net.SocketClient;

public class MatchScene extends SubScene {
    public MatchScene() {
        Text text = new Text("Matching");
        text.setStyle("-fx-font-size: 40;");
        Button cancel = FXGL.getUIFactoryService().newButton("Cancel");

        text.setTranslateX(FXGL.getAppWidth() * 0.45);
        text.setTranslateY(FXGL.getAppHeight() * 0.45);
        cancel.setTranslateX(FXGL.getAppWidth() * 0.45);
        cancel.setTranslateY(FXGL.getAppHeight() * 0.45);

        cancel.setOnAction(event -> {
            Bundle b = new Bundle("cancel");
            b.put("name", SocketClient.getInstance().name);
            b.put("mode", Config.MODE_SCENE.mode);
            SocketClient.getInstance().send(b);
            Config.MODE_SCENE.mode = 0;
            FXGL.getSceneService().popSubScene();
        });

        VBox pane = new VBox(text, cancel);
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
