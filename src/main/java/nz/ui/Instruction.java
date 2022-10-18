package nz.ui;

import static com.almasb.fxgl.dsl.FXGL.getSceneService;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import nz.net.SocketClient;
import nz.proj.Config;
import nz.proj.MatchScene;

public class Instruction extends SubScene {
	
	public static String[] imageURLs = {"assets/textures/instructions/ins1.png", "assets/textures/instructions/ins2.png", 
			"assets/textures/instructions/ins3.png", "assets/textures/instructions/ins4.png",
    		"assets/textures/instructions/ins5.png", "assets/textures/instructions/ins6.png", 
    		"assets/textures/instructions/ins7.png", "assets/textures/instructions/ins8.png",
    		"assets/textures/instructions/ins9.png", "assets/textures/instructions/ins10.png",
    		"assets/textures/instructions/ins11.png", "assets/textures/instructions/ins12.png",
    		"assets/textures/instructions/ins13.png", "assets/textures/instructions/ins14.png",
    		"assets/textures/instructions/ins15.png", "assets/textures/instructions/ins16.png",
    		"assets/textures/instructions/ins17.png", "assets/textures/instructions/ins18.png",
    		"assets/textures/instructions/ins19.png", "assets/textures/instructions/ins20.png",
    		"assets/textures/instructions/ins21.png", "assets/textures/instructions/ins22.png",
    		"assets/textures/instructions/ins23.png",};
	
    
    Image[] images = new Image[23];
    
    ImageView imageview = new ImageView();
    
    public static int current = 0;
	
	public Instruction() {
		//做一个面板，用来展示游戏规则 -> 左右按键控制图片展示，前几张图片可以展示基本规则，最后一张用文字叙述即可。
		//木板为背景，上面贴上游戏画面 加上说明性文字。
		for(int i=0; i<images.length; i++) {
        	images[i] = new Image(imageURLs[i]);
        }
    	
    	Button right = FXGL.getUIFactoryService().newButton("");
        Button left = FXGL.getUIFactoryService().newButton("");
        Button exit = FXGL.getUIFactoryService().newButton("");
        
        
        right.setLayoutX(1560);
        right.setLayoutY(500);
        right.setMinHeight(71);
		right.setMaxWidth(75);
        right.setStyle("-fx-background-image: url('assets/textures/S-a1.png')");
        
        
        left.setLayoutX(280);
        left.setLayoutY(500);
        left.setMinHeight(71);
        left.setMaxWidth(75);
        left.setStyle("-fx-background-image: url('assets/textures/S-a2.png')");
       
        exit.setTranslateX(1550);
		exit.setTranslateY(90);
        exit.setMinHeight(65);
		exit.setMaxWidth(65);
        exit.setStyle("-fx-background-image: url('assets/textures/exit.png')");
        
        right.setOnAction(event -> {
        	shownext();
        });
        left.setOnAction(event -> {
        	showlast();
        });
        
        exit.setOnAction(event -> {
            FXGL.getSceneService().popSubScene();
        });
        
        
        BorderPane borderpane = new BorderPane();
        //imageview.setPreserveRatio(true);
        imageview.setFitWidth(1304);
        imageview.setFitHeight(900);
        imageview.setImage(images[current]);

		borderpane.setCenter(imageview);
        
        StackPane pane = new StackPane(borderpane);
		pane.setPrefSize(Config.APP_WIDTH, Config.APP_HEIGHT);
		pane.setStyle("-fx-background-color: #0006");
		
		getContentRoot().getChildren().add(pane);
		getContentRoot().getChildren().add(exit);
		getContentRoot().getChildren().add(right);
		getContentRoot().getChildren().add(left);

	}
	
	private void shownext() {
		if (current +1 < 23) {
    		current += 1;
    	}else {
    		current = 0;
    	}
		imageview.setImage(images[current]);
	}
	
	private void showlast() {
		if (current -1 >= 0) {
    		current -= 1;
    	}else {
    		current = 22;
    	}
		imageview.setImage(images[current]);
	}
}
