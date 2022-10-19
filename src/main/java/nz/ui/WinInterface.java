package nz.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import nz.proj.Config;

public class WinInterface extends SubScene{
	
	private final PathTransition pt1;
	private final PathTransition pt2;
	//private final PathTransition pt3;
	
	public WinInterface() {
		Button exit = FXGL.getUIFactoryService().newButton("Return to Menu");
		exit.setOnAction(event -> {
			FXGL.play("button.wav");
			FXGL.getSceneService().popSubScene();
		});
		exit.setTranslateX(900);
		exit.setTranslateY(900);
		exit.setMinHeight(91);
		exit.setMinWidth(600);
		exit.setStyle("-fx-background-image: url('assets/textures/butt.png')");
		
		Image image1 = new Image("assets/textures/win1.png");
		ImageView imageview1 = new ImageView(image1);
		
		Image image2 = new Image("assets/textures/win2.png");
		ImageView imageview2 = new ImageView(image2);
		
		BorderPane borderpane1 = new BorderPane();
		BorderPane borderpane2 = new BorderPane();
		
		borderpane1.setCenter(imageview1);
		borderpane2.setCenter(imageview2);
		
		StackPane pane = new StackPane();
		pane.setPrefSize(Config.APP_WIDTH, Config.APP_HEIGHT);
		pane.setStyle("-fx-background-color: #0006");
		
		Path path1 = new Path();
		path1.getElements().add(new MoveTo(400, 800));
		path1.getElements().add(new CubicCurveTo(200, 100, 300, 500, 400, 800));
		pt1 = new PathTransition();
		pt1.setDuration(Duration.millis(2000));
		pt1.setPath(path1);
		pt1.setNode(borderpane2);
		pt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt1.setCycleCount(Timeline.INDEFINITE);
		
		Path path2 = new Path();
		path2.getElements().add(new MoveTo(600, 800));
		path2.getElements().add(new CubicCurveTo(600, 800, 500, 500, 400, 100));
		pt2 = new PathTransition();
		pt2.setDuration(Duration.millis(2000));
		pt2.setPath(path1);
		pt2.setNode(borderpane2);
		pt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt2.setCycleCount(Timeline.INDEFINITE);
		
		
		
		getContentRoot().getChildren().add(pane);
		getContentRoot().getChildren().add(borderpane1);
		getContentRoot().getChildren().add(exit);
		//getContentRoot().getChildren().add(pt1);
		
		
		
	}
	
	@Override
	public void onCreate() {
		pt1.play();
		pt2.play();
	}
	
	@Override
	public void onDestroy(){
		pt1.stop();
		pt2.play();
	}

}
