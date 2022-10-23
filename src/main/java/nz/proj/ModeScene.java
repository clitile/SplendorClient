package nz.proj;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import nz.net.SocketClient;

import static com.almasb.fxgl.dsl.FXGL.getSceneService;
import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;

public class ModeScene extends SubScene {
	
    public static int mode;
    
    public static boolean online = false;
    
    public static String[] imageURLs = {"assets/textures/p1.png", "assets/textures/p2.png", "assets/textures/p3.png", "assets/textures/p4.png",
    		"assets/textures/p5.png", "assets/textures/p6.png", "assets/textures/p7.png", "assets/textures/p8.png"};
    
    Image[] images = new Image[8];
    
    ImageView imageview = new ImageView();
    
    public static int current = 0;
    
    public ModeScene() {
    	//这个面板做一个斜线，右侧放游戏人数的选择，左侧放玩家头像的的选择。

    	Pane pane = new Pane();

    	Button two_player = FXGL.getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Two Player" : "Pūpāpāho Rua");
        Button three_player = FXGL.getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Three Player" : "Pūpāpāho Toru");
        Button four_player = FXGL.getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Four Player" : "Pūpāpāho Whā");
        
        
        two_player.setLayoutX(1100);
        two_player.setLayoutY(140 - 90);
        two_player.setMinSize(600, 91);
        two_player.setStyle("-fx-background-image: url('assets/textures/butt.png')");
        
        
        three_player.setLayoutX(1140);
        three_player.setLayoutY(340 - 90);
        three_player.setMinSize(600, 91);
        three_player.setStyle("-fx-background-image: url('assets/textures/butt.png')");

        four_player.setLayoutX(1180);
        four_player.setLayoutY(540 - 90);
        four_player.setMinSize(600,91);
        four_player.setStyle("-fx-background-image: url('assets/textures/butt.png')");
        two_player.setOnAction(event -> {
        	FXGL.play("bu.wav");
            mode = 2;
            getSceneService().popSubScene();
            
            if (SocketClient.getInstance().login && online) {
                sendMatch(2);
                FXGL.getSceneService().pushSubScene(new MatchScene());
            }
        });
        three_player.setOnAction(event -> {
        	FXGL.play("bu.wav");
        	
            mode = 3;
            FXGL.getSceneService().popSubScene();
            if (SocketClient.getInstance().login && online) {
                sendMatch(3);
                FXGL.getSceneService().pushSubScene(new MatchScene());
            }
        });
        four_player.setOnAction(event -> {
        	FXGL.play("bu.wav");
        	
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
        		FXGL.play("bu.wav");
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
        borderpanet1.setMinSize(800, 264);
        borderpanet1.setStyle("-fx-background-image: url('assets/textures/tit1.png')");
        borderpanet1.setLayoutX(50);
        borderpanet1.setLayoutY(0);
        
        BorderPane borderpanet2 = new BorderPane();
        borderpanet2.setMinSize(800, 264);
        borderpanet2.setStyle("-fx-background-image: url('assets/textures/tit2.png')");
        borderpanet2.setLayoutX(1080);
        borderpanet2.setLayoutY(800);
        pane.getChildren().addAll(two_player, three_player, four_player, borderpane, next, borderpanet1, borderpanet2);

        Button find_friends = FXGL.getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Find Friends" : "Kimi Hoa");
        find_friends.setLayoutX(1220);
        find_friends.setLayoutY(740 - 90);
        find_friends.setMinSize(600,91);
        find_friends.setStyle("-fx-background-image: url('assets/textures/butt.png')");
        find_friends.setOnAction(actionEvent -> {
            if (online) {
                FXGL.play("bu.wav");

                GridPane friends_pane = new GridPane();
                friends_pane.setAlignment(Pos.CENTER);
                friends_pane.setHgap(30);
                friends_pane.setVgap(15);

                TextField name = new TextField("invite your friends via a name or names: name1 name2 ...");
                name.setPrefWidth(400);
                Button ok = getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "ok" : "Ka pai");

                ok.setOnAction(event -> {
                    String[] names = name.getText().split(" ");
                    mode = names.length + 1;
                    Bundle b = new Bundle("friends");
                    b.put("name", SocketClient.getInstance().name);
                    b.put("friends", names);
                    SocketClient.getInstance().send(b);
                });

                friends_pane.addRow(0, getUIFactoryService().newText(SplendorApp.lang.equals("english") ? "Usernames" : "Ngā Ingoa Kaiwhakamahi"));
                friends_pane.addRow(1, name);
                FXGL.getDialogService().showBox(SplendorApp.lang.equals("english") ? "Find Friends" : "Kimi Hoa", friends_pane, ok, getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Cancel" : "Whakakore"));
            } else {
                FXGL.getNotificationService().pushNotification("not login");
            }
        });
        pane.getChildren().add(find_friends);


        pane.setStyle("-fx-background-image: url('assets/textures/modescene2.png')");
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

    @Override
    protected void onUpdate(double tpf) {
        if (! SocketClient.getInstance().info_corr) {
            FXGL.getNotificationService().pushNotification(SplendorApp.lang.equals("english") ? "Information Error" : "Hapa Mōhiohio");
            SocketClient.getInstance().info_corr = true;
            mode = 0;
        }
        if (SocketClient.getInstance().match) {
            FXGL.getSceneService().popSubScene();
        }
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
