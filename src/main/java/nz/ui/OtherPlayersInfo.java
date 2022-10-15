package nz.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import nz.proj.Config;

public class OtherPlayersInfo extends SubScene {
	public OtherPlayersInfo() {
		Button exit = FXGL.getUIFactoryService().newButton("");
		exit.setOnAction(event -> FXGL.getSceneService().popSubScene());
		exit.setTranslateX(1650);
		exit.setMinHeight(65);
		exit.setMaxWidth(65);
		exit.setStyle("-fx-background-image: url('assets/textures/exit.png')");
		
		BorderPane borderpane = new BorderPane();
		Image image = new Image("assets/textures/otherInfo2.png");
		ImageView imageview = new ImageView(image);

		borderpane.setCenter(imageview);
		
		StackPane pane = new StackPane(borderpane);
		pane.setPrefSize(Config.APP_WIDTH, Config.APP_HEIGHT);
		pane.setStyle("-fx-background-color: #0006");
		getContentRoot().getChildren().add(pane);
		getContentRoot().getChildren().add(exit);
	}

}
