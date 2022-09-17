package nz.proj;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import nz.net.SocketClient;

public class ModeScene extends SubScene {
    public int mode = 0;
    public ModeScene() {
        Button two_player = FXGL.getUIFactoryService().newButton("Two Player");
        Button three_player = FXGL.getUIFactoryService().newButton("Three Player");
        Button four_player = FXGL.getUIFactoryService().newButton("Four Player");
        VBox pane = new VBox(two_player, three_player, four_player);
        pane.setStyle("-fx-background-image: url('assets/textures/backg_4.png')");
        pane.setPrefHeight(FXGL.getAppHeight() * 0.6);
        pane.setPrefWidth(FXGL.getAppWidth() * 0.5);

        pane.setLayoutX(FXGL.getAppWidth() / 2.0 - pane.getPrefWidth() / 2.0);
        pane.setLayoutY(FXGL.getAppHeight() / 2.0 - pane.getPrefHeight() / 2.0);
        getContentRoot().getChildren().add(pane);

        two_player.setOnAction(event -> {
            mode = 2;
            sendMatch(2);
            FXGL.getSceneService().popSubScene();
            FXGL.getSceneService().pushSubScene(new MatchScene());
        });
        three_player.setOnAction(event -> {
            mode = 3;
            sendMatch(3);
            FXGL.getSceneService().popSubScene();
            FXGL.getSceneService().pushSubScene(new MatchScene());
        });
        four_player.setOnAction(event -> {
            mode = 4;
            sendMatch(4);
            FXGL.getSceneService().popSubScene();
            FXGL.getSceneService().pushSubScene(new MatchScene());
        });
    }

    private void sendMatch(int mode) {
        Bundle match = new Bundle("match");
        match.put("mode", Integer.toString(mode));
        match.put("name", SocketClient.getInstance().name);
        SocketClient.getInstance().send(match);
    }
}
