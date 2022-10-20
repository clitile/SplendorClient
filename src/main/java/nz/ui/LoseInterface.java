package nz.ui;

import static com.almasb.fxgl.dsl.FXGL.set;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import nz.net.SocketClient;
import nz.proj.Config;

public class LoseInterface extends SubScene{
	private final PathTransition pt1;
	private final PathTransition pt2;
	private final PathTransition pt3;
	private final PathTransition pt4;
	private final PathTransition pt5;
	
	public LoseInterface() {
		Button exit = FXGL.getUIFactoryService().newButton("Return to Menu");
		exit.setOnAction(event -> {
			FXGL.play("button.wav");
			FXGL.getSceneService().popSubScene();
			Config.MODE_SCENE.mode = 0;
            SocketClient.getInstance().match = false;
            set("match", false);
			FXGL.getGameController().gotoMainMenu();
		});
		exit.setTranslateX(1320);
		exit.setTranslateY(960);
		exit.setMinHeight(91);
		exit.setMinWidth(600);
		exit.setStyle("-fx-background-image: url('assets/textures/butt.png')");
		
		BorderPane borderpane = new BorderPane();
		Image image = new Image("assets/textures/lose.png");
		ImageView imageview = new ImageView(image);
		borderpane.setCenter(imageview);
		borderpane.setLayoutX(700);
		borderpane.setLayoutY(10);
		
		Image image1 = new Image("assets/textures/gem/gem_10.png");
		ImageView imageview1 = new ImageView(image1);
		
		Image image2 = new Image("assets/textures/gem/gem_11.png");
		ImageView imageview2 = new ImageView(image2);
		
		Image image3 = new Image("assets/textures/gem/gem_12.png");
		ImageView imageview3 = new ImageView(image3);
		
		Image image4 = new Image("assets/textures/gem/gem_13.png");
		ImageView imageview4 = new ImageView(image4);
		
		Image image5 = new Image("assets/textures/gem/gem_14.png");
		ImageView imageview5 = new ImageView(image5);
		
		
		
		BorderPane borderpane1 = new BorderPane();
		BorderPane borderpane2 = new BorderPane();
		BorderPane borderpane3 = new BorderPane();
		BorderPane borderpane4 = new BorderPane();
		BorderPane borderpane5 = new BorderPane();
		
		
		
		borderpane1.setCenter(imageview1);
		borderpane2.setCenter(imageview2);
		borderpane3.setCenter(imageview3);
		borderpane4.setCenter(imageview4);
		borderpane5.setCenter(imageview5);
		
		
		StackPane pane = new StackPane();
		pane.setPrefSize(Config.APP_WIDTH, Config.APP_HEIGHT);
		pane.setStyle("-fx-background-image: url('assets/textures/end.png')");
		
		Path path1 = new Path();
		path1.getElements().add(new MoveTo(1650, 800));
		path1.getElements().add(new CubicCurveTo(1350, 100, 1450, 500, 1650, 800));
		pt1 = new PathTransition();
		pt1.setDuration(Duration.millis(2500));
		pt1.setPath(path1);
		pt1.setNode(borderpane1);
		pt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt1.setCycleCount(Timeline.INDEFINITE);
		
		Path path2 = new Path();
		path2.getElements().add(new MoveTo(450, 800));
		path2.getElements().add(new CubicCurveTo(250, 100, 350, 500, 450, 800));
		pt2 = new PathTransition();
		pt2.setDuration(Duration.millis(2100));
		pt2.setPath(path2);
		pt2.setNode(borderpane2);
		pt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt2.setCycleCount(Timeline.INDEFINITE);
		
		Path path3 = new Path();
		path3.getElements().add(new MoveTo(750, 800));
		path3.getElements().add(new CubicCurveTo(550, 100, 650, 500, 750, 800));
		pt3 = new PathTransition();
		pt3.setDuration(Duration.millis(2200));
		pt3.setPath(path3);
		pt3.setNode(borderpane3);
		pt3.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt3.setCycleCount(Timeline.INDEFINITE);
		
		Path path4 = new Path();
		path4.getElements().add(new MoveTo(1050, 800));
		path4.getElements().add(new CubicCurveTo(850, 100, 950, 500, 1050, 800));
		pt4 = new PathTransition();
		pt4.setDuration(Duration.millis(2300));
		pt4.setPath(path4);
		pt4.setNode(borderpane4);
		pt4.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt4.setCycleCount(Timeline.INDEFINITE);
		
		Path path5 = new Path();
		path5.getElements().add(new MoveTo(1350, 800));
		path5.getElements().add(new CubicCurveTo(1150, 100, 1250, 500, 1350, 800));
		pt5 = new PathTransition();
		pt5.setDuration(Duration.millis(2400));
		pt5.setPath(path5);
		pt5.setNode(borderpane5);
		pt5.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt5.setCycleCount(Timeline.INDEFINITE);
		
	
		
		getContentRoot().getChildren().add(pane);
		getContentRoot().getChildren().add(borderpane);
		getContentRoot().getChildren().add(borderpane1);
		getContentRoot().getChildren().add(borderpane2);
		getContentRoot().getChildren().add(borderpane3);
		getContentRoot().getChildren().add(borderpane4);
		getContentRoot().getChildren().add(borderpane5);
		getContentRoot().getChildren().add(exit);
		//getContentRoot().getChildren().add(pt1);
		
		
		
	}
	
	@Override
	public void onCreate() {
		pt1.play();
		pt2.play();
		pt3.play();
		pt4.play();
		pt5.play();
	}
	
	@Override
	public void onDestroy(){
		pt1.stop();
		pt2.stop();
		pt3.stop();
		pt4.stop();
		pt5.stop();
	}

}

