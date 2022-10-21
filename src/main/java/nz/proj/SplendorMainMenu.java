package nz.proj;

import com.almasb.fxgl.app.scene.FXGLMenu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.DialogService;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.net.SocketClient;
import nz.ui.Instruction;
import nz.ui.Story;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SplendorMainMenu extends FXGLMenu {

    int t = 0;
	
	private LazyValue<Story> storyLazyValue = new LazyValue<>(() -> {
		return new Story();
	});
	
    int temp = 0;
    public SplendorMainMenu() {
        super(MenuType.MAIN_MENU);
        getPrimaryStage().setOnCloseRequest(event -> {
            if (SocketClient.getInstance().login) {
                Bundle b = new Bundle("close");
                b.put("name", SocketClient.getInstance().name);
                SocketClient.getInstance().send(b);
                SocketClient.getInstance().close();
            } else if (SocketClient.getInstance().isOpen()) {
                SocketClient.getInstance().close();
            }
        });
//        loopBGM(Config.BackMusic);

        getContentRoot().getChildren().setAll(texture("backg (2).png",Config.APP_WIDTH,Config.APP_HEIGHT));
        var blocks = new ArrayList<ColorBlock>();

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, Config.BlockColor);
            block.setTranslateX(getAppWidth() / 2.0 - 380 + i*50);
            block.setTranslateY(1);
            blocks.add(block);
            getContentRoot().getChildren().add(block);
            var block1 = new ColorBlock(40, Config.BlockColor);
            block1.setTranslateX(getAppWidth() / 2.0 - 380 + i*50);
            block1.setTranslateY(190);
            blocks.add(block1);
            getContentRoot().getChildren().add(block1);
        }
        for (int i = 0; i < 3; i++) {
            var block = new ColorBlock(40, Config.BlockColor);
            block.setTranslateX(getAppWidth() / 2.0 - 380 );
            block.setTranslateY(45+ i*50);
            blocks.add(block);
            getContentRoot().getChildren().add(block);
            var block1 = new ColorBlock(40, Config.BlockColor);
            block1.setTranslateX(getAppWidth() / 2.0 + 320 );
            block1.setTranslateY(45+ i*50);
            blocks.add(block1);
            getContentRoot().getChildren().add(block1);
        }
        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.get(i);
            animationBuilder()
                    .delay(Duration.seconds(i * 0.05))
                    .duration(Duration.seconds(0.5))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .animate(block.fillProperty())
                    .from(Config.BlockColor)
                    .to(Config.BlockColor.brighter().brighter())
                    .buildAndPlay(this);
        }

        var menuBox = new VBox(
                5,
                new MenuButton(SplendorApp.lang.equals("english") ? "Play with AI" : "Purei me AI", () -> {
                	
                	//现在先把动画去掉了，需要换一种方式。
                	//getContentRoot().getChildren().setAll(texture("S-castle.png",Config.APP_WIDTH,Config.APP_HEIGHT));
                	//getGameWorld().addEntityFactory(new SplendorFactory());
                	//spawn("cutscene");
                	//getSceneService().pushSubScene(storyLazyValue.get());
                	//getSceneService().popSubScene();
                	FXGL.play("bu.wav");
                	
                    Config.MODE_SCENE.online = false;
                    getSceneService().pushSubScene(Config.MODE_SCENE);
                }),
                new MenuButton(SplendorApp.lang.equals("english") ? "Online Game" : "Kēmu Tuihono", this::onlineGame),
                //new MenuButton("How to Play", this::instructions),
                new MenuButton(SplendorApp.lang.equals("english") ? "How to Play" : "Me Pēhea te Tākaro", () -> {
                	FXGL.play("bu.wav");
                	getSceneService().pushSubScene(new Instruction());
                }),
                new MenuButton(SplendorApp.lang.equals("english") ? "Settings" : "Ngā Tautuhinga", () -> {
                	FXGL.play("button.wav");
                	FXGL.getGameController().gotoGameMenu();
                }),
                
                new MenuButton(SplendorApp.lang.equals("english") ? "Exit" : "E puta", () -> {
                	FXGL.play("bu.wav");
                    if (SocketClient.getInstance().login) {
                        Bundle b = new Bundle("close");
                        b.put("name", SocketClient.getInstance().name);
                        SocketClient.getInstance().send(b);
                        SocketClient.getInstance().close();
                    } else if (SocketClient.getInstance().isOpen()) {
                        SocketClient.getInstance().close();
                    }
                    fireExit();
                })
        );
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(getAppWidth() / 2.0 - 140);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 200);
        getContentRoot().getChildren().addAll(menuBox);
       
    }

    private void loginPane() {
        if (!SocketClient.getInstance().login) {
            if (!SocketClient.getInstance().isOpen()) {
                SocketClient.getInstance().connect();
            }

            GridPane pane = new GridPane();
            pane.setAlignment(Pos.CENTER);
            DialogService dialogService = getDialogService();
            pane.setHgap(20);
            pane.setVgap(15);

            TextField name_inp = new TextField();
            PasswordField password_inp = new PasswordField();
            pane.addRow(0, getUIFactoryService().newText(SplendorApp.lang.equals("english") ? "Name" : "Ingoa"), name_inp);
            pane.addRow(1, getUIFactoryService().newText(SplendorApp.lang.equals("english") ? "Password" : "Kupuhipa"), password_inp);

            Button ack = getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "SignIn" : "Takiuru");
            Button signup = getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "SignUp" : "whakauru");
            Button retrieve = getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Retrieve pwd" : "Tautuhi anō i tō kupuhipa");

            ack.setOnAction(actionEvent -> {
            	FXGL.play("bu.wav");
                if (! SocketClient.getInstance().login) {
                    String name = name_inp.getText();
                    String pwd = password_inp.getText();
                    Bundle loginBundle = new Bundle("login");
                    loginBundle.put("name", name);
                    loginBundle.put("pwd", pwd);
                    SocketClient.getInstance().send(loginBundle);
                    SocketClient.getInstance().name = name;
                }

            });

            signup.setOnAction(actionEvent -> {
            	FXGL.play("bu.wav");
                GridPane signup_pane = new GridPane();
                signup_pane.setAlignment(Pos.CENTER);
                signup_pane.setHgap(20);
                signup_pane.setVgap(15);
                TextField newName = new TextField();
                TextField newAcc = new TextField();
                PasswordField newPwd = new PasswordField();

                Button ok = getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "SignUp" : "whakauru");
                ok.setOnAction(event -> {
                	FXGL.play("bu.wav");
                    Bundle sign_up = new Bundle("signup");
                    sign_up.put("name", newName.getText());
                    sign_up.put("pwd", newPwd.getText());
                    sign_up.put("acc", newAcc.getText());
                    SocketClient.getInstance().send(sign_up);
                });

                signup_pane.addRow(0, getUIFactoryService().newText(SplendorApp.lang.equals("english") ? "Set Username" : "Tautuhi Ingoa Kaiwhakamahi"), newName);
                signup_pane.addRow(1, getUIFactoryService().newText(SplendorApp.lang.equals("english") ? "Set Account" : "Tautuhi Pūkete"), newAcc);
                signup_pane.addRow(2, getUIFactoryService().newText(SplendorApp.lang.equals("english") ? "Set Password" : "Tautuhi Kupuhipa"), newPwd);
                dialogService.showBox("Login", signup_pane, ok, getUIFactoryService().newButton(SplendorApp.lang.equals("english") ? "Cancel" : "Whakakore"));
            });

            retrieve.setOnAction(actionEvent -> {
            	FXGL.play("bu.wav");
                GridPane reset_pane = new GridPane();
                reset_pane.setAlignment(Pos.CENTER);
                reset_pane.setHgap(20);
                reset_pane.setVgap(15);
                TextField re_name = new TextField();
                PasswordField re_pwd = new PasswordField();

                Button ok = getUIFactoryService().newButton("Reset");
                ok.setOnAction(event -> {
                	FXGL.play("bu.wav");
                    Bundle re = new Bundle("reset");
                    re.put("name", re_name.getText());
                    re.put("pwd", re_pwd.getText());
                    SocketClient.getInstance().send(re);
                });

                reset_pane.addRow(0, getUIFactoryService().newText("Your Username"), re_name);
                reset_pane.addRow(1, getUIFactoryService().newText("Set Password"), re_pwd);
                dialogService.showBox("Login", reset_pane, ok, getUIFactoryService().newButton("Cancel"));
            });
            pane.addRow(2, signup, retrieve);
            dialogService.showBox("Login", pane, ack, getUIFactoryService().newButton("Cancel"));
        }
    }

    private void onlineGame() {
    	FXGL.play("bu.wav");
        if (!isReachable()) {
            getDialogService().showMessageBox(SplendorApp.lang.equals("english") ? "Server Error" : "Hapa Tūmau");
        } else {
            if (!SocketClient.getInstance().login) {
                loginPane();
            }
            if (temp == 1) {
                temp = 0;
            }
        }
    }
    
    //展示游戏规则，还需要改进
    private void instructions() {
    	//做成一个面板上面有左右两侧按钮，点击后更换带有游戏解释的图片。
        //getDialogService().showMessageBox("Right now: Use your mouse to choose cards and gems ~");
    	
        
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 50.0);
            text.strokeProperty().bind(text.fillProperty());
            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.BLUE)
                            .otherwise(Color.WHITE)
            );
            setOnMouseClicked(e -> action.run());
            setPickOnBounds(true);
            getChildren().add(text);
        }
    }
    public static class ColorBlock extends Rectangle {
        public ColorBlock(int size, Color color) {
            super(size, size, color);
            if (!FXGL.isMobile()) {
                var shadow = new InnerShadow(25, Color.BLACK);
                shadow.setOffsetX(-3);
                shadow.setOffsetY(-3);
                setEffect(shadow);
            }
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        if (t == 0 && !getSettings().getLanguage().get().getName().toLowerCase().equals(SplendorApp.lang)) {
            getDialogService().showMessageBox(SplendorApp.lang.equals("english") ? "You should restart the game after changing language!" : "Me tīmata anō te kēmu i muri i te huri reo");
            t = 1;
            try {
                File writeName = new File("src/main/java/language.txt");
                writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
                try (FileWriter writer = new FileWriter(writeName);
                     BufferedWriter out = new BufferedWriter(writer)
                ) {
                    out.write(getSettings().getLanguage().get().getName().toLowerCase());
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Config.MODE_SCENE.mode != 0 && !Config.MODE_SCENE.online) {
            fireNewGame();
        } else if (SocketClient.getInstance().login) {
            if (temp == 0) {
                Config.MODE_SCENE.online = true;
                getSceneService().pushSubScene(Config.MODE_SCENE);
                FXGL.getNotificationService().pushNotification(SplendorApp.lang.equals("english") ? "Login Successfully" : "I tutuki pai te takiuru");
                temp = 1;
            }
            if (SocketClient.getInstance().match) {
                fireNewGame();
            }
        }
        if (! SocketClient.getInstance().info_corr) {
            FXGL.getNotificationService().pushNotification(SplendorApp.lang.equals("english") ? "Information Error" : "Hapa Mōhiohio");
            SocketClient.getInstance().info_corr = true;
        }
    }

    private boolean isReachable() {
        boolean reachable;
        // 如果端口为空，使用 isReachable 检测，非空使用 socket 检测
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(Config.IP, Integer.parseInt(Config.PORT)));
            reachable = true;
        } catch (Exception e) {
            reachable = false;
        }
        return reachable;
    }
}
