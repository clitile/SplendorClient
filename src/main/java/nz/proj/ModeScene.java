package nz.proj;

import static com.almasb.fxgl.dsl.FXGL.getSceneService;
import static com.almasb.fxgl.dsl.FXGL.texture;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import nz.net.SocketClient;
import nz.ui.Story;

public class ModeScene extends SubScene {
	
	
	
    public static int mode;
    
    public static boolean online = false;
    
    public static String[] imageURLs = {"assets/textures/p1.png", "assets/textures/p2.png", "assets/textures/p3.png", "assets/textures/p4.png",
    		"assets/textures/p5.png", "assets/textures/p6.png", "assets/textures/p7.png", "assets/textures/p8.png",};
    
    Image[] images = new Image[8];
    
    ImageView imageview = new ImageView();
    
    public static int current = 0;
    
    public ModeScene() {
    	//这个面板做一个斜线，右侧放游戏人数的选择，左侧放玩家头像的的选择。
    	
    	Pane pane = new Pane();
    	
    	Button two_player = FXGL.getUIFactoryService().newButton("Two Player");
        Button three_player = FXGL.getUIFactoryService().newButton("Three Player");
        Button four_player = FXGL.getUIFactoryService().newButton("Four Player");
        
        
        two_player.setLayoutX(1100);
        two_player.setLayoutY(140);
        two_player.setMinSize(600, 91);
        two_player.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
        
        
        three_player.setLayoutX(1140);
        three_player.setLayoutY(340);
        three_player.setMinSize(600, 91);
        three_player.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
       
        four_player.setLayoutX(1180);
        four_player.setLayoutY(540);
        four_player.setMinSize(600,91);
        four_player.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
        
        two_player.setOnAction(event -> {
            mode = 2;
            getSceneService().popSubScene();
            
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
        
        
        //加一个玩家头像选择的区域。
        BorderPane borderpane = new BorderPane();
        
        Button next = new Button(" ");
        next.setMinSize(26, 44);
        next.setLayoutX(600);
        next.setLayoutY(645);
        next.setStyle("-fx-background-image: url('assets/textures/S-arrow.png')");
        next.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent actionEvent) {
        		showNext();
        	}
        });
        
        for(int i=0; i<images.length; i++) {
        	images[i] = new Image(imageURLs[i]);
        }
        imageview.setPreserveRatio(true);
        imageview.setFitWidth(330);
        imageview.setFitHeight(460);
        
        borderpane.setMinSize(418, 530);
        borderpane.setStyle("-fx-background-image: url('assets/textures/S-cha.png')");
        borderpane.setCenter(imageview);
        borderpane.setLayoutX(200);
        borderpane.setLayoutY(430);
        showNext();
        
        //Titles:
        BorderPane borderpanet1 = new BorderPane();
        borderpanet1.setMinSize(800, 338);
        borderpanet1.setStyle("-fx-background-image: url('assets/textures/title1.png')");
        borderpanet1.setLayoutX(50);
        borderpanet1.setLayoutY(0);
        
        BorderPane borderpanet2 = new BorderPane();
        borderpanet2.setMinSize(800, 264);
        borderpanet2.setStyle("-fx-background-image: url('assets/textures/title2.png')");
        borderpanet2.setLayoutX(1150);
        borderpanet2.setLayoutY(800);
        
        pane.getChildren().add(two_player);
        pane.getChildren().add(three_player);
        pane.getChildren().add(four_player);
        pane.getChildren().add(borderpane);
        pane.getChildren().add(next);
        pane.getChildren().add(borderpanet1);
        pane.getChildren().add(borderpanet2);
        
        
        pane.setStyle("-fx-background-image: url('assets/textures/modescene.png')");
        pane.setPrefHeight(1080);
        pane.setPrefWidth(1920);

        pane.setLayoutX(0);
        pane.setLayoutY(0);
        
        getContentRoot().getChildren().add(pane);


        //Button cancel = FXGL.getUIFactoryService().newButton("Return");
        //pane.getChildren().add(cancel);
        //cancel.setLayoutX(320);
        //cancel.setLayoutY(840);
        //cancel.setMinSize(600, 91);
        //cancel.setStyle("-fx-background-image: url('assets/textures/S-butt.png')");
        //cancel.setOnAction(event -> FXGL.getSceneService().popSubScene());
        
        
    }
    
    

    private void sendMatch(int mode) {
        Bundle match = new Bundle("match");
        match.put("mode", Integer.toString(mode));
        match.put("name", SocketClient.getInstance().name);
        SocketClient.getInstance().send(match);
    }
    
    private void showNext() {
    	current ++;
    	if(current >= 8) {
    		current = 0;
    	}
    	imageview.setImage(images[current]);
    }
    
}
