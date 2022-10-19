package nz.ui;

import com.almasb.fxgl.app.scene.*;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.proj.Config;

public class Loadingscene extends LoadingScene{
	
	private final RotateTransition rt ;
	
	public Loadingscene() {
		
	
		Image image = new Image("assets/textures/iu3.png");
		ImageView imageview = new ImageView(image);
		
		BorderPane borderpane = new BorderPane();
		borderpane.setCenter(imageview);
		
		Text text = new Text("Loading");
        text.setStyle("-fx-font-size: 40;");
		text.setTranslateX(FXGL.getAppWidth() * 0.45);
        text.setTranslateY(FXGL.getAppHeight() * 0.5);
        
        StackPane pane = new StackPane(borderpane);
		pane.setPrefSize(Config.APP_WIDTH, Config.APP_HEIGHT);
		pane.setStyle("-fx-background-image: url('assets/textures/S-castle.png')");
		
		
		rt = new RotateTransition(Duration.seconds(0.1), borderpane);
		rt.setByAngle(360);
		rt.setCycleCount(Animation.INDEFINITE);
		rt.setInterpolator(Interpolator.LINEAR);
		
		getContentRoot().getChildren().add(pane);
		getContentRoot().getChildren().add(text);
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
