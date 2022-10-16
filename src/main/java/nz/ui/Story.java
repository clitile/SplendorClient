package nz.ui;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getCutsceneService;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getSceneService;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import java.util.List;

import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import nz.proj.Config;
import nz.proj.SplendorFactory;


public class Story extends SubScene {
	public Story(){
		
        List<String> lines = getAssetLoader().loadText("animation1.txt");
	   	Cutscene cutscene = new Cutscene(lines);
	   	getCutsceneService().startCutscene(cutscene);
	   	Button exit = FXGL.getUIFactoryService().newButton("");
		exit.setOnAction(event -> FXGL.getSceneService().popSubScene());
		exit.setTranslateX(50);
		exit.setMinHeight(65);
		exit.setMaxWidth(65);
		exit.setStyle("-fx-background-image: url('assets/textures/exit.png')");
		getContentRoot().getChildren().add(exit);
        
	}
}
