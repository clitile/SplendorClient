package nz.proj;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import nz.net.SocketClient;

public class ModeScene extends SubScene {
    public int mode = 0;
    public boolean online = false;
    public ModeScene() {
    	
    	Pane pane = new Pane();
    	
    	Button two_player = FXGL.getUIFactoryService().newButton("Two Player");
        Button three_player = FXGL.getUIFactoryService().newButton("Three Player");
        Button four_player = FXGL.getUIFactoryService().newButton("Four Player");
        Button cancel = FXGL.getUIFactoryService().newButton("Return");
        
        
        two_player.setLayoutX(600);
        two_player.setLayoutY(240);
        two_player.setMinSize(600, 91);
        two_player.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
        
        
        three_player.setLayoutX(620);
        three_player.setLayoutY(440);
        three_player.setMinSize(600, 91);
        three_player.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
       
        four_player.setLayoutX(640);
        four_player.setLayoutY(640);
        four_player.setMinSize(600,91);
        four_player.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
        
        cancel.setLayoutX(660);
        cancel.setLayoutY(840);
        cancel.setMinSize(600, 91);
        cancel.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
        
        pane.getChildren().add(two_player);
        pane.getChildren().add(three_player);
        pane.getChildren().add(four_player);
        pane.getChildren().add(cancel);
        
        pane.setStyle("-fx-background-image: url('assets/textures/S-mc.png')");
        pane.setPrefHeight(1080);
        pane.setPrefWidth(1920);

        pane.setLayoutX(0);
        pane.setLayoutY(0);
        
        getContentRoot().getChildren().add(pane);


        
        two_player.setOnAction(event -> {
            mode = 2;
            FXGL.getSceneService().popSubScene();
            if (SocketClient.getInstance().login && online) {
                sendMatch(2);
                FXGL.getSceneService().pushSubScene(new MatchScene());
            }
        });
        three_player.setOnAction(event -> {
            mode = 3;
            FXGL.getSceneService().popSubScene();
            if (SocketClient.getInstance().login && online) {
                sendMatch(3);
                FXGL.getSceneService().pushSubScene(new MatchScene());
            }
        });
        four_player.setOnAction(event -> {
            mode = 4;
            FXGL.getSceneService().popSubScene();
            if (SocketClient.getInstance().login && online) {
                sendMatch(4);
                FXGL.getSceneService().pushSubScene(new MatchScene());
            }
        });
        cancel.setOnAction(event -> FXGL.getSceneService().popSubScene());
    }

    private void sendMatch(int mode) {
        Bundle match = new Bundle("match");
        match.put("mode", Integer.toString(mode));
        match.put("name", SocketClient.getInstance().name);
        SocketClient.getInstance().send(match);
    }
}
