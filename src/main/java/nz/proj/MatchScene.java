package nz.proj;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

import java.util.ArrayList;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.net.SocketClient;

public class MatchScene extends SubScene {
	
	private final RotateTransition rt ;
	
    public MatchScene() {
        Text text = new Text("Matching");
        text.setStyle("-fx-font-size: 40;");
        Button cancel = FXGL.getUIFactoryService().newButton("Cancel");

        text.setTranslateX(FXGL.getAppWidth() * 0.45);
        text.setTranslateY(FXGL.getAppHeight() * 0.5);
        cancel.setTranslateX(FXGL.getAppWidth() * 0.44);
        cancel.setTranslateY(FXGL.getAppHeight() * 0.52);

        cancel.setOnAction(event -> {
            Bundle b = new Bundle("cancel");
            b.put("name", SocketClient.getInstance().name);
            b.put("mode", Config.MODE_SCENE.mode);
            SocketClient.getInstance().send(b);
            Config.MODE_SCENE.mode = 0;
            FXGL.getSceneService().popSubScene();
        });
        
       
        Image image = new Image("assets/textures/iu2.png");
		ImageView imageview = new ImageView(image);
		
		BorderPane borderpane = new BorderPane();
		borderpane.setCenter(imageview);
		borderpane.setLayoutX(700);
		borderpane.setLayoutY(280);
		
		rt = new RotateTransition(Duration.seconds(0.5), borderpane);
		rt.setByAngle(360);
		rt.setCycleCount(Animation.INDEFINITE);
		rt.setInterpolator(Interpolator.LINEAR);

        VBox pane = new VBox();
        pane.setStyle("-fx-background-image: url('assets/textures/S-castle.png')");
        pane.setPrefHeight(FXGL.getAppHeight());
        pane.setPrefWidth(FXGL.getAppWidth());

        pane.setLayoutX(0);
        pane.setLayoutY(0);
        getContentRoot().getChildren().add(pane);
        getContentRoot().getChildren().add(borderpane);
        getContentRoot().getChildren().add(text);
        getContentRoot().getChildren().add(cancel);
        //getContentRoot().getChildren().add(at);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (SocketClient.getInstance().match) {
            FXGL.getSceneService().popSubScene();
        }
    }
    
	@Override
	public void onCreate() {
		rt.play();
	}
	
	@Override
	public void onDestroy(){
		rt.stop();
	}
}
