package nz.ui;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import nz.comp.CardComponent;
import nz.comp.FirstCardComponent;
import nz.comp.OtherPlayersComponent;
import nz.proj.Config;
import nz.proj.SplendorApp;

public class OtherPlayersInfo extends SubScene {
	
	String score = OtherPlayersComponent.score;
	String goldcoin = OtherPlayersComponent.goldcoin;
	String[] gems = OtherPlayersComponent.gems;
	String[] othercoins = OtherPlayersComponent.othercoins;

	Text scoreText = new Text();
	Text goldcoinText = new Text();
	Text[] gemsText = new Text[5];
	Text[] othercoinsText = new Text[5];

	
	public OtherPlayersInfo() {
		Button exit = FXGL.getUIFactoryService().newButton("");
		exit.setOnAction(event -> {
			FXGL.play("button.wav");
			FXGL.getSceneService().popSubScene();
		});
		exit.setTranslateX(1500);
		exit.setTranslateY(320);
		exit.setMinHeight(65);
		exit.setMaxWidth(65);
		exit.setStyle("-fx-background-image: url('assets/textures/exit.png')");
		
		BorderPane borderpane = new BorderPane();
		Image image = new Image("assets/textures/otherInfo5.png");
		ImageView imageview = new ImageView(image);
		borderpane.setCenter(imageview);
		
		StackPane pane = new StackPane(borderpane);
		pane.setPrefSize(Config.APP_WIDTH, Config.APP_HEIGHT);
		pane.setStyle("-fx-background-color: #0006");
		
		scoreText = new Text(1393,465,score);
        scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        scoreText.setFill(Color.GOLD);
        scoreText.setStroke(Color.BLACK);
        
        goldcoinText = new Text(1063,675,goldcoin);
        goldcoinText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        goldcoinText.setFill(Color.YELLOW);
        goldcoinText.setStroke(Color.BLACK);
        
        for (int i=0; i<5; i++) {
        	if(i<=2) {
        		gemsText[i] = new Text(730, 465+105*i, gems[i]);
        		othercoinsText[i] = new Text(605, 465+105*i, othercoins[i]);
        	}else {
        		gemsText[i] = new Text(1130, 465+105*(i-3), gems[i]);
        		othercoinsText[i] = new Text(1000, 465+105*(i-3), othercoins[i]);
        	}
        	
        	gemsText[i].setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        	gemsText[i].setFill(Color.YELLOW);
        	gemsText[i].setStroke(Color.BLACK);
        	
        	
        	othercoinsText[i].setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        	othercoinsText[i].setFill(Color.WHITE);
        	othercoinsText[i].setStroke(Color.BLACK);
        }
        
        
		getContentRoot().getChildren().add(pane);
		getContentRoot().getChildren().add(exit);
		getContentRoot().getChildren().add(scoreText);
		getContentRoot().getChildren().add(goldcoinText);
		for(int i=0; i<5; i++) {
			getContentRoot().getChildren().add(gemsText[i]);
			getContentRoot().getChildren().add(othercoinsText[i]);
		}
		//getContentRoot().getChildren().addAll(savecards);
	}
	

}
